package dk.ts.virkr.cvr.integration.model.virksomhed

/**
 * Created by sorenhartvig on 29/05/2017.
 */
class Beliggenhedsadresse {

  String landekode
  String fritekst
  String vejkode
  String husnummerFra
  String husnummerTil
  String bogstavFra
  String bogstavTil
  String etage
  String sidedoer
  String conavn
  String postboks
  String vejnavn
  String bynavn
  String postnummer
  String postdistrikt
  Periode periode
  String sidstOpdateret
  String land
  String lat
  String lng

  String getVejadresselinie() {
    if (vejnavn) {
      return vejnavn + (husnummerFra ? " " + husnummerFra : "") + (husnummerTil ? "-" + husnummerTil : "") +
        (etage ? ", " + etage : "") + (sidedoer ? ". " + sidedoer : "")
    }

    return null
  }

  String getByLinje() {
    if (postnummer) {
      return postnummer + " " + postdistrikt
    }
  }

  String getAdresselinie() {
    if (vejadresselinie && byLinje) {
      return vejadresselinie+", "+byLinje
    } else if (fritekst) {
      return fritekst
    }

    return null
  }
}
