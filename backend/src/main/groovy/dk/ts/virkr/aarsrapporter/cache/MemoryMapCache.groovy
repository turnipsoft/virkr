package dk.ts.virkr.aarsrapporter.cache

import dk.ts.virkr.aarsrapporter.model.RegnskabData
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

/**
 * En simpel memorymap cache som højst kører med x-antal regnskaber
 */
@Service
class MemoryMapCache implements RegnskabsdataCache {

  Logger logger = LoggerFactory.getLogger(MemoryMapCache.class)

  @Value('${virkr.aarsrapporter.memorycache.size:1000}')
  Integer memoryCacheSize

  Map<String, List<RegnskabData>> memoryMap = [:]

  @Override
  List<RegnskabData> hentRegnskaber(String cvrnummer) {
    return this.memoryMap.get(cvrnummer)
  }

  @Override
  void gemRegnskabsdata(List<RegnskabData> rd) {
    if (rd) {
      String cvrnummer = rd.get(0).cvrnummer
      logger.info("Gemmer regnskabsdata i MemoryMapCache på cvrnummer: "+cvrnummer)
      this.memoryMap.put(cvrnummer, rd)
    }
  }

  private  synchronized  void clearCache(int amount) {
    if (this.memoryMap.keySet().size()>memoryCacheSize) {
      logger.info("Rydder op i cache")
      int i = 0
      List<String> toRemove = this.memoryMap.keySet().collect {
        if (i < amount) {
          return it
        } else {
          return null
        }
      } - null

      toRemove.each {
        this.memoryMap.remove(it)
      }
    }
  }

  @Override
  int opdaterCacheMedNyeRegnskaber() {
    return 0
  }
}
