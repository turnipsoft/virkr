package dk.ts.virkr.aarsrapporter.model

import dk.ts.virkr.aarsrapporter.db.Regnskabsdata
import org.springframework.beans.BeanUtils

/**
 * Created by sorenhartvig on 24/06/2017.
 */
class Passiver extends ModelBase {
  Regnskabstal gaeldsforpligtelser
  Regnskabstal egenkapital

  public static Passiver from(Regnskabsdata rd) {
    Passiver p = new Passiver()
    p.gaeldsforpligtelser = new Regnskabstal(rd.gaeldsforpligtelser)
    p.egenkapital = new Regnskabstal(rd.egenkapital)
    return p
  }

  void berig(Regnskabsdata rd) {
    rd.egenkapital = this.egenkapital.vaerdi
    rd.gaeldsforpligtelser = this.gaeldsforpligtelser.vaerdi
  }
}
