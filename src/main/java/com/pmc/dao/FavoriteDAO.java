package com.pmc.dao;

import com.pmc.model.Favorite;
import org.springframework.data.repository.CrudRepository;

/**
 * Created on 09/03/15.
 *
 * @author stephaneki
 */
public interface FavoriteDAO extends CrudRepository<Favorite, Integer> {
}
