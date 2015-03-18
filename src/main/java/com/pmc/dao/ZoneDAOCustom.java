package com.pmc.dao;

import com.pmc.model.Zone;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by stephaneki on 04/03/15.
 */
public interface ZoneDAOCustom {
    public List<Zone> findZonesByPositionAfterDate(double latitude, double longitude, DateTime date, int radius);
    public List<Zone> findZonesByPositionBetweenDates(double latitude, double longitude, DateTime dateStart, DateTime dateStop, int radius);
    public List<Zone> findZonesOfHourAndDay(double latitude, double longitude, DateTime date, int radius);
}
