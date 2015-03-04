package com.pmc.dao;

import com.pmc.model.Zone;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by stephaneki on 04/03/15.
 */
public interface ZoneDAO extends CrudRepository<Zone, Integer>, ZoneDAOCustom {
}
