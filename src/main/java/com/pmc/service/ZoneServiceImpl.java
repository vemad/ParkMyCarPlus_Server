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

    /*Some Parameters*/
    private static final int TIMELAPS_MINUTE = 180;

    @Resource
    private ZoneDAO zoneDAO;

    @Override
    public Zone getById(int id) {
        return zoneDAO.findOne(id);
    }

    @Override
    public Zone save(Zone zone) {
        zone.setDate(new DateTime());
        return zoneDAO.save(zone);
    }

    @Override
    public List<Zone> getZones(double latitude, double longitude, int radius) {

        //TODO: remove -60 cause by jetlag(timezone)
        DateTime oldestDate = new DateTime().plusMinutes(-TIMELAPS_MINUTE -60);
        return zoneDAO.findZonesByPosition(latitude, longitude, oldestDate, radius);
    }
}
