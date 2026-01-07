package com.cperazzolli.algasensors.temperature.monitoring.infrastructure.rabbitmq;

import com.cperazzolli.algasensors.temperature.monitoring.api.model.TemperatureLogData;

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

    @RabbitListener(queues = QUEUE)
    public void handle(@Payload TemperatureLogData temperatureLogData,
                       @Headers Map<String, Object> headers) throws InterruptedException {
        TSID sensorId = temperatureLogData.getSensorId();
        Double value = temperatureLogData.getValue();

        log.info("Temperature update: sensorId {} temp {}", sensorId, value);
        log.info("Headers: {}", headers);
        Thread.sleep(Duration.ofSeconds(2));
    }
}
