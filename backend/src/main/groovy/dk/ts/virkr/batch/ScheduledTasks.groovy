package dk.ts.virkr.batch

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * Created by sorenhartvig on 27/05/2017.
 */
@Component
class ScheduledTasks {
  private static final Logger log = LoggerFactory.getLogger(ScheduledTasks)

  @Autowired
  AarsrapportOpdatering aarsrapportOpdatering

  @Scheduled(cron = "0 0 2 * * *")
  opdaterAarsrapporter() {
    int antalOpdaterede = aarsrapportOpdatering.opdaterRegnskaber()
    log.info("Opdateret ${antalOpdaterede} regnskaber")
  }
}
