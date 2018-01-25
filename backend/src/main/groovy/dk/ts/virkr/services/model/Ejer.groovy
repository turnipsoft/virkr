package dk.ts.virkr.services.model

import dk.ts.virkr.aarsrapporter.util.Utils
import dk.ts.virkr.cvr.integration.model.virksomhed.Attribut
import dk.ts.virkr.cvr.integration.model.virksomhed.Beliggenhedsadresse
import dk.ts.virkr.cvr.integration.model.virksomhed.DeltagerRelation
import dk.ts.virkr.cvr.integration.model.virksomhed.Medlemsdata
import dk.ts.virkr.cvr.integration.model.virksomhed.Navn
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
  String ophoersdato
  boolean ophoert

  // level bruges til at styre grafisk hvilket niveau ejeren skal ligge på i grafen
  int level

  List<LegalEjerandel> reelleEjerandele = []

  static Ejer from(DeltagerRelation deltagerRelation) {

    Ejer ejer = new Ejer()
    Navn nyesteNavn  = Utils.findNyeste(deltagerRelation.deltager.navne)
    ejer.navn = nyesteNavn.navn

    if (nyesteNavn.periode.gyldigTil!=null) {
      ejer.ophoert = true
    }

    ejer.ejertype = deltagerRelation.deltager.enhedstype == 'PERSON'?EjerType.PERSON : EjerType.VIRKSOMHED

    Beliggenhedsadresse adresse = Utils.findNyeste(deltagerRelation.deltager.beliggenhedsadresse)

    if (adresse ) {
      ejer.adresse = adresse.adresselinie
    }

    ejer.enhedsnummer = deltagerRelation.deltager.enhedsNummer
    ejer.forretningsnoegle = deltagerRelation.deltager.forretningsnoegle

    boolean aktuel = !ejer.ophoert
    berigEjerMedEjerAndele(ejer, deltagerRelation, aktuel)
    if (!ejer.andel) {
      berigEjerMedEjerAndele(ejer, deltagerRelation, !aktuel)
    }

    return ejer

  }

  static void berigEjerMedEjerAndele(Ejer ejer, DeltagerRelation deltagerRelation, boolean aktuel) {
    Medlemsdata medlemsdata = findMedlemsdata(deltagerRelation, 'EJERREGISTER', aktuel)
    if (medlemsdata) {
      // så findes der ejeroplysninger
      ejer.andel = findVaerdi(medlemsdata, 'EJERANDEL_PROCENT', aktuel)
      ejer.andelInterval = interval(ejer.andel)
      ejer.stemmeprocent = findVaerdi(medlemsdata, 'EJERANDEL_STEMMERET_PROCENT', aktuel)
      ejer.stemmeprocentInterval = interval(ejer.stemmeprocent)
      ejer.kapitalklasse = findVaerdi(medlemsdata, 'EJERANDEL_KAPITALKLASSE', aktuel)
      Vaerdi meddelelseDato = findAttributVaerdi(medlemsdata, 'EJERANDEL_MEDDELELSE_DATO', aktuel)
      if (!meddelelseDato) {
        meddelelseDato = findAttributVaerdi(medlemsdata, 'EJERANDEL_PROCENT', aktuel)
      }
      ejer.ophoersdato = meddelelseDato.periode.gyldigTil
    }
  }

  static Medlemsdata findMedlemsdata(DeltagerRelation deltagerRelation, String navn, boolean aktuel) {
    Organisation ejerOrganisation = deltagerRelation.organisationer.find {it.organisationsNavn.find{it.periode.gyldigTil==null}.navn == navn}
    return findAktuelMedlemsdataFraOrganisation(ejerOrganisation, aktuel );
  }

  static Medlemsdata findAktuelMedlemsdataFraOrganisation(Organisation organisation, boolean aktuel) {
    Medlemsdata medlemsdata = organisation.medlemsData.find{
      Attribut attribut = it.attributter.find{a-> a.type == 'EJERANDEL_MEDDELELSE_DATO'}
      if (!attribut) {
        attribut = it.attributter.find{a-> a.type == 'EJERANDEL_PROCENT'}
      }
      if (attribut) {
        return attribut.vaerdier.find { v-> (v.periode.gyldigTil == null) == aktuel }
      }

      return false
    }
    return  medlemsdata
  }

  static String findAktuelVaerdi(Medlemsdata medlemsdata, String v) {
    return findVaerdi(medlemsdata, v, true)
  }

  /**
   * Forsøger at finde en værdi for en given medlemsdata
   * @param medlemsdata
   * @param v
   * @return
   */
  static String findVaerdi(Medlemsdata medlemsdata, String v, boolean aktuel) {
    Attribut attribut = medlemsdata.attributter.find{ it.type==v }
    if (attribut) {
      Vaerdi vaerdi = attribut.vaerdier.find { (it.periode.gyldigTil == null) == aktuel }
      if (vaerdi) {
        return vaerdi.vaerdi
      }
    }

    return null
  }

  /**
   * Forsøger at finde en attribut for en given medlemsdata
   * @param medlemsdata
   * @param v
   * @return
   */
  static Vaerdi findAttributVaerdi(Medlemsdata medlemsdata, String v, boolean aktuel) {
    Attribut attribut = medlemsdata.attributter.find{ it.type==v }
    if (attribut) {
      Vaerdi vaerdi = attribut.vaerdier.find { (it.periode.gyldigTil == null) == aktuel }
      if (vaerdi) {
        return vaerdi
      }
    }

    return null
  }


  static String interval(String andel) {
    if (!andel) {
      return null
    }

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
