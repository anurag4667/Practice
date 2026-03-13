package org.pm.smarty.controller;


import org.pm.smarty.Dto.UserRequestDto;
import org.pm.smarty.Dto.UserResponseDto;
import org.pm.smarty.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class  UserController{


    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getUsers(){

        List<UserResponseDto> userResponseDtos = userService.getAllUsers();

        return  ResponseEntity.ok().body(userResponseDtos);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto userRequestDto) throws Exception {

        UserResponseDto userResponseDto = userService.createUser(userRequestDto);

        return ResponseEntity.ok().body(userResponseDto);

    }

}
