package com.cperazzolli.algasensors.temperature.monitoring.infrastructure.rabbitmq;

import com.cperazzolli.algasensors.temperature.monitoring.api.model.TemperatureLogData;

import com.cperazzolli.algasensors.temperature.monitoring.domain.service.TemperatureMonitoringService;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

import static com.cperazzolli.algasensors.temperature.monitoring.infrastructure.rabbitmq.RabbitmqConfig.QUEUE;

@Slf4j
@RequiredArgsConstructor
@Component
public class RabbitmqListener {

    private final TemperatureMonitoringService temperatureMonitoringService;

    @RabbitListener(queues = QUEUE)
    public void handle(@Payload TemperatureLogData temperatureLogData) throws InterruptedException {

        temperatureMonitoringService.processingTemperatureReading(temperatureLogData);
        Thread.sleep(Duration.ofSeconds(5));
    }
}
