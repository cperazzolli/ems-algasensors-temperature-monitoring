package com.cperazzolli.algasensors.temperature.monitoring.api.controller;

import com.cperazzolli.algasensors.temperature.monitoring.api.model.SensorAlertInput;
import com.cperazzolli.algasensors.temperature.monitoring.api.model.SensorAlertOutput;
import com.cperazzolli.algasensors.temperature.monitoring.domain.model.SensorAlert;
import com.cperazzolli.algasensors.temperature.monitoring.domain.model.SensorId;
import com.cperazzolli.algasensors.temperature.monitoring.domain.repository.SensorAlertRepository;
import io.hypersistence.tsid.TSID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@AllArgsConstructor
@RestController
@RequestMapping("/api/sensors/{sensorId}/alert")
public class SensorAlertController {

    private final SensorAlertRepository sensorAlertRepository;


    @GetMapping
    public SensorAlertOutput getAlert(@PathVariable("sensorId") String sensorId) {

        SensorAlert sensorAlert = sensorAlertRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor not found"));
        return SensorAlertOutput.builder()
                .sensorId(sensorAlert.getId().getValue())
                .maxTemperature(sensorAlert.getMaxTemperature())
                .minTemperature(sensorAlert.getMinTemperature())
                .build();

    }

    @PutMapping
    public SensorAlertOutput updateAlert(@PathVariable("sensorId") TSID sensorId, @RequestBody SensorAlertInput request) {
        SensorAlert sensorAlert = sensorAlertRepository.findById(new SensorId(sensorId))
                .orElse(SensorAlert.builder()
                        .id(new SensorId(sensorId))
                        .build());
        sensorAlert.setMaxTemperature(request.getMaxTemperature());
        sensorAlert.setMinTemperature(request.getMinTemperature());
        SensorAlert sensorAlertUpdate = sensorAlertRepository.saveAndFlush(sensorAlert);
        return SensorAlertOutput.builder()
                .sensorId(sensorAlertUpdate.getId().getValue())
                .maxTemperature(sensorAlertUpdate.getMaxTemperature())
                .minTemperature(sensorAlertUpdate.getMinTemperature())
                .build();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAlert(@PathVariable("sensorId") TSID sensorId) {
        SensorAlert sensorAlert = sensorAlertRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor not found"));
        sensorAlertRepository.delete(sensorAlert);
    }

}
