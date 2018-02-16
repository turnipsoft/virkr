package dk.ts.virkr.batch

import dk.ts.virkr.aarsrapporter.cache.RegnskabsdataCacheFactory
import dk.ts.virkr.services.internal.RegnskabInternalService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.time.LocalDateTime

/**
 * Created by sorenhartvig on 27/05/2017.
 */
@Service
class AarsrapportOpdatering {

  Logger logger = LoggerFactory.getLogger(AarsrapportOpdatering.class)

  @Autowired
  RegnskabsdataCacheFactory regnskabsdataCacheFactory

  @Autowired
  RegnskabInternalService regnskabInternalService

  @Value('${virkr.aarsrapporter.caching}')
  Boolean useCaching;

  @Transactional
  int opdaterRegnskaber() {
    if (!useCaching) {
      logger.info("Caching is disabled and reports won't get updated then")
      return 0
    }

    return regnskabsdataCacheFactory.getRegnskabsdataCache().opdaterCacheMedNyeRegnskaber()
  }
}
