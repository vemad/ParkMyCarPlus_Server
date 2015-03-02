package com.pmc.controller;

import com.pmc.model.User;
import com.pmc.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by stephaneki on 02/03/15.
 */
@RestController
public class UserController {

    @Autowired
    private CustomUserDetailsService userService;

    @RequestMapping(value ="/signup", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<User> signup(@RequestBody User user) {

        //TODO Do not return the user entity but only return a message
        User newuser = userService.SaveUser(user);
        return new ResponseEntity(newuser, new HttpHeaders(), HttpStatus.OK);
    }
}
