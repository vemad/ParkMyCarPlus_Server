package com.pmc.controller;

import com.Application;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
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

    @Before
    public void setUp() {

        user = new User().setUsername("username").setPassword("password");

        //The database is cleared and re-initialized for each test so that we always
        // validate against a known state
        userDao.deleteAll();
        userDao.save(user);

        RestAssured.port = port;
    }

    @Test
    public void testUserCanBeCreated() throws Exception {

        String user="{ \"username\" : \"username1\", \"password\" : \"password\"}";

        given().
                body(user).
        with().
                contentType(ContentType.JSON).
        when().
                post("/rest/users/signup").
        then().
                statusCode(HttpStatus.SC_OK);

    }
}
