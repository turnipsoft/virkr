package dk.ts.virkr.aarsrapporter.cache

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import dk.ts.virkr.aarsrapporter.model.RegnskabData
import dk.ts.virkr.services.cache.CvrCache
import dk.ts.virkr.services.model.graf.DeltagerGraf
import dk.ts.virkr.services.model.graf.EjerGraf

import java.util.concurrent.TimeUnit

/**
 * Created by shartvig2 on 1/24/18.
 */
class GuavaCache implements RegnskabsdataCache, CvrCache {

  Cache<String, List<RegnskabData>> regnskabCache;
  Cache<String, EjerGraf> ejerGrafCache;
  Cache<String, DeltagerGraf> deltagerGrafCache;

  public GuavaCache() {
    regnskabCache = CacheBuilder.newBuilder()
      .maximumSize(1000)
      .expireAfterWrite(10, TimeUnit.HOURS)
      .build();

    ejerGrafCache = CacheBuilder.newBuilder().
      maximumSize(100).
      expireAfterWrite(30, TimeUnit.MINUTES).
      build()

    deltagerGrafCache = CacheBuilder.newBuilder().
      maximumSize(100).
      expireAfterWrite(30, TimeUnit.MINUTES).
      build()
  }

  @Override
  List<RegnskabData> hentRegnskaber(String cvrnummer) {
    return regnskabCache.getIfPresent(cvrnummer)
  }

  @Override
  void gemRegnskabsdata(List<RegnskabData> rd) {
    if (rd.size()>0) {
      String cvrnummer = rd.get(0).cvrnummer;
      regnskabCache.put(cvrnummer, rd)
    }
  }

  @Override
  DeltagerGraf hentDeltagerGraf(String enhedsnummer) {
    return deltagerGrafCache.getIfPresent(enhedsnummer)
  }

  @Override
  void gemDeltagerGraf(String enhedsnummer, DeltagerGraf deltagerGraf) {
    if (deltagerGraf) {
      deltagerGrafCache.put(enhedsnummer, deltagerGraf);
    }
  }

  @Override
  EjerGraf hentEjerGraf(String cvrnr) {
    return ejerGrafCache.getIfPresent(cvrnr)
  }

  @Override
  void gemEjerGraf(String cvrnr, EjerGraf ejerGraf) {
    if (ejerGraf) {
      ejerGrafCache.put(cvrnr, ejerGraf);
    }
  }

  @Override
  int opdaterCacheMedNyeRegnskaber() {
    return 0
  }
}
