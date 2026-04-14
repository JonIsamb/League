package com.jisamb.league.service;

import com.jisamb.league.dto.request.CreateUserRequest;
import com.jisamb.league.dto.response.UserDTO;
import com.jisamb.league.entity.User;
import com.jisamb.league.mapper.CreateUserRequestMapper;
import com.jisamb.league.mapper.UserDTOMapper;
import com.jisamb.league.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;
    private final CreateUserRequestMapper createUserRequestMapper;

    public UserService(UserRepository userRepository, UserDTOMapper userDTOMapper, CreateUserRequestMapper createUserRequestMapper) {
        this.userRepository = userRepository;
        this.userDTOMapper = userDTOMapper;
        this.createUserRequestMapper = createUserRequestMapper;
    }

    public List<UserDTO> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(userDTOMapper)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Integer id) {
        return userRepository.findById(id)
                .map(userDTOMapper)
                .orElseThrow(() -> new IllegalArgumentException("User of id " + id + " not found."));
    }

    public UserDTO createUser(CreateUserRequest request) {
        User saved = userRepository.save(createUserRequestMapper.apply(request));

        return userDTOMapper.apply(saved);
    }
}
