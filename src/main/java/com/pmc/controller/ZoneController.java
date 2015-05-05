package com.pmc.controller;

import com.pmc.model.User;
import com.pmc.model.Zone;
import com.pmc.service.CustomUserDetailsService;
import com.pmc.service.ZoneService;
import com.pmc.service.ZoneServiceImpl;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by stephaneki on 04/03/15.
 *
 *
 * @author Stephane KI
 */
@RestController
@RequestMapping("rest/zones")
public class ZoneController {

    /**
     *
     */
    private static final int DEFAULT_RADIUS = 144;
    private static final int CONFIANCESCORE_ADDED_WHEN_ZONENOTALIKE=-5;
    private static final int CONFIANCESCORE_ADDED_WHEN_ZONEALIKE=1;
    /**
     *
     */
    private static final int MAX_RADIUS = 5000;

    @Resource
    private CustomUserDetailsService userService;
    private ZoneService zoneService;
    private ZoneServiceImpl zoneServiceImpl;

    /**
     * Find a zone by its id
     * @param id : id of the zone to find
     * @return The zone founded or null otherwise. Check response status for more details.
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
     public ResponseEntity<Zone> findZoneById(@PathVariable("id") int id) {
        try{
            Zone zone=zoneService.getById(id);
            HttpStatus status = HttpStatus.OK;
            if (zone==null){
                status=HttpStatus.NOT_FOUND;
            }
            return new ResponseEntity(zone, new HttpHeaders(), status);
        }catch (IllegalArgumentException e){
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);}
        catch (Exception e){
            System.out.println("allo");
            System.err.println(e.getMessage());
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     *
     * @param zone
     * @return
     */
    @RequestMapping(value = "/indicate", method = RequestMethod.POST)
    public ResponseEntity<Zone> indicateZone(@RequestBody Zone zone) {

        if(true||zoneServiceImpl.isZoneALike(zoneServiceImpl.getZoneAlike(zone.getLatitude(),zone.getLongitude()),zone.getDensity()))
        {
            try {
                User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                userService.addConfianceScore(user, CONFIANCESCORE_ADDED_WHEN_ZONEALIKE);
                if(true||user.getConfianceScore()>0) {
                    System.out.println("coucou");
                    return new ResponseEntity(zoneService.save(user, zone), new HttpHeaders(), HttpStatus.OK);
                }
                else{
                    return new ResponseEntity(null, new HttpHeaders(), HttpStatus.OK);
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
                return new ResponseEntity(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else{
            try {
                User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                userService.addConfianceScore(user, CONFIANCESCORE_ADDED_WHEN_ZONENOTALIKE);
                return new ResponseEntity(null, new HttpHeaders(), HttpStatus.OK);
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
                return new ResponseEntity(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    /**
     * @param latitude latitude of position specified.
     * @param longitude longitude of the position specified.
     * @param radius 
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<Zone>> listZonesByPosition(@RequestParam(value="latitude") double latitude,
                                                          @RequestParam(value="longitude") double longitude,
                                                          @RequestParam(value="radius", defaultValue="0") int radius){

        if(radius<1 || radius> MAX_RADIUS) radius = DEFAULT_RADIUS;

        try{System.out.println("hole");
            List<Zone> listZone = zoneService.getZones(latitude, longitude, radius);
            return new ResponseEntity(listZone, HttpStatus.OK);
        }catch (Exception e){
            System.err.println(e.getMessage());
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
