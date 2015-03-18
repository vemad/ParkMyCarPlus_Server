package com.pmc.controller;

import com.pmc.model.Favorite;
import com.pmc.model.User;
import com.pmc.service.FavoriteService;
import com.pmc.service.FavoriteServiceException.FavoriteNotFound;
import com.util.Message4Client;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
            User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new ResponseEntity(favService.save(fav, user), new HttpHeaders(), HttpStatus.CREATED);
        }catch (Exception e){
            System.err.println(e.getMessage());
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<Favorite>> listFavorites(){
        try{
            User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new ResponseEntity(favService.getFavorites(user), HttpStatus.OK);
        }catch (Exception e){
            System.err.println(e.getMessage());
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public @ResponseBody
    ResponseEntity<Message4Client> deleteFavoriteById(@PathVariable("id") int id) {
        try {
            User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            favService.deleteFavoriteById(id, user);
            String message= "Favorite <"+id+"> deleted";
            return new ResponseEntity(new Message4Client(message), new HttpHeaders(), HttpStatus.OK);
        }catch (FavoriteNotFound eNotFound){
            return new ResponseEntity(new Message4Client(eNotFound.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }catch (IllegalArgumentException e){
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            System.err.println(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
