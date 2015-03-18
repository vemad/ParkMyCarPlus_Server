package com.pmc.controller;

import com.Application;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.pmc.dao.FavoriteDAO;
import com.pmc.dao.UserDao;
import com.pmc.model.Favorite;
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

import java.util.Arrays;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

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

    @Autowired
    private FavoriteDAO favoriteDAO;

    private User userWith2Favs;
    private User userWith1Favs;
    private Favorite fav1;
    private Favorite fav2;
    private Favorite fav3;
    private String token;

    @Before
    public void setUp() {

        userWith2Favs = new User().setUsername("username").setPassword("password");
        userDao.save(userWith2Favs);

        userWith1Favs= new User().setUsername("joe").setPassword("doe");
        userDao.save(userWith1Favs);

        fav1 = new Favorite(1.1, 1.1, "50 rue du village",userWith2Favs);
        fav2 = new Favorite(2.2, 2.2, "60 rue de la paix", userWith2Favs);
        fav3 = new Favorite(3.3, 3.3, "80 rue de la joie", userWith1Favs);

        favoriteDAO.save(Arrays.asList(fav1,fav2,fav3));
        RestAssured.port = port;
    }

    @After
    public void tearDown() throws Exception {
        token=null;
        favoriteDAO.deleteAll();
        userDao.deleteAll();
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

    @Test
    public void testFavCanBeCreated() throws Exception {
        authenticate(userWith2Favs);
        float latitude = 1.2f;
        float longitude = 2.3f;
        String address = "50 rue La patate douce";
        String fav = "{\"latitude\": "+ latitude +", \"longitude\": "+ longitude +"," +
                " \"address\":\""+ address +"\"}";
        given().
                header("Authorization", "Bearer "+token).
                contentType(ContentType.JSON).
                body(fav).
        when().
                log().all().
                post("/rest/favorites").

        then().
                log().all().
                statusCode(HttpStatus.SC_CREATED).
                body("id",notNullValue()).
                body("latitude", is(latitude)).
                body("longitude", is(longitude)).
                body("address", is(address)).
                body("density", notNullValue()).
                body("intensity", notNullValue());


    }

    @Test
    public void testFavoritesCanBeFetched() throws Exception {
        authenticate(userWith2Favs);

        given().
                header("Authorization", "Bearer "+token).
                contentType(ContentType.JSON).
        when().
                get("/rest/favorites").
        then().
                //log().all().
                statusCode(HttpStatus.SC_OK).
                body("id", hasItems(fav1.getId(), fav2.getId())).
                body("latitude",hasItems((float)fav1.getLatitude(), (float)fav2.getLatitude())).
                body("longitude",hasItems((float)fav1.getLongitude(), (float)fav2.getLongitude())).
                body("address", hasItems(fav1.getAddress(),fav2.getAddress())).
                body("intensity", hasItems(fav1.getIntensity(), fav2.getIntensity())).
                body("density", hasItems("LOW", "LOW")); //TODO Refactoring
    }

    @Test
    public void testFavCanBeDeleted() throws Exception {
        authenticate(userWith2Favs);

        given().
                header("Authorization", "Bearer "+token).
                contentType(ContentType.JSON).
        when().
                delete("/rest/favorites/{id}", fav1.getId()).
        then().
                statusCode(HttpStatus.SC_OK);

    }

    @Test
    public void testIfFavoriteDoesNotExist() throws Exception {
        authenticate(userWith1Favs);

        given().
                header("Authorization", "Bearer "+token).
                contentType(ContentType.JSON).
        when().
                delete("/rest/favorites/{id}", 100).
        then().
                statusCode(HttpStatus.SC_NOT_FOUND);

    }
}
