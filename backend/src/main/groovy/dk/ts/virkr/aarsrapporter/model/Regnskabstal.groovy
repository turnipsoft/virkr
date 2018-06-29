package dk.ts.virkr.aarsrapporter.model

/**
 * Created by sorenhartvig on 15/12/2017.
 */
class Regnskabstal {
  Long vaerdi
  Long decimal
  RegnskabstalMetadata metadata
  String xbrlFeltnavn
  String feltnavn
  String placering

  Regnskabstal() {
    this.metadata = new RegnskabstalMetadata()
  }

  Regnskabstal(Long vaerdi, Long decimal, String xbrlFeltnavn, String feltnavn, String placering) {
    this()
    this.vaerdi = vaerdi
    this.decimal = decimal
    this.xbrlFeltnavn = xbrlFeltnavn
    this.feltnavn = feltnavn
    this.placering = placering
  }

  void tilfoejKontrol(RegnskabstalKontrolResultat resultat) {
    this.metadata.kontroller << resultat
  }

}
