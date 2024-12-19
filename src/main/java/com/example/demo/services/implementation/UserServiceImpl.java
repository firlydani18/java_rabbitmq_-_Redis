package com.example.demo.services.implementation;

import org.springframework.data.redis.core.RedisTemplate;
import com.example.demo.rabbitconfiguration.RabbitConfig;
import com.example.demo.rabbitproducer.NotificationProducer;
import com.example.demo.services.UserService;
import com.example.demo.repository.UserRepository;
import com.example.demo.model.Users;
import com.example.demo.exception.MessageBatchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.example.demo.rabbitconfiguration.RabbitConfig;
import java.util.stream.Collectors;

import java.util.Optional;
import java.util.List;

@Service
@CacheConfig(cacheNames = "Users")
public class UserServiceImpl implements UserService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private NotificationProducer producer;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    @Cacheable(value = "Users")
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Cacheable(key = "#id")
    public Users findByUserId(Long id) {
        return userRepository.findById(id);

    }

    @Override
    @CachePut(key = "#user.id")
    public Users saveUsers(Users user) {
        Users savedUser = userRepository.save(user);
        redisTemplate.opsForValue().set("user:" + savedUser.getId(), savedUser);
        System.out.println("User created: " + savedUser.toString());
        return savedUser;
    }


    @Override
    @CachePut(key = "#id")
    public Users updateUsers(Long id, Users user) {
        Users updated_user = userRepository.findById(id);
        if (updated_user != null) {

            updated_user.setFirstName(user.getFirstName());
            updated_user.setLastName(user.getLastName());
            updated_user.setEmail(user.getEmail());
            redisTemplate.opsForValue().set("user:" + id, updated_user);
            System.out.println("User created: " + updated_user.toString());
            return userRepository.save(updated_user);

        }
        return null;
    }

    @Override
    @CacheEvict(key = "#id", allEntries = true)
    public boolean  deleteUsers(Long id) {
        if (userRepository.existsById(id)) {
            redisTemplate.delete("user:" + id);
            userRepository.deleteById(id);
            return true;
        }
        return false;

    }

    @Override
    public void sendPromo(String messageContent) {
        producer.sendPromo(messageContent);
    }

    @Override
    public void sendNotification(String messageContent) {
        producer.sendNotification(messageContent);
    }

    @Override
    public void sendAlert(String messageContent) {
        producer.sendAlert(messageContent);
    }

    @Override
    public void sendBatchMessages(MessageBatchRequest messageBatchRequest) {
        if (messageBatchRequest.getPromoMessages() != null) {
            for (String promo : messageBatchRequest.getPromoMessages()) {
                sendPromo(promo);
            }
        }
        if (messageBatchRequest.getNotificationMessages() != null) {
            for (String notification : messageBatchRequest.getNotificationMessages()) {
                sendNotification(notification);
            }
        }
        if (messageBatchRequest.getAlertMessages() != null) {
            for (String alert : messageBatchRequest.getAlertMessages()) {
                sendAlert(alert);
            }
        }
    }


    @Override
    // Metode untuk mengambil semua pengguna dari Redis
    public List<Users> getAllUsersFromRedis() {
        // Mengambil semua kunci yang dimulai dengan "user:"
        List<String> keys = redisTemplate.keys("user:*").stream().collect(Collectors.toList());

        // Mengambil nilai untuk setiap kunci dan mengonversinya menjadi objek Users
        return keys.stream()
                .map(key -> (Users) redisTemplate.opsForValue().get(key))
                .collect(Collectors.toList());
    }

//    public Users getUserFromCache(Long id) {
//        String key = "user:" + id;
//        return (Users) redisTemplate.opsForValue().get(key);
//    }

}
