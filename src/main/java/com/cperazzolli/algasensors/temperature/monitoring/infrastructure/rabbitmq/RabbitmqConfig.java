package com.cperazzolli.algasensors.temperature.monitoring.infrastructure.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    public static final String QUEUE_PROCESSING_TEMPERATURE = "temperature-moniring.process-temperature.v1.q";

    public static final String QUEUE_ALERTING = "temperature-moniring.alerting.v1.q";

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Queue queueProcessorTemperature() {
        return QueueBuilder.durable(
                QUEUE_PROCESSING_TEMPERATURE
        ).build();
    }

    @Bean
    public Queue queueAlert() {
        return QueueBuilder.durable(
                QUEUE_ALERTING
        ).build();
    }

    @Bean
    public Binding bindingProcessorTemperature() {
        return BindingBuilder.bind(queueProcessorTemperature()).to(exchange());
    }

    @Bean
    public Binding bindingAlerting() {
        return BindingBuilder.bind(queueAlert()).to(exchange());
    }

    public FanoutExchange exchange() {
        return ExchangeBuilder.fanoutExchange(
                "temperature-processing.temperature-received.v1.e"
        ).build();
    }
}
