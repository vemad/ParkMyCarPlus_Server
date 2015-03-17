package com.pmc.service;

import com.pmc.dao.ZoneDAO;
import com.pmc.model.Density;
import com.pmc.model.User;
import com.pmc.model.Zone;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;

/**
 * Created by stephaneki on 04/03/15.
 */
@Service(value = "zoneService")
public class ZoneServiceImpl implements ZoneService {

    /*Some Parameters*/
    private static final int TIMELAPS_MINUTE = 60;
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



        //Zones Level 1:Zones of the last hour (intensity=1)
        DateTime oldestDate = new DateTime().plusMinutes(-TIMELAPS_MINUTE -60); //TODO: remove -60 cause by jetlag(timezone)
        List<Zone> listZoneLevel1 = zoneDAO.findZonesByPositionAfterDate(latitude, longitude, oldestDate, radius);
        for(Zone z:listZoneLevel1){
            z.setIntensity(1);
        }

        //Zones Level 2: Zones of the previous week the same day around a hour
        DateTime datePreviousWeekStart = new DateTime().plusMinutes(-30-60).plusDays(-7);//-30 min before
        DateTime datePreviousWeekStop = new DateTime().plusMinutes(+30-60).plusDays(-7);//+30 min after
        List<Zone> listZoneLevel2 = zoneDAO.findZonesByPositionBetweenDates(latitude, longitude, datePreviousWeekStart, datePreviousWeekStop, radius);
        for(Zone z:listZoneLevel2){
            z.setIntensity(0.5f);
        }

        //Zones Level3: Zones avg on a grid
        List<Zone> listZoneLevel3 = new ArrayList<Zone>();
        List<Pair<Double, Double>> listPositions = generateGrid(latitude, longitude, radius, 24);
        DateTime currentDate = new DateTime().plusMinutes(-60);
        for(Pair<Double, Double> position:listPositions){
            List<Zone> listZoneAroundPosition = zoneDAO.findZonesOfHourAndDay(position.getKey(), position.getValue(), currentDate, 24);
            if(!listZoneAroundPosition.isEmpty()){
                Zone zone = new Zone().setLatitude(latitude).setLongitude(longitude).setIntensity(0.2f).setDensity(calculateAvgDensity(listZoneAroundPosition));
                listZoneLevel3.add(zone);
            }
        }

        //Aggregate all list
        listZoneLevel1.addAll(listZoneLevel2);
        listZoneLevel1.addAll(listZoneLevel3);
        return listZoneLevel1;
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
        if(avg<=2/3){
            return Density.LOW;
        }
        else if(avg<=4/3){
            return Density.MEDIUM;
        }
        else{
            return Density.HIGH;
        }
    }

    private List<Pair<Double, Double>> generateGrid(double latitude, double longitude, int size, double gap){
        List<Pair<Double, Double>> listPositions = new ArrayList<Pair<Double, Double>>();
        //Add center of the grid
        listPositions.add(new Pair<Double, Double>(latitude, longitude));

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
                listPositions.add(getPosition(listPositions.get(i).getKey(), listPositions.get(i).getValue(), i*gap, 0));
                //Points on the bottom
                listPositions.add(getPosition(listPositions.get(i).getKey(), listPositions.get(i).getValue(), i*gap, Math.PI));
            }
        }
        return listPositions;
    }

    private Pair<Double, Double> getPosition(double latitude, double longitude, double distance, double direction){
        double distance2 = distance/5000000;
        double latRad = latitude*Math.PI/180;
        double lonRad = longitude*Math.PI/180;
        double latRadRes= Math.asin(Math.sin(latRad)*Math.cos(distance2)+Math.cos(latRad)*Math.sin(distance2)*Math.cos(direction));
        double lonRadTmp = Math.atan2(Math.sin(direction)*Math.sin(distance2)*Math.cos(latRad), Math.cos(distance2) - Math.sin(latRad)*Math.sin(latRadRes));
        double lonRadRes = ((lonRad-lonRadTmp+Math.PI)%(2*Math.PI))-Math.PI;
        return new Pair<Double, Double>(latRadRes*180/Math.PI, lonRadRes*180/Math.PI);
    }
}
