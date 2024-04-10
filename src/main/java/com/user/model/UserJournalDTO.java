package com.user.model;

import com.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserJournalDTO {

    private String email;
    private UserActionType actionType;
    private String userDetails;
}
