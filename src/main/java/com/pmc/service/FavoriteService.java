package com.pmc.service;

import com.pmc.model.Favorite;
import com.pmc.model.User;
import com.pmc.service.UserServiceException.UsernameAlreadyUsed;
import com.pmc.service.UserServiceException.UsernameOrPasswordEmpty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created on 09/03/15.
 *
 * @author stephaneki
 */

public interface FavoriteService {
    public Favorite save(Favorite favorite, User user) throws UsernameOrPasswordEmpty, UsernameAlreadyUsed;
    public List<Favorite> getFavorites(User user);
    public void deleteFavoriteById(int id, User user)throws FavoriteNotFound;
}
