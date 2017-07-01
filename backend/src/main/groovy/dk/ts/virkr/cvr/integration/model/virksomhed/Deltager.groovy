package dk.ts.virkr.cvr.integration.model.virksomhed

/**
 * Created by sorenhartvig on 01/07/2017.
 */
class Deltager {
  String enhedsNummer
  String enhedstype
  String forretningsnoegle
  String organisationstype
  String sidstIndlaest
  String sidstOpdateret
  List<Navn> navne
  List<Beliggenhedsadresse> beliggenhedsadresse
}
