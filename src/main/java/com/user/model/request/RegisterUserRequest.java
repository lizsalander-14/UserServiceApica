package com.user.model.request;

import com.user.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequest {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Role role;
}
