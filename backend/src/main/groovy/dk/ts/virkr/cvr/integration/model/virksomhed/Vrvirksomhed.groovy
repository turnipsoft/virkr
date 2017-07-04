package dk.ts.virkr.cvr.integration.model.virksomhed
/**
 * Created by sorenhartvig on 29/05/2017.
 */
class Vrvirksomhed {

  String cvrNummer
  String enhedsNummer
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
  List<DeltagerRelation> deltagerRelation
  List<Produktionsenhed> penheder

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

  List<Ejer> getEjere() {
    List<Ejer> ejere = []
    if (this.deltagerRelation) {
      this.deltagerRelation.each {
        Organisation organisation = it.organisationer.find {it.hovedtype=='REGISTER'}
        if (organisation) {
          OrganisationsNavn on = organisation.organisationsNavn.find{it.navn=='EJERREGISTER' && it.periode.gyldigTil==null}
          if (on) {
            Medlemsdata medlemsdata = Ejer.findAktuelleMedlemsdata(it)
            if (medlemsdata) {
              ejere << Ejer.from(it)
            }
          }
        }
      }
    }

    return ejere
  }
}
