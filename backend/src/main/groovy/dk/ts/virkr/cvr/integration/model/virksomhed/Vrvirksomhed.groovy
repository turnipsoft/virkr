package dk.ts.virkr.cvr.integration.model.virksomhed

import dk.ts.virkr.aarsrapporter.model.RegnskabData
import dk.ts.virkr.aarsrapporter.model.Revision
import dk.ts.virkr.aarsrapporter.util.Utils
import dk.ts.virkr.services.model.DeltagerVirksomhed
import dk.ts.virkr.services.model.Ejer

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
  List<Virksomhedsstatus> virksomhedsstatus
  List<DeltagerVirksomhed> deltagere
  List<Livsforloeb> livsforloeb

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

  private List<Ejer> getEjere(boolean aktuel) {

    List<Ejer> ejere = []
    if (this.deltagerRelation) {
      this.deltagerRelation.each {
        Organisation organisation = it.organisationer.find {it.hovedtype=='REGISTER'}
        if (organisation) {
          OrganisationsNavn on = organisation.organisationsNavn.find{it.navn=='EJERREGISTER' && it.periode.gyldigTil==null}
          if (on) {
            Medlemsdata medlemsdata = Ejer.findMedlemsdata(it, 'EJERREGISTER', aktuel)
            if (medlemsdata) {
              ejere << Ejer.from(it)
            }
          }
        }
      }
    }

    return ejere
  }

  private List<Revisor> getRevisorer(boolean aktuel) {
    List<Revisor> revisorer = []

    if (this.deltagerRelation) {
      this.deltagerRelation.each { dr ->
        Periode periode
        dr.organisationer.each { o->
          if (o.hovedtype=='REVISION') {
            o.medlemsData.each  {m->
              m.attributter.each { attr->
                if (attr.type=='FUNKTION') {
                  attr.vaerdier.each{ vaerdi->
                    if (vaerdi.vaerdi == 'REVISION') {
                      periode=vaerdi.periode
                    }
                  }
                }
              }
            }
          }
        }
        if (periode && ((periode.gyldigTil==null && aktuel) || !aktuel)) {
          Revisor revisor = new Revisor()
          revisor.periode = periode
          revisor.beliggenhedsadresse = Utils.findNyeste(dr.deltager.beliggenhedsadresse)
          revisor.navn = Utils.findNyeste(dr.deltager.navne).navn
          revisor.cvrnummer = dr.deltager.forretningsnoegle
          revisorer << revisor
        }

      }
    }

    return revisorer
  }

  Revisor getAktuelRevisor() {
    List<Revisor> r = getRevisorer(true)
    if (r) {
      return r[0]
    }

    return null
  }

  List<Revisor> getAlleRevisorer() {
    List<Revisor> r = getRevisorer(false)
    return r
  }

  List<Ejer> getAktuelleEjere() {
    return getEjere(true)
  }

  List<Ejer> getHistoriskeEjere() {
    return getEjere(false)
  }

  static String ledelsroller = ['direktion','adm. direktør','direktør','bestyrelse']

  boolean harDeltagerLedelsesRolle(DeltagerVirksomhed deltagerVirksomhed) {
    return deltagerVirksomhed.rolleliste.find{ledelsroller.contains(it)}
  }

  List<DeltagerVirksomhed> getLedelse() {
    List<DeltagerVirksomhed> deltagere = []
    deltagere = this.deltagere.findAll{ harDeltagerLedelsesRolle(it)}
    return deltagere
  }

  String getNyesteStatus() {
    if (this.virksomhedsstatus) {
      Virksomhedsstatus status = Utils.findNyeste(this.virksomhedsstatus)
      if (status) {
        return status.status
      }
    }

    return null
  }

  Livsforloeb getNyesteLivsforloeb() {
    if (this.livsforloeb) {
      return Utils.findNyeste(this.livsforloeb)
    }
  }
}
