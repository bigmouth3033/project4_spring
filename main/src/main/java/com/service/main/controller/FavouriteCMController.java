package com.service.main.controller;

import com.service.main.dto.CustomResult;
import com.service.main.dto.FavouriteDto;
import com.service.main.dto.FavouriteIDDto;
import com.service.main.service.customer.FavouriteCMService;
import feign.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("favouriteCM")
public class FavouriteCMController {
    @Autowired
    public FavouriteCMService favouriteCMService;

    @GetMapping("getFavourites")
    public ResponseEntity<CustomResult> getAllFavouritesByUserId(@Param("userId") Integer userId) {
        var customResult = favouriteCMService.getAllFavouritesByUserId(userId);
        return ResponseEntity.ok(customResult);
    }

    @PostMapping("deleteFavourites")
    public ResponseEntity<CustomResult> deleteFavourites(@ModelAttribute FavouriteIDDto favouriteIDDto){
        var customResult = favouriteCMService.deleteFavourite(favouriteIDDto);
        return ResponseEntity.ok(customResult);
    }

    @PostMapping("createFavourite")
    public ResponseEntity<CustomResult> createFavourite(@ModelAttribute FavouriteDto favouriteDto)
    {
            var customResult = favouriteCMService.createFavourite(favouriteDto);
            return ResponseEntity.ok(customResult);
    }
}
