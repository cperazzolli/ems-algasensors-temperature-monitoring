package com.cperazzolli.algasensors.temperature.monitoring.api.controller;

import com.cperazzolli.algasensors.temperature.monitoring.api.model.SensorMonitoringOutput;
import com.cperazzolli.algasensors.temperature.monitoring.domain.model.SensorId;
import com.cperazzolli.algasensors.temperature.monitoring.domain.model.SensorMonitoring;
import com.cperazzolli.algasensors.temperature.monitoring.domain.repository.SensorMonitoringRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1/sensors/{sensorId}/monitoring")
@RequiredArgsConstructor
public class SensorMonitoringController {

    private final SensorMonitoringRepository sensorMonitoringRepository;

    @GetMapping
    public SensorMonitoringOutput getMonitoring(@PathVariable("sensorId") TSID sensorId) {
        SensorMonitoring sensorMonitoring = getSensorMonitoring(sensorId);

        return SensorMonitoringOutput.builder()
                .id(sensorMonitoring.getId().getValue())
                .enabled(sensorMonitoring.getEnabled())
                .updatedAt(sensorMonitoring.getUpdatedAt())
                .lastTemperature(sensorMonitoring.getLastTemperature())
                .build();
    }

    @PutMapping("/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable("sensorId") TSID sensorId) {
        SensorMonitoring monitoring = getSensorMonitoring(sensorId);
        if(monitoring.getEnabled()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        monitoring.setEnabled(true);
        sensorMonitoringRepository.saveAndFlush(monitoring);
    }

    @DeleteMapping("/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SneakyThrows
    public void disable(@PathVariable("sensorId") TSID sensorId) {
        SensorMonitoring monitoring = getSensorMonitoring(sensorId);
        if(!monitoring.getEnabled()) {
            Thread.sleep(Duration.ofSeconds(10));
        }
        monitoring.setEnabled(false);
        sensorMonitoringRepository.saveAndFlush(monitoring);
    }

    private SensorMonitoring getSensorMonitoring(TSID sensorId) {
        return sensorMonitoringRepository.findById(new SensorId(sensorId))
                .orElse(SensorMonitoring.builder()
                        .id(new SensorId(sensorId))
                        .enabled(false)
                        .updatedAt(null)
                        .lastTemperature(null)
                        .build());
    }
}
