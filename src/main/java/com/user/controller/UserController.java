package com.user.controller;

import com.user.exception.MyCustomException;
import com.user.model.request.RegisterUserRequest;
import com.user.model.response.UserDetailsResponse;
import com.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDetailsResponse> registerUser(@RequestBody RegisterUserRequest registerUserRequest)
            throws MyCustomException {
        log.info("Going to register user for email: {}", registerUserRequest.getEmail());
        return new ResponseEntity<>(userService.registerUser(registerUserRequest), HttpStatus.OK);
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserDetailsResponse> getUserByEmail(@PathVariable("email")String email){
        log.info("Going to fetch user for email: {}", email);
        return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
    }
}
