package com.pmc.controller;

import com.Application;
import com.jayway.restassured.RestAssured;
import com.pmc.dao.ZoneDAO;
import com.pmc.model.Zone;
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

import java.util.Arrays;

import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;

/**
 * Created by stephaneki on 06/03/15.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class ZoneControllerTest {

    @Autowired
    private ZoneDAO zoneDao;
    @Value("${local.server.port}")
    int port;

    private Zone highDensityZone;

    @Before
    public void setUp() {
        highDensityZone =new Zone().setLatitude(1.1).setLongitude(1.1).setDensity(Zone.Density.HIGH);

        //The database is cleared and re-initialized for each test so that we always
        // validate against a known state
        zoneDao.deleteAll();
        zoneDao.save(Arrays.asList(highDensityZone));

        RestAssured.port = port;
    }

    @Test
    public void canFetchAZoneByItsId() throws Exception {
        when().
                get("/rest/zones/{id}", highDensityZone.getId()).
        then().
                statusCode(HttpStatus.SC_OK).
                body("id", is(highDensityZone.getId())).
                body("latitude", is((float)highDensityZone.getLatitude())).
                body("longitude", is((float)highDensityZone.getLongitude())).
                body("density", is(Zone.Density.HIGH.toString()));
    }

    @Test
    public void notFoundStatusSentIfZoneDoesNotExist() throws Exception {
        int dummyId= highDensityZone.getId() + 1;
        when().
                get("/rest/zones/{id}", dummyId).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);

    }

    @Test
    public void badRequestStatusSentIfIdIsNotANumber() throws Exception {
        when().
                get("/rest/zones/{id}", "dummyId").
                then().
                statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}
