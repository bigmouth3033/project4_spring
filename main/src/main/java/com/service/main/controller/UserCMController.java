package com.service.main.controller;

import com.service.main.dto.CustomResult;
import com.service.main.dto.UserInfoDto;
import com.service.main.service.customer.UserCMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("userCM")
public class UserCMController {

    @Autowired
    private UserCMService userCMService;

    @GetMapping("get_user_info_by_id/{id}")
    public UserInfoDto getUserInfo(@PathVariable int id){
        return userCMService.getUserInfoById(id);
    }

    @GetMapping("search_user_chat")
    public List<UserInfoDto> searchUserChat(@RequestParam(required = false, defaultValue = "") String search, @RequestParam int userId, @RequestParam List<Long> friendsId){
        return userCMService.searchForUser(userId, search,friendsId);
    }

    @GetMapping("search_user_group_chat")
    public ResponseEntity<CustomResult> searchUserGroupChat(@RequestParam(required = false, defaultValue = "") String search, @RequestParam int userId){
        var customResult = userCMService.searchUserForGroupChat(userId, search);
        return ResponseEntity.ok(customResult);
    }
}
