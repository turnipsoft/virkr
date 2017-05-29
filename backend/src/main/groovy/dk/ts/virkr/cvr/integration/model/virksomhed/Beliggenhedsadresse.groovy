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
  String lat
  String lng

  public String getVejadresselinie() {
    return vejnavn + (husnummerFra?" "+ husnummerFra:"") + (husnummerTil?"-"+husnummerTil:"") +
      (etage?", "+etage:"") + (sidedoer?". "+sidedoer:"")
  }

  public String getByLinje() {
    return postnummer + " " + postdistrikt
  }
}
