package org.pm.smarty.service;


import org.pm.smarty.Dto.UserRequestDto;
import org.pm.smarty.Dto.UserResponseDto;
import org.pm.smarty.entity.User;
import org.pm.smarty.mapper.UserMapper;
import org.pm.smarty.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    public List<UserResponseDto> getAllUsers(){
        List<User> users = userRepository.findAll();

        return users.stream().map(UserMapper::toDto).toList();
    }

    public UserResponseDto createUser(UserRequestDto userRequestDto) throws RuntimeException {

        if(userRepository.existsByEmail(userRequestDto.getEmail())){
            throw new RuntimeException("email already exist");
        }

        User user = userRepository.save(UserMapper.toModel(userRequestDto));

        return UserMapper.toDto(user);
    }
}
