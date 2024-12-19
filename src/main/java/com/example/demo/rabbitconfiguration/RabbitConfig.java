package com.example.demo.rabbitconfiguration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import com.example.demo.messageconsumer.MessageConsumer;



@Configuration
@EnableRabbit
public class RabbitConfig {
    public static final String PRODUCT_EXCHANGE = "productExchange";
    public static final String PRODUCT_QUEUE = "productQueue";



    @Bean
    public Queue productQueue() {
        return new Queue(PRODUCT_QUEUE, true);
    }

    @Bean
    public DirectExchange productExchange() {
        return new DirectExchange(PRODUCT_EXCHANGE);
    }

    // Mengikat queue produk dengan exchange
    @Bean
    public Binding bindingBooks(Queue productQueue, DirectExchange PRODUCT_EXCHANGE) {
        return BindingBuilder.bind(productQueue).to(PRODUCT_EXCHANGE).with("books.*");
    }
    @Bean
    public Binding bindingUsers(Queue productQueue, DirectExchange PRODUCT_EXCHANGE) {
        return BindingBuilder.bind(productQueue).to(PRODUCT_EXCHANGE).with("users.*");
    }
    @Bean
    public Binding bindingPromo(Queue productQueue, DirectExchange PRODUCT_EXCHANGE) {
        return BindingBuilder.bind(productQueue).to(PRODUCT_EXCHANGE).with("promo.*");
    }
    @Bean
    public Binding bindingNotification(Queue productQueue, DirectExchange PRODUCT_EXCHANGE) {
        return BindingBuilder.bind(productQueue).to(PRODUCT_EXCHANGE).with("notification.*");
    }
    @Bean
    public Binding bindingAlert(Queue productQueue, DirectExchange PRODUCT_EXCHANGE) {
        return BindingBuilder.bind(productQueue).to(PRODUCT_EXCHANGE).with("alert.*");
    }

    // Mendefinisikan bean untuk konverter pesan JSON
    // Mengembalikan konverter pesan yang menggunakan Jackson untuk mengonversi pesan ke format JSON
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();

        return converter;
    }


    // Mendefinisikan bean untuk RabbitTemplate
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                System.out.println("Message successfully sent to RabbitMQ");
            } else {
                System.err.println("Message failed to send to RabbitMQ: " + cause);
            }
        });

        return rabbitTemplate;
    }
}