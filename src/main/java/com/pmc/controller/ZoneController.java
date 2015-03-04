package com.pmc.controller;

import com.pmc.model.Zone;
import com.pmc.service.ZoneService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by stephaneki on 04/03/15.
 */
@RestController
@RequestMapping("rest/zones")
public class ZoneController {

    private static final int defaultRadius = 500;
    private static final int maxRadius = 5000;
    @Resource
    private ZoneService zoneService;

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
     public ResponseEntity<Zone> getZoneById(@PathVariable("id") int id) {
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
            System.err.println(e.getMessage());
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/indicate", method = RequestMethod.POST)
    public ResponseEntity<Zone> indicateZone(@RequestBody Zone zone) {
        try{
            return new ResponseEntity(zoneService.save(zone), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e){
            System.err.println(e.getMessage());
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<Zone>> listZonesByPosition(@RequestParam(value="latitude") double latitude,
                                            @RequestParam(value="longitude") double longitude,
                                            @RequestParam(value="radius", defaultValue="0") int radius){

        if(radius<1 || radius>maxRadius) radius = defaultRadius;

        try{
            return new ResponseEntity(zoneService.getZones(latitude, longitude, radius), HttpStatus.OK);
        }catch (Exception e){
            System.err.println(e.getMessage());
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



}
