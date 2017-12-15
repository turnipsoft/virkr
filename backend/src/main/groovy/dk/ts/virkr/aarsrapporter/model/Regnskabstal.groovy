package dk.ts.virkr.aarsrapporter.model

/**
 * Created by sorenhartvig on 15/12/2017.
 */
class Regnskabstal {
  Long vaerdi
  RegnskabstalMetadata metadata

  Regnskabstal() {
    this.metadata = new RegnskabstalMetadata()
  }

  Regnskabstal(Long vaerdi) {
    this()
    this.vaerdi = vaerdi
  }

  void tilfoejKontrol(RegnskabstalKontrolResultat resultat) {
    this.metadata.kontroller << resultat
  }

}
