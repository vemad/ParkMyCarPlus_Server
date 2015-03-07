package com.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by stephaneki on 07/03/15.
 */
public class PositionTest {

    private Position position;

    @Before
    public void setUp(){
        position = new Position().setLatitude(12.12).setLongitude(10.10);
    }

    @After
    public void after(){
        position =null;
    }

    @Test
    public void hasRequiredProperties(){
        assertEquals(position.getLatitude(), 12.12, 0);
        assertEquals(position.getLongitude(), 10.10, 0);
    }
}
