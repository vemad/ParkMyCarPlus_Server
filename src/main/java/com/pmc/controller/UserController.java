package com.pmc.controller;

import com.pmc.model.User;
import com.pmc.service.CustomUserDetailsService;
import com.pmc.service.UserServiceException.UsernameAlreadyUsed;
import com.pmc.service.UserServiceException.UsernameOrPasswordEmpty;
import com.util.Message4Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public @ResponseBody ResponseEntity<Message4Client> signUp(@RequestBody User user) {
        try{

            userService.SaveUser(user);
            return new ResponseEntity(new Message4Client("New user created"), new HttpHeaders(), HttpStatus.OK);
        }catch (UsernameAlreadyUsed e) {
            return new ResponseEntity(new Message4Client("This username is already used"),
                                      new HttpHeaders(), HttpStatus.CONFLICT);
        }catch (UsernameOrPasswordEmpty e){
            return new ResponseEntity(new Message4Client("Username or Password should not be empty"),
                    new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value ="/current", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<User> getUser(){
        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity(user, new HttpHeaders(), HttpStatus.OK);
    }
}
