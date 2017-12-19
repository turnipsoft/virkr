package dk.ts.virkr.aarsrapporter.parser.kontroller

import dk.ts.virkr.aarsrapporter.model.RegnskabData

/**
 * Created by sorenhartvig on 15/12/2017.
 */
interface RegnskabstalKontrol {
  void kontrollerOgBerig(List<RegnskabData> regnskabsdata)
}
