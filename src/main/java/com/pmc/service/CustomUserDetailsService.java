package com.pmc.service;

import com.pmc.dao.UserDao;
import com.pmc.model.User;
import com.pmc.service.UserServiceException.UserNameAlreadyUsed;
import com.pmc.service.UserServiceException.UsernameOrPasswordEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
        return new User(user);
    }

    public User SaveUser(User user) throws UserNameAlreadyUsed, UsernameOrPasswordEmpty{

        // Username should be unique
        User user1=userDao.findByUsername(user.getUsername());
        if(user1!=null){
            throw new UserNameAlreadyUsed();
        }

        //username and password cannot be empty
        if(user.getPassword().isEmpty() || user.getUsername().isEmpty()){
            throw new UsernameOrPasswordEmpty();
        }
        return userDao.save(user);

    }

}
