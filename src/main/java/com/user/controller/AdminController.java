package com.user.controller;

import com.user.exception.MyCustomException;
import com.user.model.request.UpdateUserRequest;
import com.user.model.response.UserDetailsResponse;
import com.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    private UserService userService;

    @PatchMapping("/user/{email}")
    public ResponseEntity<UserDetailsResponse> updateUserByEmail(@PathVariable("email")String email,
                                                                 @RequestBody UpdateUserRequest updateUserRequest)
            throws MyCustomException {
        log.info("Going to update details: {} for email: {}", updateUserRequest, email);
        return new ResponseEntity<>(userService.updateUserByEmail(email, updateUserRequest), HttpStatus.OK);
    }

    @DeleteMapping("/user/{email}")
    public ResponseEntity deleteUserByEmail(@PathVariable("email")String email) throws MyCustomException {
        log.info("Going to delete user: {}", email);
        userService.deleteUserByEmail(email);
        return new ResponseEntity(HttpStatus.OK);
    }
}
