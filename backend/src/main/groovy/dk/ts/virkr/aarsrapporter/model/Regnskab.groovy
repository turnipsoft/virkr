package dk.ts.virkr.aarsrapporter.model

/**
 * Created by sorenhartvig on 14/12/2017.
 */
class Regnskab extends ModelBase {

  Regnskab() {
    this.resultatopgoerelse = new Resultatopgoerelse()
    this.balance = new Balance()
    this.noter = new Noter()
    this.findesTal = true
    this.revision = new Revision()
  }

  boolean findesTal
  String startdato
  String slutdato
  String aar
  String regnskabsklasse

  Resultatopgoerelse resultatopgoerelse
  Balance balance
  Noter noter

  Revision revision

}
