package com.jisamb.league.dto.request;

public record CreateUserRequest(
        String name,
        String password,
        String email
) {}
