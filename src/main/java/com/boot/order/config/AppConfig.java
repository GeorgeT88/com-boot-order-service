package com.boot.order.config;

import java.io.IOException;
import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.velocity.VelocityEngineFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.boot.order.client.CartServiceClient;
import com.boot.order.rabbitmq.Producer;

@Configuration
public class AppConfig {
	
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
}
	@Bean
	public RestTemplate template() {
		return new RestTemplate();
	}

	@Bean
	public CartServiceClient productServiceClient() {
		return new CartServiceClient();
	}
	
	@Bean
	public Producer producer() {
		return new Producer();
	}
	
	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);

		mailSender.setUsername("testsurnametestname0@gmail.com");
		mailSender.setPassword("qwerty123456&&&");

        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.starttls.enable", "true");
        javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put("mail.transport.protocol", "smtp");
        javaMailProperties.put("mail.debug", "true");
 
        mailSender.setJavaMailProperties(javaMailProperties);

		return mailSender;
	}

    @Bean
    public VelocityEngine getVelocityEngine() throws VelocityException, IOException {
        VelocityEngineFactory velocityEngineFactory = new VelocityEngineFactory();
        Properties props = new Properties();
        props.put("resource.loader", "class");
        props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
 
        velocityEngineFactory.setVelocityProperties(props);
        return velocityEngineFactory.createVelocityEngine();
    }

    // Value is populated with the queue name from "application.properties" file.
    @Value("${rabbitmq.queue}")
    private String queueName;
     
    // Value is populated with the exchange name from "application.properties" file.
    @Value("${rabbitmq.exchange}")
    private String exchange;
     
    // Value is populated with the routing key from "application.properties" file.
    @Value("${rabbitmq.routingkey}")
    private String routingKey;
 
    // @Bean annotation tells that a method produces a bean which is to be managed by the spring container.
    @Bean
    Queue queue() {
        // Creating a queue.
        return new Queue(queueName, Boolean.FALSE);
    }
 
    @Bean
    TopicExchange topicExchange() {
        // Creating a topic exchange.
        return new TopicExchange(exchange);
    }
 
    @Bean
    Binding binding(final Queue queue, final TopicExchange topicExchange) {
        // Binding the queue to the topic with a routing key.
        return BindingBuilder.bind(queue).to(topicExchange).with(routingKey);
    }
	
}
