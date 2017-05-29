package dk.ts.virkr.cvr.integration.model.virksomhed
/**
 * Created by sorenhartvig on 29/05/2017.
 */
class Vrvirksomhed {

  String cvrNummer
  boolean reklamebeskyttet
  List<Navn> navne
  List<Beliggenhedsadresse> beliggenhedsadresse
  List<Kontakoplysning> elektroniskPost
  List<Kontakoplysning> telefonNummer
  List<Kontakoplysning> telefaxNummer
  List<Kontakoplysning> hjemmeside
  List<Branche> hovedbranche
  VirksomhedMetadata virksomhedMetadata
  String sidstIndlaest
  String sidstOpdateret
  List<Attribut> attibutter

}
