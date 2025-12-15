package com.cperazzolli.algasensors.temperature.monitoring.domain.repository;

import com.cperazzolli.algasensors.temperature.monitoring.domain.model.SensorAlert;
import com.cperazzolli.algasensors.temperature.monitoring.domain.model.SensorId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorAlertRepository extends JpaRepository<SensorAlert, SensorId> {
}
