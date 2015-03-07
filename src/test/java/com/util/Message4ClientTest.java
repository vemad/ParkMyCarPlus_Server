package com.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by stephaneki on 07/03/15.
 */
public class Message4ClientTest {

    private Message4Client message;
    @Before
    public void setUp(){
        message= new Message4Client("My message");
    }

    @After
    public void after(){
        message=null;
    }

    @Test
    public void hasRequiredProperties(){
        assertEquals(message.getMessage(), "My message");
    }

}
