package com.pmc.controller;

import com.pmc.model.User;
import com.pmc.service.CustomUserDetailsService;
import com.pmc.service.UserServiceException.UserNameAlreadyUsed;
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
            userService.SaveUser(user);
            return new ResponseEntity(new Message4Client("New user created"), new HttpHeaders(), HttpStatus.OK);
        }catch (UserNameAlreadyUsed e) {
            return new ResponseEntity(new Message4Client("This username is already used"),
                                      new HttpHeaders(), HttpStatus.CONFLICT);
        }catch (Exception e){
            System.err.println(e.getMessage());
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value ="/logout/success")
    public @ResponseBody ResponseEntity<Message4Client> logout_success() {
       
        return new ResponseEntity(new Message4Client("User have been successfully logged out"),
                                  new HttpHeaders(), HttpStatus.OK);
    }
}
