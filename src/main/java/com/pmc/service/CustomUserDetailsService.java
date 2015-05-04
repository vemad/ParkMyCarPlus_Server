package com.pmc.service;


import com.pmc.dao.UserDao;
import com.pmc.model.Place;
import com.pmc.model.User;
import com.pmc.service.UserServiceException.UsernameAlreadyUsed;
import com.pmc.service.UserServiceException.UsernameOrPasswordEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private final UserDao userDao;

    @Autowired
    public CustomUserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User %s does not exist!", username));
        }
        return user;
    }


    public User SaveUser(User user) throws UsernameAlreadyUsed, UsernameOrPasswordEmpty, NullPointerException{
        checkNotNull(user);

        //username and password cannot be empty
        if(user.getPassword().isEmpty() || user.getUsername().isEmpty()){
            throw new UsernameOrPasswordEmpty();
        }

        // Username should be unique
        User user1=userDao.findByUsername(user.getUsername());
        if(user1 != null){
            throw new UsernameAlreadyUsed();
        }
        return userDao.save(user);
    }

    public void releasePlace(User user, int scoreAdded){
        user.releasePlace();
        user.addScore(scoreAdded);
        userDao.save(user);
    }

    public void takePlace(User user, Place place, int scoreAdded){
        user.takePlace(place);
        user.addScore(scoreAdded);
        userDao.save(user);
    }

    public void addScore(User user, int value){
        user.addScore(value);
        userDao.save(user);
    }

    public void addConfianceScore(User user, int value) {
        user.addConfianceScore(value);
        userDao.save(user);
    }
    public void changeMacAddress(User user, String macAddress){
        user.setMacAddress(macAddress);
        userDao.save(user);
    }
}
