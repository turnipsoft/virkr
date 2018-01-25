package dk.ts.virkr.services.cache

import dk.ts.virkr.aarsrapporter.cache.GuavaCache

import dk.ts.virkr.aarsrapporter.cache.NoOpRegnskabsdataCache
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

/**
 * Created by sorenhartvig on 1/24/18.
 */
@Service
class CvrCacheFactory {
  @Value('${virkr.cvr.cachefactory}')
  String cacheFactory

  GuavaCache guavaCache = new GuavaCache()

  CvrCache getCache() {
    if (cacheFactory.equalsIgnoreCase('guavacache')) {
      return guavaCache
    }

    return new NoOpRegnskabsdataCache()
  }
}
