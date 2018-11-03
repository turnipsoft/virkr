package dk.ts.virkr.services

import dk.ts.virkr.services.model.metrics.Metric
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by sorenhartvig on 03/11/2018.
 */
@Service
class MetricsService {

  Logger logger = LoggerFactory.getLogger(MetricsService.class)
  Map<Metric, Counter> counterMap

  @Autowired
  MeterRegistry meterRegistry

  String counterName(Metric metric) {
    return "virkr.metrics.${metric.name().toLowerCase()}"
  }
  void init() {
    counterMap = [:]
    counterMap.put(Metric.VIRKSOMHED,meterRegistry.counter(counterName(Metric.VIRKSOMHED)))
    counterMap.put(Metric.PERSON,meterRegistry.counter(counterName(Metric.PERSON)))
    counterMap.put(Metric.SOEGNING,meterRegistry.counter(counterName(Metric.SOEGNING)))
    counterMap.put(Metric.FEJLVIRKSOMHED,meterRegistry.counter(counterName(Metric.FEJLVIRKSOMHED)))
    counterMap.put(Metric.FEJLPERSON,meterRegistry.counter(counterName(Metric.FEJLPERSON)))
    counterMap.put(Metric.FEJLSOEGNING,meterRegistry.counter(counterName(Metric.FEJLSOEGNING)))
    logger.info("Initialized metrics")
  }

  void increment(Metric metric) {
    if (!counterMap) {
      init()
    }
    logger.info("increment metric {}", metric.name().toLowerCase())
    counterMap.get(metric).increment()
  }
}
