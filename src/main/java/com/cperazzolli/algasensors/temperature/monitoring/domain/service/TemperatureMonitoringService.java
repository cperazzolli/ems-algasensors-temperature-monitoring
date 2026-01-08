package com.cperazzolli.algasensors.temperature.monitoring.domain.service;

import com.cperazzolli.algasensors.temperature.monitoring.api.model.TemperatureLogData;
import com.cperazzolli.algasensors.temperature.monitoring.domain.model.SensorId;
import com.cperazzolli.algasensors.temperature.monitoring.domain.model.SensorMonitoring;
import com.cperazzolli.algasensors.temperature.monitoring.domain.model.TemperatureLog;
import com.cperazzolli.algasensors.temperature.monitoring.domain.model.TemperatureLogId;
import com.cperazzolli.algasensors.temperature.monitoring.domain.repository.SensorMonitoringRepository;
import com.cperazzolli.algasensors.temperature.monitoring.domain.repository.TemperatureLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemperatureMonitoringService {

    private final TemperatureLogRepository temperatureLogRepository;
    private final SensorMonitoringRepository sensorMonitoringRepository;

    @Transactional
    public void processingTemperatureReading(TemperatureLogData temperatureLogData) {

        sensorMonitoringRepository.findById(new SensorId(temperatureLogData.getSensorId()))
                .ifPresentOrElse(sensor -> handleSensorMoniroting(temperatureLogData,sensor),() -> logIgnoreTemperature(temperatureLogData));
    }


    private void handleSensorMoniroting(TemperatureLogData temperatureLogData, SensorMonitoring sensor) {
        if (sensor.isEnabled()) {
            sensor.setLastTemperature(temperatureLogData.getValue());
            sensor.setUpdatedAt(OffsetDateTime.now());
            sensorMonitoringRepository.save(sensor);

            TemperatureLog temperatureLog = TemperatureLog.builder()
                    .id(new TemperatureLogId(temperatureLogData.getId()))
                    .sensorId(new SensorId(temperatureLogData.getSensorId()))
                    .value(temperatureLogData.getValue())
                    .createdAt(temperatureLogData.getRegisteredAt())
                    .build();
            temperatureLogRepository.save(temperatureLog);
            log.info("Temperature update for sensorId {} temp {}", temperatureLogData.getSensorId(), temperatureLogData.getValue());
        } else {
            logIgnoreTemperature(temperatureLogData);
        }
    }

    private void logIgnoreTemperature(TemperatureLogData temperatureLogData) {
        log.warn("Temperature reading ignored for sensorId {} temp {}", temperatureLogData.getSensorId(), temperatureLogData.getValue());
    }
}
