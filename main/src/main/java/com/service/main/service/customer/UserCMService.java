package com.service.main.service.customer;


import com.service.main.dto.CustomResult;
import com.service.main.dto.UserInfoDto;
import com.service.main.entity.User;
import com.service.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserCMService {

    @Autowired
    private UserRepository userRepository;

    public UserInfoDto getUserInfoById(int userId){
        try{
            var user = userRepository.findById(userId);

            if(user.isEmpty()){
                return null;
            }

            var userInfoDto = new UserInfoDto();

            userInfoDto.setId(user.get().getId());
            userInfoDto.setFirstName(user.get().getFirstName());
            userInfoDto.setLastName(user.get().getLastName());
            userInfoDto.setAvatar(user.get().getAvatar());
            return userInfoDto;
        }catch (Exception e){
            return null;
        }
    }

    public CustomResult searchUserForGroupChat(int userId, String search){
        try{
            var users = userRepository.searchUserGroupChat(userId, search);

            List<UserInfoDto> userInfoDtos = new ArrayList<>();

            for (var user : users) {
                var userInfoDto = new UserInfoDto();
                userInfoDto.setId(user.getId());
                userInfoDto.setFirstName(user.getFirstName());
                userInfoDto.setLastName(user.getLastName());
                userInfoDto.setAvatar(user.getAvatar());
                userInfoDtos.add(userInfoDto);
            }

            return new CustomResult(200, "Success", userInfoDtos);

        }catch (Exception e){
            return new CustomResult(400, "Bad request", e.getMessage());
        }
    }

    public List<UserInfoDto> searchForUser(int userId, String search, List<Long> friendsId){
        try{

            var users = userRepository.searchChatUser(search, userId, friendsId);

            List<UserInfoDto> userInfoDtos = new ArrayList<>();

            for (var user : users) {
                var userInfoDto = new UserInfoDto();
                userInfoDto.setId(user.getId());
                userInfoDto.setFirstName(user.getFirstName());
                userInfoDto.setLastName(user.getLastName());
                userInfoDto.setAvatar(user.getAvatar());
                userInfoDtos.add(userInfoDto);
            }

            return userInfoDtos;
        }catch (Exception e){
            return null;
        }
    }
}
