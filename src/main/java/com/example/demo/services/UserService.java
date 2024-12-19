package com.example.demo.services;

import com.example.demo.model.Users;
import com.example.demo.exception.MessageBatchRequest;

import java.util.List;

public interface UserService {

    List<Users> getAllUsers();
    Users findByUserId(Long id);
    Users saveUsers(Users user);
    Users updateUsers(Long id,Users user);
    boolean  deleteUsers(Long id);
    void sendNotification(String messageContent);
    void sendAlert(String messageContent);
    void sendPromo(String messageContent);
    void sendBatchMessages(MessageBatchRequest messageBatchRequest);

    List<Users> getAllUsersFromRedis();


}
