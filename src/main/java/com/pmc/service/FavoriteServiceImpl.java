package com.pmc.service;

import com.pmc.dao.FavoriteDAO;
import com.pmc.dao.UserDao;
import com.pmc.model.Density;
import com.pmc.model.Favorite;
import com.pmc.model.User;
import com.pmc.service.UserServiceException.UsernameAlreadyUsed;
import com.pmc.service.UserServiceException.UsernameOrPasswordEmpty;
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
    public Favorite save(Favorite favorite, User user) throws UsernameOrPasswordEmpty, UsernameAlreadyUsed {
        user.addFavorite(favorite);
        User u = userDao.save(user);

        int size = u.getFavorites().size();
        Favorite favAdded =u.getFavorites().get(size-1).setIntensity(DEFAULT_INTENSITY).setDensity(DEFAULT_DENSITY);
        try {
            deleteFavoriteById(0, user);
            user.addFavorite(favAdded);
        } catch (FavoriteNotFound favoriteNotFound) {
            favoriteNotFound.printStackTrace();
        }

        return favAdded;

        /*System.err.println(u.toString());
        Authentication authentication = new UsernamePasswordAuthenticationToken(u, u.getPassword(), u.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.err.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());*/


    }

    @Override
    public List<Favorite> getFavorites(User user) {
        //TODO Refactoring if possible - lazy init collection

        List<Favorite> favorites = user.getFavorites();
        for(Favorite fav:favorites){
            fav.setDensity(DEFAULT_DENSITY).setIntensity(DEFAULT_INTENSITY);
        }
        return favorites;
    }

    @Override
    public void deleteFavoriteById(int id, User user) throws FavoriteNotFound{
        for(Favorite fav: user.getFavorites()){
            if(fav.getId()==id){
                user.removeFavorite(fav);
                favoriteDAO.delete(fav); // NASTY !!!
                userDao.save(user);
                return;
            }
        }
        throw new FavoriteNotFound();
    }
}
