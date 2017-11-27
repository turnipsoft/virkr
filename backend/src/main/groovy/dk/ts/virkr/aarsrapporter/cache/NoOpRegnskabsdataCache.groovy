package dk.ts.virkr.aarsrapporter.cache

import dk.ts.virkr.aarsrapporter.model.RegnskabData
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * En cache der gør absolut ingenting
 */
class NoOpRegnskabsdataCache implements RegnskabsdataCache {

  Logger logger = LoggerFactory.getLogger(RegnskabsdataCache.class)

  @Override
  List<RegnskabData> hentRegnskaber(String cvrnummer) {
    logger.warn("NoOpCache:hentRegnskaber")
    return null
  }

  @Override
  void gemRegnskabsdata(List<RegnskabData> rd) {
    logger.warn("NoOpCache:gemRegnskabsdata")
  }

  @Override
  int opdaterCacheMedNyeRegnskaber() {
    logger.warn("NoOpCache:opdaterCacheMedNyeRegnskaber")
    return 0
  }
}
