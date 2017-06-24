package dk.ts.virkr.aarsrapporter.model

import dk.ts.virkr.aarsrapporter.db.Regnskabsdata
import org.springframework.beans.BeanUtils

/**
 * Created by sorenhartvig on 24/06/2017.
 */
class Balance extends ModelBase {

  Aktiver aktiver
  Passiver passiver

  public Balance() {
    this.aktiver = new Aktiver()
    this.passiver = new Passiver()
  }

  public static Balance from(Regnskabsdata rd) {
    Balance balance = new Balance()
    balance.aktiver = Aktiver.from(rd)
    balance.passiver = Passiver.from(rd)
    return balance
  }

  public void berig(Regnskabsdata rd) {
    this.aktiver.berig(rd)
    this.passiver.berig(rd)
  }
}
