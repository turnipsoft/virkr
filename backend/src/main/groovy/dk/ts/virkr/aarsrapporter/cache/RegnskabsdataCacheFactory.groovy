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
  JpaRegnskabsdataCache jpaRegnskabsdataCache

  @Autowired
  MemoryMapCache memoryMapCache

  RegnskabsdataCache getRegnskabsdataCache() {
    if (cacheFactory.equalsIgnoreCase('jpa')) {
      return jpaRegnskabsdataCache
    } else if (cacheFactory.equalsIgnoreCase('memorymap')) {
      return memoryMapCache
    }

    return new NoOpRegnskabsdataCache()
  }

}
