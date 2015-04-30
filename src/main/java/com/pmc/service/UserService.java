package com.pmc.service;

import com.pmc.dao.UserDao;
import com.pmc.model.User;

import javax.annotation.Resource;

/**
 * Created by Romain on 29/04/2015.
 */
public class UserService {

    @Resource
    private UserDao userDAO;

    @Resource
    private CustomUserDetailsService userService;

    public User save(User user, String macAddress) {
        userService.changeMacAddress(user, macAddress);
        return userDAO.save(user);
    }
}