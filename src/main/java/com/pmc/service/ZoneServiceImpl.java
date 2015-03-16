package com.pmc.service;

import com.pmc.dao.ZoneDAO;
import com.pmc.model.User;
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
    private static final int SCORE_ADDED_WHEN_ZONE = 10;

    @Resource
    private ZoneDAO zoneDAO;

    @Resource
    private CustomUserDetailsService userService;

    @Override
    public Zone getById(int id) {
        return zoneDAO.findOne(id);
    }

    @Override
    public Zone save(User user, Zone zone) {
        zone.setDate(new DateTime());
        userService.addScore(user, SCORE_ADDED_WHEN_ZONE);
        return zoneDAO.save(zone);
    }

    @Override
    public List<Zone> getZones(double latitude, double longitude, int radius) {

        //TODO: remove -60 cause by jetlag(timezone)
        DateTime oldestDate = new DateTime().plusMinutes(-TIMELAPS_MINUTE -60);
        return zoneDAO.findZonesByPosition(latitude, longitude, oldestDate, radius);
    }
}
