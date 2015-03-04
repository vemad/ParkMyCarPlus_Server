package com.pmc.controller;

import com.pmc.model.Zone;
import com.pmc.service.ZoneService;
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
     public Zone getZoneById(@PathVariable("id") int id) {
        return zoneService.getById(id);
    }

    @RequestMapping(value = "/indicate", method = RequestMethod.POST)
    public Zone getZoneById(@RequestBody Zone zone) {
        return zoneService.save(zone);
    }

    @RequestMapping("")
    public List<Zone> listZonesByPosition(@RequestParam(value="latitude") double latitude,
                                            @RequestParam(value="longitude") double longitude,
                                            @RequestParam(value="radius", defaultValue="0") int radius){

        if(radius<1 || radius>maxRadius) radius = defaultRadius;

        return zoneService.getZones(latitude, longitude, radius);
    }



}
