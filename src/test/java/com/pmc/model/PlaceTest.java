package com.pmc.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author stephaneki
 */
public class PlaceTest{

    private Place place;

    @Before
    public void setUp(){
        place= new Place().setLatitude(12.12).setLongitude(10.10);
    }

    @After
    public void after(){
        place=null;
    }

    @Test
    public void hasRequiredProperties(){
        assertEquals(place.getLatitude(), 12.12, 0);
        assertEquals(place.getLongitude(), 10.10, 0);
    }
}
