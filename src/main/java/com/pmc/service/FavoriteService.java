package com.pmc.service;

import com.pmc.model.Favorite;
import org.springframework.stereotype.Service;

/**
 * Created on 09/03/15.
 *
 * @author stephaneki
 */

public interface FavoriteService {
    public Favorite save(Favorite favorite);
}
