package dk.ts.virkr.aarsrapporter.cache

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

/**
 * Created by sorenhartvig on 16/11/2017.
 */
@Service
class RegnskabsdataCacheFactory {

  @Value('${virkr.aarsrapporter.cachefactory}')
  String cacheFactory

  @Autowired
  MemoryMapCache memoryMapCache
  GuavaCache guavaCache = new GuavaCache()

  RegnskabsdataCache getRegnskabsdataCache() {
    if (cacheFactory.equalsIgnoreCase('memorymap')) {
      return memoryMapCache
    } else if (cacheFactory.equalsIgnoreCase('guavacache')) {
      return guavaCache
    }

    return new NoOpRegnskabsdataCache()
  }

}
