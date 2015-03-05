package com.pmc.controller;

import com.Application;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.pmc.dao.PlaceDAO;
import com.pmc.model.Place;
import com.util.Position;
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

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;

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

    private Place freePlace;
    private Place takenPlace;

    @Value("${local.server.port}")
    int port;

    @Before
    public void setUp() {
        freePlace =new Place().setLatitude(1.0).setLongitude(1.0);
        takenPlace =new Place().setLatitude(2.2).setLongitude(2.2).takePlace();

        //The database is cleared and re-initialized for each test so that we always
        // validate against a known state
        placeDAO.deleteAll();
        placeDAO.save(Arrays.asList(freePlace, takenPlace));

        RestAssured.port = port;
    }

    @Test
    public void canFetchAPlaceByItsId() throws Exception {
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
        when().
                get("/rest/places/{id}", dummyId).
        then().
                statusCode(HttpStatus.SC_NOT_FOUND);

    }

    @Test
    public void badRequestStatusSentIfIdIsNotANumber() throws Exception {
        when().
                get("/rest/places/{id}", "dummyId").
        then().
                statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void whenFetchingAPlaceRequestMethodShouldBeGet() throws Exception {
        when().
                post("/rest/places/{id}", freePlace.getId()).
        then().
                statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);

    }

    @Test
    @Ignore
    public void conflictStatusSentIfPlaceAlreadyReleased() throws Exception {
        Position p = new Position().setLatitude(freePlace.getLatitude()).
                                   setLongitude(freePlace.getLongitude());
        String pos="{ \"latitude\" : 1.0, \"longitude\" : 1.0}";

        given().
                body(pos).
        with().
                contentType(ContentType.JSON).
        when().
                post("/rest/places/released").
        then().
                statusCode(HttpStatus.SC_CONFLICT);
    }

    @Test
    @Ignore
    public void canDeleteAPlaceByItsId() throws Exception {
        int id= freePlace.getId();
        when().
                delete("/rest/places/delete/{id}", id).
        then().
                statusCode(HttpStatus.SC_OK);
        when().
                get("/rest/places/{id}", id).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);
    }


}
