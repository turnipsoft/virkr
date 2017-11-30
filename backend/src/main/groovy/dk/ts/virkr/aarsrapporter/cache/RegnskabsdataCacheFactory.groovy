package dk.ts.virkr.aarsrapporter.cache

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

/**
 * Created by sorenhartvig on 16/11/2017.
 */
@Service
class RegnskabsdataCacheFactory {

  @Value('${virkr.aarsrapporter.cachefactory}')
  String cacheFactory

  final List<RegnskabsdataCache> caches

  RegnskabsdataCacheFactory(List<RegnskabsdataCache> caches) {
    this.caches = caches
    if (!cacheFactory) {
      cacheFactory = RegnskabsdataCache.CACHE_TYPE_NOOP
    }
  }

  RegnskabsdataCache getRegnskabsdataCache() {
    return caches.find { it.supports(cacheFactory) }
  }

}
