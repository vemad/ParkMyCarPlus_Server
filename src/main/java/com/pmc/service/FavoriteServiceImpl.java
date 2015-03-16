package com.pmc.service;

import com.pmc.dao.FavoriteDAO;
import com.pmc.dao.UserDao;
import com.pmc.model.Density;
import com.pmc.model.Favorite;
import com.pmc.model.User;
import com.pmc.service.FavoriteServiceException.FavoriteNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created on 09/03/15.
 *
 * @author stephaneki
 */
@Service("favoriteService")
public class FavoriteServiceImpl implements FavoriteService {
    private static final float DEFAULT_INTENSITY = 1;
    private static final Density DEFAULT_DENSITY = Density.LOW;

    @Autowired
    private FavoriteDAO favoriteDAO;

    @Autowired
    private UserDao userDao;


    @Override
    public Favorite save(Favorite favorite, User user){
        favorite.setUser(user);
        favoriteDAO.save(favorite);
        favorite.setIntensity(DEFAULT_INTENSITY).setDensity(DEFAULT_DENSITY);
        return favorite;
    }

    @Override
    public List<Favorite> getFavorites(User user) {

        List<Favorite> favorites=favoriteDAO.findUserFavorites(user.getId());
        for(Favorite fav:favorites){
            fav.setDensity(DEFAULT_DENSITY).setIntensity(DEFAULT_INTENSITY);
        }
        return favorites;
    }

    @Override
    public void deleteFavoriteById(int id, User user) throws FavoriteNotFound{
        Favorite favorite = favoriteDAO.findFavoriteToDelete(id, user.getId());

        if(favorite==null){
            throw new FavoriteNotFound();
        }else{
            favoriteDAO.delete(favorite);
        }
    }
}
