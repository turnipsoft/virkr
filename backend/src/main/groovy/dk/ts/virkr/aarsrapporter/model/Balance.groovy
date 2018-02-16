package dk.ts.virkr.aarsrapporter.model

/**
 * Created by sorenhartvig on 24/06/2017.
 */
class Balance extends ModelBase {

  Aktiver aktiver
  Passiver passiver

  Balance() {
    this.aktiver = new Aktiver()
    this.passiver = new Passiver()
  }
}
