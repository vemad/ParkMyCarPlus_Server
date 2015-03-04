package com.pmc.controller;

import com.pmc.model.User;
import com.pmc.service.CustomUserDetailsService;
import com.util.Message4Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by stephaneki on 02/03/15.
 */
@RestController
@RequestMapping("rest/users")
public class UserController {

    @Autowired
    private CustomUserDetailsService userService;

    @RequestMapping(value ="/signup", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Message4Client> signup(@RequestBody User user) {

        try{
            User newuser = userService.SaveUser(user);
            return new ResponseEntity(new Message4Client("New user created"), new HttpHeaders(), HttpStatus.OK);
        }catch (Exception e){
            System.err.println(e.getMessage());
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}
