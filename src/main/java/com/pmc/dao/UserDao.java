package com.pmc.dao;

import com.pmc.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by stephaneki on 02/03/15.
 */
public interface UserDao extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
