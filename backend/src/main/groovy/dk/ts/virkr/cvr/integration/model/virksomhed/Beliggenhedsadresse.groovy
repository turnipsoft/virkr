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
      return vejnavn + (husnummerFra ? " " + husnummerFra : "") +
                       (husnummerTil ? "-" + husnummerTil : "") +
                       (bogstavFra ? " "+bogstavFra :"")+
                       (bogstavTil ? "-"+bogstavTil :"")+
                       (etage ? ", " + etage : "") +
                       (sidedoer ? ". " + sidedoer : "")
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
      String adrlinie = ''
      if (conavn) {
        adrlinie += 'C/O '+conavn + ', '
      }
      adrlinie += vejadresselinie+', '+byLinje
      return adrlinie
    } else if (fritekst) {
      return fritekst
    }

    return null
  }
}
