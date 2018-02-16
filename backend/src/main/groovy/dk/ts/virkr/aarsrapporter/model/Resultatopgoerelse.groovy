package dk.ts.virkr.aarsrapporter.model

/**
 * Created by sorenhartvig on 24/06/2017.
 */
class Resultatopgoerelse extends ModelBase {

  OmsaetningTal omsaetningTal
  BruttoresultatTal bruttoresultatTal
  NettoresultatTal nettoresultatTal
  AaretsresultatTal aaretsresultatTal

  Resultatopgoerelse() {
    this.omsaetningTal = new OmsaetningTal()
    this.bruttoresultatTal = new BruttoresultatTal()
    this.nettoresultatTal = new NettoresultatTal()
    this.aaretsresultatTal = new AaretsresultatTal()
  }
}
