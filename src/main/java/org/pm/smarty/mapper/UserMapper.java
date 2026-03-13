package org.pm.smarty.mapper;

import org.pm.smarty.Dto.UserRequestDto;
import org.pm.smarty.Dto.UserResponseDto;
import org.pm.smarty.entity.User;

public class UserMapper {

    public static UserResponseDto toDto(User user){
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUserid(user.getUserid().toString());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setName(user.getName());

        return userResponseDto;
    }

    public static User toModel(UserRequestDto userRequestDto){
        User user = new User();
        user.setEmail(userRequestDto.getEmail());
        user.setName(userRequestDto.getName());
        user.setPassword(userRequestDto.getPassword());

        return user;
    }
}
