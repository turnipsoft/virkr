package dk.ts.virkr.aarsrapporter.cache

import dk.ts.virkr.aarsrapporter.model.RegnskabData

/**
 * Created by sorenhartvig on 16/11/2017.
 */
interface RegnskabsdataCache {

  /**
   * Har til ansvar at hente regnskabsdata fra cachen på et givent cvrnummer
   * @param cvrnummer
   * @return
   */
  List<RegnskabData> hentRegnskaber(String cvrnummer)

  /**
   * Har til ansvar at gemme en række regnskaber i cachen
   * @param rd
   */
  void gemRegnskabsdata(List<RegnskabData> rd)

  /**
   * Har til ansvar at opdatere cachen med nye regnskaber der måtte være kommet på virksomheder
   */
  int opdaterCacheMedNyeRegnskaber()

}
