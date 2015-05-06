package com.pmc.service;

import com.pmc.dao.PlaceDAO;
import com.pmc.dao.ZoneDAO;
import com.pmc.model.Density;
import com.pmc.model.User;
import com.pmc.model.Zone;
import com.util.Position;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by stephaneki on 04/03/15.
 */
@Service(value = "zoneService")
public class ZoneServiceImpl implements ZoneService {

    /*Some Parameters*/
    private static final int TIMELAPS_MINUTE = 60;
    private static final int SCORE_ADDED_WHEN_ZONE = 10;
    private static final int ZONE_DEFAULT_RADIUS = 24;
    private static final int EARTH_RADIUS = 5000000;
    private static final float INTENSITY_LEVEL2 = 0.5f;
    private static final float INTENSITY_LEVEL3 = 0.2f;
    private static final int NB_DAY_BEFORE_LEVEL2 = 7;
    private static final int MIN_AROUND_TIME_LEVEL2 = 30;

    @Resource
    private ZoneDAO zoneDAO;

    @Resource
    private PlaceDAO placeDAO;

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

        //Zones Level 1:Zones of the last hour (intensity=1)
        DateTime oldestDate = new DateTime().plusMinutes(-TIMELAPS_MINUTE );
        List<Zone> listZoneLevel1 = zoneDAO.findZonesByPositionAfterDate(latitude, longitude, oldestDate, radius);
        for(Zone z:listZoneLevel1){
            Double occupationRate = placeDAO.getOccupationRate(z.getLatitude(), z.getLongitude(), ZONE_DEFAULT_RADIUS);
            Float intensity = calculateIntensityByOccupationRate(occupationRate, z.getDensity());
            z.setIntensity(intensity);
        }


        //Zones Level 2: Zones of the previous week the same day around a hour
        DateTime datePreviousWeekStart = new DateTime().plusMinutes(-MIN_AROUND_TIME_LEVEL2 ).plusDays(-NB_DAY_BEFORE_LEVEL2);
        DateTime datePreviousWeekStop = new DateTime().plusMinutes(+MIN_AROUND_TIME_LEVEL2).plusDays(-NB_DAY_BEFORE_LEVEL2);
        List<Zone> listZoneLevel2 = zoneDAO.findZonesByPositionBetweenDates(latitude, longitude, datePreviousWeekStart, datePreviousWeekStop, radius);
        for(Zone z:listZoneLevel2){
            z.setIntensity(INTENSITY_LEVEL2);
        }

        //Zones Level3: Zones avg on a grid
        List<Zone> listZoneLevel3 = new ArrayList<Zone>();
        List<Position> listPositions = generateGrid(latitude, longitude, radius, ZONE_DEFAULT_RADIUS);
        DateTime currentDate = new DateTime();
        for(Position position:listPositions){
            List<Zone> listZoneAroundPosition = zoneDAO.findZonesOfHourAndDay(position.getLatitude(), position.getLongitude(), currentDate, ZONE_DEFAULT_RADIUS);
            if(!listZoneAroundPosition.isEmpty()){
                Zone zone = new Zone().setLatitude(latitude).setLongitude(longitude).setIntensity(INTENSITY_LEVEL3).setDensity(calculateAvgDensity(listZoneAroundPosition));
                listZoneLevel3.add(zone);
            }
        }

        //Aggregate all list
        listZoneLevel1.addAll(listZoneLevel2);
        listZoneLevel1.addAll(listZoneLevel3);
        return listZoneLevel1;
    }

    private Float calculateIntensityByOccupationRate(Double occupationRate, Density zoneDensity){
        if(occupationRate == null) return 1f;

        Density densityOccupation;
        if(occupationRate <=1f/3){
            densityOccupation = Density.LOW;
        }
        else if(occupationRate <=2f/3){
            densityOccupation = Density.MEDIUM;
        }
        else{
            densityOccupation = Density.HIGH;
        }


        if(densityOccupation == zoneDensity){
            return 1f;
        }
        else if(densityOccupation == Density.MEDIUM || zoneDensity == Density.MEDIUM){
            return 0.8f;
        }
        else{
            return 0.6f;
        }
    }

    private Density calculateAvgDensity(List<Zone> listZoneAroundPosition){
        int sum=0;
        for(Zone z:listZoneAroundPosition){
            if(z.getDensity() == Density.LOW){
                sum += 0;
            }
            else if(z.getDensity() == Density.MEDIUM){
                sum += 1;
            }
            else if(z.getDensity() == Density.HIGH){
                sum += 2;
            }
        }
        double avg = sum/listZoneAroundPosition.size();
        if(avg<=2f/3){
            return Density.LOW;
        }
        else if(avg<=4f/3){
            return Density.MEDIUM;
        }
        else{
            return Density.HIGH;
        }
    }

    private List<Position> generateGrid(double latitude, double longitude, int size, double gap){
        List<Position> listPositions = new ArrayList<Position>();
        //Add center of the grid
        listPositions.add(new Position(latitude, longitude));

        int nbPointAround = Math.round(new Float((size/2)/gap));
        for(int i=1; i<=nbPointAround; i++){
            //Points on the left
            listPositions.add(getPosition(latitude, longitude, i*gap, Math.PI/2));
            //Points on the right
            listPositions.add(getPosition(latitude, longitude, i*gap, -Math.PI/2));
        }
        for(int i=0; i<nbPointAround*2+1; i++){
            for(int j=1; j<=nbPointAround; j++){
                //Points on the top
                listPositions.add(getPosition(listPositions.get(i).getLatitude(), listPositions.get(i).getLongitude(), i*gap, 0));
                //Points on the bottom
                listPositions.add(getPosition(listPositions.get(i).getLatitude(), listPositions.get(i).getLongitude(), i*gap, Math.PI));
            }
        }
        return listPositions;
    }

    private Position getPosition(double latitude, double longitude, double distance, double direction){
        double distance2 = distance/ EARTH_RADIUS;
        double latRad = latitude*Math.PI/180;
        double lonRad = longitude*Math.PI/180;
        double latRadRes= Math.asin(Math.sin(latRad)*Math.cos(distance2)+Math.cos(latRad)*Math.sin(distance2)*Math.cos(direction));
        double lonRadTmp = Math.atan2(Math.sin(direction)*Math.sin(distance2)*Math.cos(latRad), Math.cos(distance2) - Math.sin(latRad)*Math.sin(latRadRes));
        double lonRadRes = ((lonRad-lonRadTmp+Math.PI)%(2*Math.PI))-Math.PI;
        return new Position(latRadRes*180/Math.PI, lonRadRes*180/Math.PI);
    }
}
