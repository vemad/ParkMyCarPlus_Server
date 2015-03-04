package com.pmc.service;

import com.pmc.dao.ZoneDAO;
import com.pmc.model.Zone;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by stephaneki on 04/03/15.
 */
@Service(value = "zoneService")
public class ZoneServiceImpl implements ZoneService {

    @Resource
    private ZoneDAO zoneDAO;

    @Override
    public Zone getById(int id) {
        return zoneDAO.findOne(id);
    }

    @Override
    public Zone save(Zone zone) {
        return zoneDAO.save(zone);
    }

    @Override
    public List<Zone> getZones(double latitude, double longitude, int radius) {
        return zoneDAO.getZones(latitude,longitude,new DateTime(), 10);
    }
}
