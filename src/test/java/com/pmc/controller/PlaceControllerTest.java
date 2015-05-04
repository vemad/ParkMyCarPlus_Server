package com.pmc.controller;

import com.Application;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.pmc.dao.PlaceDAO;
import com.pmc.dao.UserDao;
import com.pmc.model.Place;
import com.pmc.model.User;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Ignore;
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
import static org.hamcrest.Matchers.is;

/**
 * Created by stephaneki on 25/02/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class PlaceControllerTest {

    @Autowired
    private PlaceDAO placeDAO;

    @Autowired
    private UserDao userDao;

    private Place freePlace;
    private Place takenPlace;

    @Value("${local.server.port}")
    int port;

    private User user;
    private String token;

    @Before
    public void setUp() {
        freePlace =new Place().setLatitude(1.0).setLongitude(1.0);
        takenPlace =new Place().setLatitude(2.2).setLongitude(2.2).takePlace();
        user = new User().setUsername("username").setPassword("password");

        //The database is cleared and re-initialized for each test so that we always
        // validate against a known state
        placeDAO.deleteAll();
        placeDAO.save(Arrays.asList(freePlace, takenPlace));

        userDao.deleteAll();
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
    public void canFetchAPlaceByItsId() throws Exception {

        authenticate();

        given().
                header("Authorization", "Bearer "+token).
        when().
                get("/rest/places/{id}", freePlace.getId()).
        then().
                statusCode(HttpStatus.SC_OK).
                body("id", is(freePlace.getId())).
                body("latitude", is((float) freePlace.getLatitude())).
                body("longitude", is((float) freePlace.getLongitude())).
                body("isTaken", is(false));
    }

    @Test
    public void notFoundStatusSentIfPlaceDoesNotExist() throws Exception {
        int dummyId= freePlace.getId()+ takenPlace.getId()+1;
        authenticate();

        given().
                header("Authorization", "Bearer "+token).
        when().
                get("/rest/places/{id}", dummyId).
        then().
                statusCode(HttpStatus.SC_NOT_FOUND);

    }

    @Test
    public void badRequestStatusSentIfIdIsNotANumber() throws Exception {
        authenticate();

        given().
                header("Authorization", "Bearer "+token).
        when().
                get("/rest/places/{id}", "dummyId").
        then().
                statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void whenFetchingAPlace_RequestMethodShouldBeGet() throws Exception {
        authenticate();

        given().
                header("Authorization", "Bearer "+token).
        when().
                post("/rest/places/{id}", freePlace.getId()).
        then().
                statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);

    }

    /*@Test
    @Ignore //Sub queries issues in hsqlbd (need some readings to fix it)
    public void conflictStatusSentIfPlaceAlreadyReleased() throws Exception {
        String pos="{ \"latitude\" : "+freePlace.getLatitude()+"," +
                " \"longitude\" :" +freePlace.getLongitude()+"}";

        authenticate();

        given().
                header("Authorization", "Bearer " + token).
                body(pos).
        with().
                contentType(ContentType.JSON).
        when().
                post("/rest/places/released").
        then().
                statusCode(HttpStatus.SC_CONFLICT);
    }

    @Test
    @Ignore //Sub queries issues in hsqlbd (need some readings to fix it)
    public void conflictStatusIfPlaceAlreadyTaken() throws Exception {
        String pos="{ \"latitude\" : "+takenPlace.getLatitude()
                +", \"longitude\" : "+takenPlace.getLongitude()+"}";
        authenticate();

        given().
                header("Authorization", "Bearer " + token).
                body(pos).
        with().
                contentType(ContentType.JSON).
        when().
                post("/rest/places/taken").
        then().
                statusCode(HttpStatus.SC_CONFLICT);
    }*/

    @Test
    public void canDeleteAPlaceByItsId() throws Exception {
        int id = freePlace.getId();
        authenticate();

        given().
                header("Authorization", "Bearer "+token).
        when().
                delete("/rest/places/{id}", id).
        then().
                statusCode(HttpStatus.SC_OK);

        given().
                header("Authorization", "Bearer "+token).
        when().
                get("/rest/places/{id}", id).
        then().
                statusCode(HttpStatus.SC_NOT_FOUND);
    }


}
