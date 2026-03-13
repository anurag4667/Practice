package org.pm.smarty.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pm.smarty.Dto.UserRequestDto;
import org.pm.smarty.Dto.UserResponseDto;
import org.pm.smarty.entity.User;
import org.pm.smarty.repository.UserRepository;

import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;


    @Test
    void createUserTest() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setEmail("hello@gmail.com");
        userRequestDto.setName("hello");
        userRequestDto.setPassword("password");

        UUID userId = UUID.randomUUID();
        User savedUser = new User();
        savedUser.setUserid(userId);
        savedUser.setEmail(userRequestDto.getEmail());
        savedUser.setName(userRequestDto.getName());
        savedUser.setPassword(userRequestDto.getPassword());

        Mockito.when(userRepository.existsByEmail(userRequestDto.getEmail())).thenReturn(false);
        Mockito.when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponseDto userResponseDto = userService.createUser(userRequestDto);

        Assertions.assertEquals(savedUser.getEmail(), userResponseDto.getEmail());
        Assertions.assertEquals(savedUser.getName(), userResponseDto.getName());
        Assertions.assertEquals(savedUser.getUserid().toString(), userResponseDto.getUserid());

    }
}
