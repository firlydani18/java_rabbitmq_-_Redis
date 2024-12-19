package com.example.demo.messageconsumer;

import com.example.demo.model.Users;
import com.example.demo.model.Books;
import com.example.demo.repository.UserRepository;
import com.example.demo.rabbitconfiguration.RabbitConfig;
//import com.example.demo.rabbitconfiguration.ProductRabbitConfiguration;
import com.example.demo.services.BookService;
import com.example.demo.model.Users;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.util.List;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;



@Component
public class MessageConsumer{
    @Autowired
    private BookService bookService;

    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);
    @Autowired
    private UserRepository userRepository;

//    @RabbitListener(queues = RabbitConfig.PRODUCT_QUEUE, ackMode = "MANUAL", concurrency = "100-1000")
//    public void consumeMessage(String message) {
//        // Cek jenis pesan berdasarkan konten atau routing key
//        if (message.contains("user")) {
//            processUserMessage(message);
//        } else if (message.contains("book")) {
//            processBookMessage(message);
//        } else {
//            System.out.println("Received Unknown Message: " + message);
//        }
//    }

//    // Logika untuk memproses pesan User
//    private void processUserMessage(String message) {
//        System.out.println("Processing User Message...");
//
//        // Contoh pemrosesan logika bisnis berdasarkan action
//        if (message.contains("CREATE")) {
//            System.out.println("User Created: " + extractPayload(message));
//
//        } else if (message.contains("UPDATE")) {
//            System.out.println("User Updated: " + extractPayload(message));
//
//        } else if (message.contains("DELETE")) {
//            System.out.println("User Deleted: " + extractPayload(message));
//
//        } else {
//            System.out.println("Unknown User Action: " + message);
//        }
//    }
//
//    // Logika untuk memproses pesan Book
//    private void processBookMessage(String message) {
//        System.out.println("Processing Book Message...");
//
//        // Contoh pemrosesan logika bisnis berdasarkan action
//        if (message.contains("CREATE")) {
//            System.out.println("Book Created: " + extractPayload(message));
//
//        } else if (message.contains("UPDATE")) {
//            System.out.println("Book Updated: " + extractPayload(message));
//
//        } else if (message.contains("DELETE")) {
//            System.out.println("Book Deleted: " + extractPayload(message));
//
//        } else {
//            System.out.println("Unknown Book Action: " + message);
//        }
//    }
//
//    // Fungsi untuk mengekstrak payload dari pesan
//    private String extractPayload(String message) {
//        // Asumsi format pesan: "Action: ACTION_TYPE, Payload: PAYLOAD"
//        int payloadIndex = message.indexOf("Payload: ");
//        if (payloadIndex != -1) {
//            return message.substring(payloadIndex + 9);
//        }
//        return "No payload found";
//    }
//

    @RabbitListener(queues = RabbitConfig.PRODUCT_QUEUE, ackMode = "MANUAL") // , concurrency = "1000-10000"
    public void receiveMessage(String message, Message rabbitMessage, Channel channel) throws IOException {
        try {
            String[] parts = message.split(":", 2);
            String messageType = parts[0];
            String messageContent = parts.length > 1 ? parts[1] : "";

            switch (messageType) {
                case "PROMO":
                    logger.info("Received promo message: {}", messageContent);
                    break;
                case "NOTIFICATION":
                    logger.info("Received notification: {}", messageContent);
                    break;
                case "ALERT":
                    logger.info("Received alert message: {}", messageContent);
                    break;
                case "USERS":
                    //processUserMessage(messageContent);
                    logger.info("Received User message: {}", messageContent);
                    break;
                case "BOOKS":
                    //processBookMessage(messageContent);
                    logger.info("Received Books message: {}", messageContent);
                    break;
                default:
                    logger.warn("Unknown message type: {}", messageType);
            }

            channel.basicAck(rabbitMessage.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            logger.error("Error processing message: {}", e.getMessage());
            channel.basicNack(rabbitMessage.getMessageProperties().getDeliveryTag(), false, true);
        }
    }

}
