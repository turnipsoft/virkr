package dk.ts.virkr.aarsrapporter.model

import dk.ts.virkr.aarsrapporter.db.Regnskabsdata
import org.springframework.beans.BeanUtils

/**
 * Created by sorenhartvig on 24/06/2017.
 */
class Passiver extends ModelBase {
  Long gaeldsforpligtelser
  Long egenkapital

  public static Passiver from(Regnskabsdata rd) {
    Passiver p = new Passiver()
    BeanUtils.copyProperties(rd, p)
    return p
  }
}
