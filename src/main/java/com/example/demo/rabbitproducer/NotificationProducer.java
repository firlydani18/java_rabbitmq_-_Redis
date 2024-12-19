package com.example.demo.rabbitproducer;

import com.example.demo.rabbitconfiguration.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationProducer {

    @Autowired
    public NotificationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private static final String PRODUCT_EXCHANGE = "productExchange";

    public void sendPromo(String messageContent) {
        String message = "PROMO:" + messageContent;
        rabbitTemplate.convertAndSend(RabbitConfig.PRODUCT_QUEUE, message);
        System.out.println("Sent promo message: " + message);
    }

    public void sendNotification(String messageContent) {
        String message = "NOTIFICATION:" + messageContent;
        rabbitTemplate.convertAndSend(RabbitConfig.PRODUCT_QUEUE, message);
        System.out.println("Sent notification message: " + message);
    }


    public void sendAlert(String messageContent) {
        String message = "ALERT:" + messageContent;
        rabbitTemplate.convertAndSend(RabbitConfig.PRODUCT_QUEUE, message);
        System.out.println("Sent alert message: " + message);
    }


    public void sendMessage(String category, String action, Object payload) {
        String message = category.toUpperCase() + ":" + action.toUpperCase() + ":" + payload.toString();
        rabbitTemplate.convertAndSend(RabbitConfig.PRODUCT_QUEUE, message);
        System.out.println("Sent message: " + message);
    }

}
