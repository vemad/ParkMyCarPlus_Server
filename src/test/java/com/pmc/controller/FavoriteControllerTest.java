package com.pmc.controller;

import com.Application;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.pmc.dao.UserDao;
import com.pmc.model.User;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Created on 09/03/15.
 *
 * @author stephaneki
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class FavoriteControllerTest {

    @Value("${local.server.port}")
    int port;

    @Autowired
    private UserDao userDao;

    private User user;
    private String token;

    @Before
    public void setUp() {

        user = new User().setUsername("username").setPassword("password");
        userDao.save(user);

        RestAssured.port = port;
    }

    public void authenticate(){
        String data= "password="+user.getPassword()+"&username="+user.getUsername()+
                "&grant_type=password&scope=read write";

        String jsonResponse = given().
                contentType("application/x-www-form-urlencoded").
                body(data).
                auth().preemptive().basic("pmcAndroid", "123456").
                when().
                post("/oauth/token").asString();
        token = new JsonPath(jsonResponse).getString("access_token");
    }

    @Test
    public void testFavCanBeCreated() throws Exception {
        authenticate();
        float latitude=1.2f;
        float longitude=2.3f;
        String address="50 rue La patate douce";
        String fav= "{\"latitude\": "+latitude+", \"longitude\": "+longitude+"," +
                " \"address\":\""+address+"\"}";
        given().
                header("Authorization", "Bearer "+token).
                contentType(ContentType.JSON).
                body(fav).
        when().
                //log().all().
                post("/rest/favorites").

        then().
                //log().all().
                statusCode(HttpStatus.SC_CREATED).
                body("id",notNullValue()).
                body("latitude", is(latitude)).
                body("longitude", is(longitude)).
                body("address", is(address)).
                body("density", notNullValue()).
                body("intensity", notNullValue());
    }
}
