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
    p.gaeldsforpligtelser = rd.gaeldsforpligtelser
    p.egenkapital = rd.egenkapital
    return p
  }

  void berig(Regnskabsdata rd) {
    rd.egenkapital = this.egenkapital
    rd.gaeldsforpligtelser = this.gaeldsforpligtelser
  }
}
