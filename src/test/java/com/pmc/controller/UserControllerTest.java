package com.pmc.controller;

import com.Application;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.pmc.dao.UserDao;
import com.pmc.model.User;
import org.apache.http.HttpStatus;
import org.junit.After;
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
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.Matchers.is;

/**
 * Created by stephaneki on 05/03/15.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class UserControllerTest {

    @Autowired
    private UserDao userDao;
    @Value("${local.server.port}")
    int port;

    private User user;
    private String token;

    @Before
    public void setUp() {

        user = new User().setUsername("username").setPassword("password");
        //The database is cleared and re-initialized for each test so that we always
        //validate against a known state
        userDao.deleteAll();
        userDao.save(user);

        RestAssured.port = port;
    }

    public void authenticate(User user){
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

    @After
    public void tearDown(){
        token=null;
    }

    @Test
    public void testUserCanBeCreated() throws Exception {

        String user="{ \"username\" : \"username1\", \"password\" : \"password1\"}";

        given().
                body(user).
        with().
                contentType(ContentType.JSON).
        when().
                post("/rest/users/signup").
        then().
                statusCode(HttpStatus.SC_CREATED). // SC_OK dans PMC
                body("message", is("New user created"));
    }

    @Test
    public void testUsernameShouldNotBeEmpty() throws Exception {
        String emptyUsername="{ \"username\" : \"\", \"password\" : \"password\"}";

        testIfCredentialAreEmptyString(emptyUsername);
    }

    @Test
    public void testPasswordShouldNotBeEmpty() throws Exception {
        String emptyPassword="{ \"username\" : \"username11\", \"password\" : \"\"}";

        testIfCredentialAreEmptyString(emptyPassword);
    }

    @Test
    public void testUsernameAndShouldNotBeEmpty() throws Exception {
        String emptyUsernameAndPassword="{ \"username\" : \"\", \"password\" : \"\"}";

        testIfCredentialAreEmptyString(emptyUsernameAndPassword);
    }

    public void testIfCredentialAreEmptyString(String userCredentials){
        given().
                body(userCredentials).
        with().
                contentType(ContentType.JSON).
        when().
                post("/rest/users/signup").
        then().
                statusCode(HttpStatus.SC_BAD_REQUEST).
                body("message", is("Username or Password should not be empty"));
    }

    @Test
    public void testUsernameShouldBeUnique() throws Exception {
        String userData="{ \"username\" : \"username\", \"password\" : \"password\"}";

        given().
                body(userData).
        with().
                contentType(ContentType.JSON).
        when().
                post("/rest/users/signup").
        then().
                statusCode(HttpStatus.SC_CONFLICT).
                body("message", is("This username is already used"));

    }

    @Test
    public void testHowToAuthenticateAClient() throws Exception {
        String data= "password="+user.getPassword()+"&username="+user.getUsername()+
                     "&grant_type=password&scope=read write";

        String json= given().
                            log().all().
                            contentType("application/x-www-form-urlencoded").
                            body(data).
                            auth().preemptive().basic("pmcAndroid", "123456").
                    expect().
                            log().all().

                    when().
                            post("/oauth/token").asString();
        assertNotNull(new JsonPath(json).getString("access_token"));
    }

    @Test
    public void testGetCurrentUser() throws Exception {
        authenticate(user);
        given().
                header("Authorization", "Bearer "+token).
                contentType(ContentType.JSON).
        when().
                get("/rest/users/current").
        then().
                statusCode(HttpStatus.SC_OK);


    }
}
