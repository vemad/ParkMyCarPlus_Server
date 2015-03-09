package com.pmc.service;

import com.pmc.dao.FavoriteDAO;
import com.pmc.model.Density;
import com.pmc.model.Favorite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created on 09/03/15.
 *
 * @author stephaneki
 */
@Service("favoriteService")
public class FavoriteServiceImpl implements FavoriteService {
    @Autowired
    private FavoriteDAO favoriteDAO;

    @Override
    public Favorite save(Favorite favorite) {
        //TODO Next line need to be modified
        favorite.setDensity(Density.LOW).setIntensity(1);
        return favoriteDAO.save(favorite);
    }
}
