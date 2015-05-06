package com.pmc.service;

import com.pmc.model.Density;
import com.pmc.model.User;
import com.pmc.model.Zone;

import java.util.List;

/**
 * Created by stephaneki on 04/03/15.
 */
public interface ZoneService {
    public Zone getById(int id);
    public Zone save(User user, Zone zone);
    public List<Zone> getZones(double latitude, double longitude, int radius);
    public List<Zone> getAllZones();
    public List<Zone> getZoneAlike(double latitude, double longitude);
    public boolean isZoneALike (List<Zone> zonesalike, Density densit);
}
