package dk.ts.virkr.aarsrapporter.parser.kontroller

import dk.ts.virkr.aarsrapporter.model.RegnskabData

/**
 * Created by sorenhartvig on 15/12/2017.
 */
class RegnskabsKontroller {

  void kontrollerOgBerig(List<RegnskabData> regnskabData) {
    KontrolAfvigendeTal kontrolAfvigendeTal = new KontrolAfvigendeTal()
    kontrolAfvigendeTal.kontrollerOgBerig(regnskabData)
  }
}
