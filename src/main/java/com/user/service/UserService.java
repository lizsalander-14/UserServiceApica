package com.user.service;

import com.user.exception.MyCustomException;
import com.user.model.request.RegisterUserRequest;
import com.user.model.request.UpdateUserRequest;
import com.user.model.response.UserDetailsResponse;

public interface UserService {

    UserDetailsResponse registerUser(RegisterUserRequest registerUserRequest) throws MyCustomException;

    UserDetailsResponse getUserByEmail(String email);

    UserDetailsResponse updateUserByEmail(String email, UpdateUserRequest updateUserRequest) throws MyCustomException;

    void deleteUserByEmail(String email) throws MyCustomException;
}
