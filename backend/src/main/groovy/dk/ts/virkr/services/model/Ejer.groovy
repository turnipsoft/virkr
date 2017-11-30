package dk.ts.virkr.services.model

import dk.ts.virkr.cvr.integration.model.virksomhed.Attribut
import dk.ts.virkr.cvr.integration.model.virksomhed.Beliggenhedsadresse
import dk.ts.virkr.cvr.integration.model.virksomhed.DeltagerRelation
import dk.ts.virkr.cvr.integration.model.virksomhed.Medlemsdata
import dk.ts.virkr.cvr.integration.model.virksomhed.Organisation
import dk.ts.virkr.cvr.integration.model.virksomhed.Vaerdi

/**
 * Created by sorenhartvig on 01/07/2017.
 */
class Ejer {
  String enhedsnummer
  String forretningsnoegle
  EjerType ejertype
  String navn
  String adresse
  String andel
  String andelInterval
  String stemmeprocent
  String stemmeprocentInterval
  String kapitalklasse

  // level bruges til at styre grafisk hvilket niveau ejeren skal ligge på i grafen
  int level

  List<ReelEjerandel> reelleEjerandele = []

  static Ejer from(DeltagerRelation deltagerRelation) {
    Ejer ejer = new Ejer()
    ejer.navn = deltagerRelation.deltager.navne.find {it.periode.gyldigTil==null}?.navn
    ejer.ejertype = deltagerRelation.deltager.enhedstype == 'PERSON'?EjerType.PERSON : EjerType.VIRKSOMHED

    Beliggenhedsadresse aktuelAdresse = deltagerRelation.deltager.beliggenhedsadresse.find{ it.periode.gyldigTil==null }
    if (aktuelAdresse ) {
      ejer.adresse = aktuelAdresse.adresselinie
    }

    ejer.enhedsnummer = deltagerRelation.deltager.enhedsNummer
    ejer.forretningsnoegle = deltagerRelation.deltager.forretningsnoegle

    Medlemsdata medlemsdata = findAktuelleMedlemsdata(deltagerRelation)

    ejer.andel = findAktuelVaerdi(medlemsdata, 'EJERANDEL_PROCENT')
    ejer.andelInterval = interval(ejer.andel)
    ejer.stemmeprocent = findAktuelVaerdi(medlemsdata, 'EJERANDEL_STEMMERET_PROCENT')
    ejer.stemmeprocentInterval = interval(ejer.stemmeprocent)
    ejer.kapitalklasse = findAktuelVaerdi(medlemsdata,'EJERANDEL_KAPITALKLASSE')
    return ejer

  }

  static Medlemsdata findAktuelleMedlemsdata(DeltagerRelation deltagerRelation) {
    Organisation ejerOrganisation = deltagerRelation.organisationer.find {it.organisationsNavn.find{it.periode.gyldigTil==null}.navn == 'EJERREGISTER'}
    return findAktuelMedlemsdataFraOrganisation(ejerOrganisation);
  }

  static Medlemsdata findAktuelMedlemsdataFraOrganisation(Organisation organisation) {
    Medlemsdata medlemsdata = organisation.medlemsData.find{ it.attributter.find{it.type == 'EJERANDEL_PROCENT'}.vaerdier.find{it.periode.gyldigTil==null}}
    return  medlemsdata
  }

  static String findAktuelVaerdi(Medlemsdata medlemsdata, String v) {
    Attribut attribut = medlemsdata.attributter.find{ it.type==v }
    if (attribut) {
      Vaerdi vaerdi = attribut.vaerdier.find { it.periode.gyldigTil == null }
      return vaerdi.vaerdi
    }

    return null
  }

  static String interval(String andel) {
    double d = Double.parseDouble(andel)
    if (d==0) {
      return "0%"
    }

    if (d<0.05) {
      return "1-4,99%"
    }

    if (d<0.1) {
      return "5-9,99%"
    }

    if (d<0.15) {
      return "10-14,99%"
    }

    if (d<0.2) {
      return "15-19,99%"
    }

    if (d<0.25) {
      return "20-24.99%"
    }

    if (d<0.33) {
      return "25-33,32%"
    }

    if (d<0.50) {
      return "33,33-49,99%"
    }

    if (d<0.67) {
      return "50-66,66%"
    }

    if (d<1.0) {
      return "66,67-99,99%"
    }

    return "100%"
  }


}
