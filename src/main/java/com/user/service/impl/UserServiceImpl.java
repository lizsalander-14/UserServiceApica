package com.user.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.entity.User;
import com.user.exception.MyCustomException;
import com.user.kafka.KafkaProducer;
import com.user.model.UserActionType;
import com.user.model.UserJournalDTO;
import com.user.model.request.RegisterUserRequest;
import com.user.model.request.UpdateUserRequest;
import com.user.model.response.UserDetailsResponse;
import com.user.repository.UserRepository;
import com.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaProducer producer;

    @Value("${kafka.topic.user.events}")
    private String userEventTopic;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public UserDetailsResponse registerUser(RegisterUserRequest registerUserRequest) throws MyCustomException {
        User user = userRepository.findByEmail(registerUserRequest.getEmail());
        if(Objects.nonNull(user)){
            log.error("User for email: {} is already registered", registerUserRequest.getEmail());
            throw new MyCustomException("User already exists");
        }
        user = new User();
        BeanUtils.copyProperties(registerUserRequest, user, "password");
        user.setPassword(passwordEncoder().encode(registerUserRequest.getPassword()));
        userRepository.save(user);
        sendUserEvent(user, UserActionType.REGISTER);
        return new UserDetailsResponse(user);
    }

    @Override
    public UserDetailsResponse getUserByEmail(String email) {
        UserDetailsResponse response = null;
        User user = userRepository.findByEmail(email);
        if(Objects.nonNull(user)){
            response = new UserDetailsResponse(user);
        }
        return response;
    }

    @Override
    public UserDetailsResponse updateUserByEmail(String email, UpdateUserRequest updateUserRequest) throws MyCustomException {
        User user = userRepository.findByEmail(email);
        if(Objects.isNull(user)){
            log.error("User for email: {} not found", email);
            throw new MyCustomException("User not found");
        }
        user.setFirstName(updateUserRequest.getFirstName());
        user.setLastName(updateUserRequest.getLastName());
        userRepository.save(user);
        sendUserEvent(user, UserActionType.UPDATE);
        return new UserDetailsResponse(user);
    }

    @Override
    public void deleteUserByEmail(String email) throws MyCustomException {
        User user = userRepository.findByEmail(email);
        if(Objects.isNull(user)){
            log.error("User for email: {} not found", email);
            throw new MyCustomException("User not found");
        }
        userRepository.delete(user);
        sendUserEvent(user, UserActionType.DELETE);
    }

    private void sendUserEvent(User user, UserActionType actionType){
        ObjectMapper objectMapper=new ObjectMapper();
        UserJournalDTO userJournalDTO = new UserJournalDTO(user.getEmail(), actionType, user.toString());
        try {
            producer.sendMessage(userEventTopic, objectMapper.writeValueAsString(userJournalDTO));
        } catch (JsonProcessingException e) {
            log.error("Error sending user event for user: {}, actinType: {}", user, actionType);
        }
    }
}
