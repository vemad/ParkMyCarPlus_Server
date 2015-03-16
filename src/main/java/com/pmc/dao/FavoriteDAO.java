package com.pmc.dao;

import com.pmc.model.Favorite;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created on 09/03/15.
 *
 * @author stephaneki
 */
public interface FavoriteDAO extends CrudRepository<Favorite, Integer> {

    @Query(value = "SELECT * FROM favorite WHERE user_id = ?1", nativeQuery = true)
    List<Favorite> findUserFavorites(int id);

    @Query(value = "SELECT * FROM favorite WHERE id=?1 AND user_id =?2", nativeQuery = true)
    Favorite findFavoriteToDelete(int favId, int userId);
}
