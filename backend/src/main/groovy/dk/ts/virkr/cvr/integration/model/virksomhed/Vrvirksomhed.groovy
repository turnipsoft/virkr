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
  List<Attribut> attributter

  String getKapital() {
    return getAttributVaerdi('KAPITAL')
  }

  String getTegningsregel() {
    return getAttributVaerdi('TEGNINGSREGEL')
  }

  String getFormaal() {
    return getAttributVaerdi('FORMÅL')
  }

  String getAttributVaerdi(String type) {
    Attribut attribut = attributter.find { it.type == type }
    if (attribut) {
      return attribut.vaerdier[0].vaerdi
    }
    return null
  }

}
