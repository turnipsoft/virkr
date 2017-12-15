package dk.ts.virkr.aarsrapporter.parser.berigelse

import dk.ts.virkr.aarsrapporter.model.Regnskabstal

/**
 * Created by sorenhartvig on 25/06/2017.
 */
class Berigelse {

  long val(Regnskabstal val) {
    if (!val) return 0;

    return val.vaerdi?val.vaerdi.abs():0
  }

}
