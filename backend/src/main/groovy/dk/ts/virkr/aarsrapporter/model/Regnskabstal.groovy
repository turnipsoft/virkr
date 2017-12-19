package dk.ts.virkr.aarsrapporter.model

/**
 * Created by sorenhartvig on 15/12/2017.
 */
class Regnskabstal {
  Long vaerdi
  Long decimal
  RegnskabstalMetadata metadata

  Regnskabstal() {
    this.metadata = new RegnskabstalMetadata()
  }

  Regnskabstal(Long vaerdi, Long decimal) {
    this()
    this.vaerdi = vaerdi
    this.decimal = decimal
  }

  void tilfoejKontrol(RegnskabstalKontrolResultat resultat) {
    this.metadata.kontroller << resultat
  }

}
