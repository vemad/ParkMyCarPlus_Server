package com.pmc.service.UserServiceException;

/**
 * Created by stephaneki on 05/03/15.
 */
public class UsernameAlreadyUsed extends Exception{

    public UsernameAlreadyUsed() {
        super("Username already used");
    }
}
