package com.pmc.controller;

import com.Application;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.pmc.dao.PlaceDAO;
import com.pmc.model.Place;
import junit.framework.Assert;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Before;
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
import static org.junit.Assert.*;
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

    private Place place1;
    private Place place2;

    @Value("${local.server.port}")
    int port;

    @Before
    public void setUp() {
        place1=new Place().setLatitude(1.0).setLongitude(1.0);
        place2=new Place().setLatitude(2.2).setLongitude(2.2);

        //The database is cleared and re-initialized for each test so that we always
        // validate against a known state
        placeDAO.deleteAll();
        placeDAO.save(Arrays.asList(place1, place2));

        RestAssured.port = port;
    }

    @Test
    public void testCanFetchPlaceById() throws Exception {
        /*Response res = get("/rest/places/{id}", place1Id);

        assertEquals(HttpStatus.SC_OK, res.getStatusCode());
        String json = res.asString();
        JsonPath jp = new JsonPath(json);

        assertEquals(place1.getLatitude(),(float) jp.get("latitude"),0);*/

        when().
            get("/rest/places/{id}", place1.getId()).
        then().
            statusCode(HttpStatus.SC_OK).
            body("id", is(place1.getId())).
            body("latitude", is((float)place1.getLatitude())).
            body("longitude", is((float)place1.getLongitude())).
            body("isTaken", is(false));
    }
}
