package com.pmc.dao;

import com.pmc.model.Place;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Gaetan on 23/02/2015.
 */
@Repository("placeDAO")
public interface PlaceDAO extends CrudRepository<Place,Integer>, PlaceDAOCustom{
}
