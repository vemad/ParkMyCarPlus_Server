package com.pmc.model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by stephaneki on 27/02/15.
 */
public class PlaceTest{

    private Place place;

    @Before
    public void setUp(){
        place= new Place();
        place.setLatitude(12.12);
        place.setLongitude(10.10);
    }

    @After
    public void after(){
        place=null;
    }

    @Test
    public void hasRequiredProperties(){
        Assert.assertEquals(place.getLatitude(), 12.12,0);
        Assert.assertEquals(place.getLongitude(),10.10,0);
    }
}
