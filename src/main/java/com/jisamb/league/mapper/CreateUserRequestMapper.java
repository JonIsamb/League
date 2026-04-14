package com.jisamb.league.mapper;

import com.jisamb.league.dto.request.CreateUserRequest;
import com.jisamb.league.entity.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CreateUserRequestMapper implements Function<CreateUserRequest, User> {
    @Override
    public User apply(CreateUserRequest createUserRequest) {
        return new User(
                createUserRequest.name(),
                createUserRequest.email(),
                createUserRequest.password()
        );
    }
}
