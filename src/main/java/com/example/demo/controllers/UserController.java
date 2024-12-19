package com.example.demo.controllers;

import com.example.demo.rabbitproducer.NotificationProducer;
import com.example.demo.exception.MessageBatchRequest;
import com.example.demo.rabbitproducer.NotificationProducer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.example.demo.rabbitconfiguration.RabbitConfig;
import com.example.demo.services.UserService;
import com.example.demo.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private NotificationProducer producer;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getUserById(@PathVariable Long id) {
        Users user = userService.findByUserId(id);
        if (user == null) {
            // Mengembalikan 404 dengan pesan jika user tidak ditemukan
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with ID " + id + " not found.");
        }
        System.out.println("User found: " + user.toString());
        return new ResponseEntity<>("The user is\n " + user.toString(), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody Users user) {
        Users savedUser = userService.saveUsers(user);
        producer.sendMessage("users", "create", savedUser);
        return ResponseEntity.ok("User created successfully: " + savedUser);
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody Users user) {
        Users updatedUser = userService.updateUsers(id, user);
        if (updatedUser != null) {
            producer.sendMessage("users", "update", updatedUser);
//            System.out.println("User Update: " + updatedUser.toString());
            return ResponseEntity.ok("User with ID " + id + " updated successfully: " + updatedUser);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with ID " + id + " not found.");
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        boolean isDeleted = userService.deleteUsers(id);
        if (isDeleted) {
            producer.sendMessage("users", "delete", id);
            return ResponseEntity.ok("User with ID " + id + " deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with ID " + id + " not found.");
        }
    }


    // Mengirim pesan promo
    @PostMapping("/sendPromo")
    public ResponseEntity<String> sendPromo(@RequestBody String messageContent) {
        userService.sendPromo(messageContent);
        return ResponseEntity.ok("Promo message sent: " + messageContent);
    }

    // Mengirim pesan notifikasi
    @PostMapping("/sendNotification")
    public ResponseEntity<String> sendNotification(@RequestBody String messageContent) {
        userService.sendNotification(messageContent);
        return ResponseEntity.ok("Notification message sent: " + messageContent);
    }

    // Mengirim pesan alert
    @PostMapping("/sendAlert")
    public ResponseEntity<String> sendAlert(@RequestBody String messageContent) {
        userService.sendAlert(messageContent);
        return ResponseEntity.ok("Alert message sent: " + messageContent);
    }

    @PostMapping("/sendBatchMessages")
    public ResponseEntity<String> sendBatchMessages(@RequestBody MessageBatchRequest messageBatchRequest) {
        userService.sendBatchMessages(messageBatchRequest);
        return ResponseEntity.ok("Batch messages sent successfully.");
    }

    @GetMapping("/redis")
    public ResponseEntity<List<Users>> getAllUsersFromRedis() {
        List<Users> users = userService.getAllUsersFromRedis();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build(); // Mengembalikan 204 jika tidak ada pengguna
        }
        return ResponseEntity.ok(users); // Mengembalikan daftar pengguna
    }

}
