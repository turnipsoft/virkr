package dk.ts.virkr.aarsrapporter.model

import dk.ts.virkr.aarsrapporter.db.Regnskabsdata

/**
 * Created by sorenhartvig on 24/06/2017.
 */
class Aktiver extends ModelBase {

  static Aktiver from(Regnskabsdata rd) {
    Aktiver aktiver = new Aktiver()
    return aktiver
  }

  void berig(Regnskabsdata rd) {

  }
}
