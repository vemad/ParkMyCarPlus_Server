package com.pmc.controller;

import com.pmc.model.Favorite;
import com.pmc.service.FavoriteService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created on 09/03/15.
 *
 * @author stephaneki
 */

@RestController
@RequestMapping("rest/favorites")
public class FavoriteController {

    @Resource
    private FavoriteService favService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Favorite> createFavorite(@RequestBody Favorite fav) {

        try{
            return new ResponseEntity(favService.save(fav), new HttpHeaders(), HttpStatus.CREATED);
        }catch (Exception e){
            System.err.println(e.getMessage());
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
