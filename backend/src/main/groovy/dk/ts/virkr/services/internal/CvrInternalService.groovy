package dk.ts.virkr.services.internal

import dk.ts.virkr.aarsrapporter.util.Utils
import dk.ts.virkr.cvr.integration.CvrClient
import dk.ts.virkr.cvr.integration.model.deltager.VirksomhedSummariskRelation
import dk.ts.virkr.cvr.integration.model.deltager.Vrdeltagerperson
import dk.ts.virkr.cvr.integration.model.virksomhed.Medlemsdata
import dk.ts.virkr.cvr.integration.model.virksomhed.Organisation
import dk.ts.virkr.cvr.integration.model.virksomhed.Virksomhedsstatus
import dk.ts.virkr.services.cache.CvrCacheFactory
import dk.ts.virkr.services.model.Ejer
import dk.ts.virkr.services.model.graf.DeltagerGraf
import dk.ts.virkr.services.model.graf.DeltagerRelation
import dk.ts.virkr.services.model.graf.EjerAfVirksomhed
import dk.ts.virkr.services.model.graf.EjerGraf
import dk.ts.virkr.services.model.graf.EjerRelation
import dk.ts.virkr.services.model.EjerType
import dk.ts.virkr.cvr.integration.model.virksomhed.Navn
import dk.ts.virkr.services.model.LegalEjerandel
import dk.ts.virkr.cvr.integration.model.virksomhed.Vrvirksomhed
import dk.ts.virkr.services.model.DeltagerSoegeresultat
import dk.ts.virkr.services.model.DeltagerVirksomhed
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

/**
 * Created by sorenhartvig on 03/07/2017.
 */
@Service
class CvrInternalService {

  Logger logger = LoggerFactory.getLogger(CvrInternalService.class)

  @Autowired
  CvrClient cvrClient

  @Autowired
  CvrCacheFactory cvrCacheFactory

  @Value('${virkr.cvr.caching}')
  Boolean useCaching;

  EjerGraf hentEjergraf(String cvrnummer) {
    if (useCaching) {
      EjerGraf ejerGraf = cvrCacheFactory.cache.hentEjerGraf(cvrnummer);
      if (ejerGraf) {
        return ejerGraf;
      }
    }

    EjerGraf ejerGraf = new EjerGraf()
    Vrvirksomhed vrvirksomhed = cvrClient.hentVirksomhed(cvrnummer)
    ejerGraf.virksomhed = vrvirksomhed

    EjerAfVirksomhed ejerAfVirksomhed = new EjerAfVirksomhed()
    ejerAfVirksomhed.cvrnummer = vrvirksomhed.cvrNummer
    ejerAfVirksomhed.virksomhedsnavn = vrvirksomhed.virksomhedMetadata.nyesteNavn.navn
    ejerAfVirksomhed.ejer = new Ejer()

    // Pseudo ejer for at have øverste niveau med ud, blive muligvis recactored så man mere eksplicit sætter roden
    ejerAfVirksomhed.ejer.navn = ejerAfVirksomhed.virksomhedsnavn
    ejerAfVirksomhed.ejer.forretningsnoegle = ejerAfVirksomhed.cvrnummer
    ejerAfVirksomhed.ejer.enhedsnummer = vrvirksomhed.enhedsNummer
    ejerAfVirksomhed.ejer.ejertype = EjerType.ROD
    ejerAfVirksomhed.ejer.level = 0

    ejerGraf.ejere << ejerAfVirksomhed

    berigEjergraf(vrvirksomhed, ejerGraf, ejerAfVirksomhed, 1)

    if (useCaching) {
      cvrCacheFactory.cache.gemEjerGraf(cvrnummer, ejerGraf)
    }
    return ejerGraf
  }

  DeltagerGraf hentEjergrafForPerson(String enhedsnummer) {
    if (useCaching) {
      DeltagerGraf deltagerGraf = cvrCacheFactory.cache.hentDeltagerGraf(enhedsnummer)
      if (deltagerGraf) {
        return deltagerGraf
      }
    }

    DeltagerGraf deltagerGraf = new DeltagerGraf()
    Vrdeltagerperson vrdeltagerperson = cvrClient.hentDeltager(enhedsnummer);
    deltagerGraf.deltager = vrdeltagerperson

    // hent alle virksomheder som personen ejer
    List<Vrvirksomhed> virksomheder = cvrClient.hentVirksomhedsDeltagere(enhedsnummer)

    virksomheder.each { virksomhed->
      // tager den kun med såfremt deltager faktisk er ejer af virksomheden, hvilket man kan se ved at virksomhedens ejere inkluderer personen
      if (virksomhed.aktuelleEjere && virksomhed.aktuelleEjere.find { it.enhedsnummer == enhedsnummer}) {
        berigDeltagersVirksomhed(vrdeltagerperson.enhedsNummer, virksomhed, deltagerGraf, 0, [])
      }
    }

    if (useCaching) {
      cvrCacheFactory.cache.gemDeltagerGraf(enhedsnummer, deltagerGraf)
    }
    return deltagerGraf
  }

  DeltagerGraf hentDeltagerGrafForVirksomhed(String cvrnummer) {
    if (useCaching) {
      DeltagerGraf deltagerGraf = cvrCacheFactory.cache.hentDeltagerGraf(cvrnummer)
      if (deltagerGraf) {
        return deltagerGraf
      }
    }

    DeltagerGraf deltagerGraf = new DeltagerGraf()
    Vrvirksomhed vrvirksomhed = cvrClient.hentVirksomhed(cvrnummer)
    deltagerGraf.virksomhed = vrvirksomhed

    String enhedsnummer = vrvirksomhed.enhedsNummer

    // hent alle virksomheder som virksomheden ejer
    List<Vrvirksomhed> virksomheder = cvrClient.hentVirksomhedsDeltagere(enhedsnummer)

    virksomheder.each { virksomhed->
      // tager den kun med såfremt deltager faktisk er ejer af virksomheden, hvilket man kan se ved at virksomhedens ejere inkluderer personen
      if (virksomhed.aktuelleEjere && virksomhed.aktuelleEjere.find { it.enhedsnummer == enhedsnummer}) {
        berigDeltagersVirksomhed(enhedsnummer, virksomhed, deltagerGraf, 0, [])
      }
    }

    if (useCaching) {
      cvrCacheFactory.cache.gemDeltagerGraf(cvrnummer, deltagerGraf)
    }
    return deltagerGraf
  }


  EjerAfVirksomhed bygEjerAfVirksomhed(Vrvirksomhed virksomhed, String enhedsnummer){
    EjerAfVirksomhed ejerAfVirksomhed = new EjerAfVirksomhed()
    ejerAfVirksomhed.cvrnummer = virksomhed.cvrNummer
    ejerAfVirksomhed.enhedsnummer = virksomhed.enhedsNummer
    ejerAfVirksomhed.virksomhedsnavn = virksomhed.virksomhedMetadata.nyesteNavn.navn
    ejerAfVirksomhed.adresse = virksomhed.virksomhedMetadata.nyesteBeliggenhedsadresse?.adresselinie
    ejerAfVirksomhed.ejer = virksomhed.aktuelleEjere.find { it.enhedsnummer == enhedsnummer }

    return ejerAfVirksomhed

  }
  void berigDeltagersVirksomhed(String deltagerEnhedsnummer, Vrvirksomhed virksomhed, DeltagerGraf deltagerGraf,
                                int level, List<String> virksomhedsGren) {
    EjerAfVirksomhed ejerAfVirksomhed = bygEjerAfVirksomhed(virksomhed, deltagerEnhedsnummer)
    ejerAfVirksomhed.ejer.level = level

    deltagerGraf.ejere << ejerAfVirksomhed
    deltagerGraf.relationer << new DeltagerRelation(deltagerEnhedsnummer, virksomhed.enhedsNummer, ejerAfVirksomhed.ejer.andelInterval)

    // Der skal findes alle de virksomheder som har virkomsheden som deltage relation og dernæst findes de virksomheder som faktisk har
    // en ejerrelation til virksomheden  ved at kigge i hver virksomheds ejere og holde det op i mod enhedsnummeret på virksomheden.
    // elastic search er jo altid komplette, så man kan ikke bare få de "relevante" deltagere ud altså ejerne
    // det betyder også at denne graf kunne laves om til at køre rent på relationer og ikke forholde sig til ejerskab og på den måde kunne man danne sig et
    // komplet overblik over en person engagement
    List<Vrvirksomhed> virksomhederDerEjesAfVirksomhed = cvrClient.hentVirksomhedsDeltagere(virksomhed.enhedsNummer)?.
      findAll {it.aktuelleEjere?.find { it.enhedsnummer == virksomhed.enhedsNummer}}

    virksomhedsGren << virksomhed.cvrNummer
    virksomhederDerEjesAfVirksomhed.each { ejetVirksomhed->
      if (virksomhedsGren.contains(ejetVirksomhed.cvrNummer)) {
        logger.warn("Virksomheden $ejetVirksomhed.virksomhedMetadata.nyesteNavn.navn med cvrnummer : $ejetVirksomhed.cvrNummer" +
          " ejer direkte eller indirekte $virksomhed.virksomhedMetadata.nyesteNavn.navn med cvrnummer : $virksomhed.cvrNummer ")
        return
      }
      berigDeltagersVirksomhed(virksomhed.enhedsNummer, ejetVirksomhed, deltagerGraf, level+1, virksomhedsGren)
    }
  }

  /**
   *
   * @param vrvirksomhed virksomhed hvis ejere skal traverseres
   * @param ejerGraf grafen der skal beriges
   * @param virksomhed modervirksomheden til vrvirksomheden
   */
  void berigEjergraf(Vrvirksomhed vrvirksomhed, EjerGraf ejerGraf, EjerAfVirksomhed virksomhed, int level) {

    vrvirksomhed.aktuelleEjere.each { ejer->
      if (ejer.forretningsnoegle && ejer.forretningsnoegle == vrvirksomhed.cvrNummer) {
        // skip den findes jo givetvis allerede
        return
      }
      EjerAfVirksomhed ejerAfVirksomhed = new EjerAfVirksomhed()
      ejerAfVirksomhed.cvrnummer = vrvirksomhed.cvrNummer
      ejerAfVirksomhed.virksomhedsnavn = vrvirksomhed.virksomhedMetadata.nyesteNavn.navn
      ejerAfVirksomhed.ejer = ejer
      ejerAfVirksomhed.ejer.level = level
      ejerGraf.ejere << ejerAfVirksomhed
      EjerRelation ejerRelation = new EjerRelation()
      ejerRelation.virksomhed = virksomhed
      ejerRelation.ejer = ejerAfVirksomhed

      List<LegalEjerandel> moderEjere = virksomhed.ejer.reelleEjerandele
      ejer.reelleEjerandele = beregnReelleEjerAndele(moderEjere, ejer, ejerAfVirksomhed.cvrnummer, ejerAfVirksomhed.virksomhedsnavn)

      ejerGraf.ejerRelationer << ejerRelation

      if (ejer.ejertype != EjerType.PERSON) {
        Vrvirksomhed v = cvrClient.hentVirksomhed(ejer.forretningsnoegle)
        if (v && v.cvrNummer != vrvirksomhed.cvrNummer) {
          berigEjergraf(v, ejerGraf, ejerAfVirksomhed, level+1)
        }
      }
    }
  }

  /**
   * Beregner ejerandel, ved at kigge på moderselskabets og moderens moder osv osv, og derved beregne sin andel i hver virksomhed
   * @param re ejerandele fra moderselskaber
   * @param ejer ejer der skal beregnes ud fra
   * @param cvrnr cvnr på ejerens umiddelbare ejerskab
   * @param virksomhedsnavn virksomhedsnavn på  ejerens umiddelbare ejerskab
   * @return
   */
  List<LegalEjerandel> beregnReelleEjerAndele(List<LegalEjerandel> re, Ejer ejer, String cvrnr, String virksomhedsnavn) {
    List<LegalEjerandel> reelleEjerandele = []
    re.each { r->
      reelleEjerandele << beregnLegalEjerandel(ejer, r, r.virksomhedsnavn, r.cvrnummer)
    }
    reelleEjerandele << beregnLegalEjerandel(ejer, null,virksomhedsnavn, cvrnr)
  }

  /**
   * Denne metode beregner en ejerandel ud fra den ejerandel virksomheden / eller personen direkte har i en given virksomhed
   * @param ejer
   * @param vrvirksomhed
   * @return
   */
  LegalEjerandel beregnLegalEjerandel(Ejer ejer, LegalEjerandel reelEjerandel, String virksomhedsnavn, String cvrnr) {
    LegalEjerandel beregnetReelEjerandel = new LegalEjerandel()
    beregnetReelEjerandel.cvrnummer = cvrnr
    beregnetReelEjerandel.virksomhedsnavn = virksomhedsnavn
    beregnetReelEjerandel.kapitalklasse = ejer.kapitalklasse
    beregnetReelEjerandel.andel = reelEjerandel ? beregn(reelEjerandel.andel, ejer.andel) : ejer.andel
    beregnetReelEjerandel.stemmeprocent = reelEjerandel ? beregn(reelEjerandel.stemmeprocent, ejer.stemmeprocent) : ejer.stemmeprocent
    beregnetReelEjerandel.stemmeprocentInterval = Ejer.interval(beregnetReelEjerandel.stemmeprocent)
    beregnetReelEjerandel.andelInterval = Ejer.interval(beregnetReelEjerandel.andel)
    return beregnetReelEjerandel
  }

  String beregn(String re, String e)  {
    double a1 = Double.valueOf(re)
    double a2 = Double.valueOf(e)
    double resultat = a1*a2
    return String.valueOf(resultat)
  }

  /**
   * Omdanner en deltager til en kortere deltagere søgeresultat form hvor man direkte se relevante oplysninger om deltageren
   * @param vrdeltagerperson
   * @return
   */
  DeltagerSoegeresultat tilDeltager(Vrdeltagerperson vrdeltagerperson) {
    DeltagerSoegeresultat deltagerSoegeresultat = new DeltagerSoegeresultat()
    deltagerSoegeresultat.navn = vrdeltagerperson.navne[0].navn
    if (vrdeltagerperson.deltagerpersonMetadata.nyesteBeliggenhedsadresse) {
      deltagerSoegeresultat.adresselinie = vrdeltagerperson.deltagerpersonMetadata.nyesteBeliggenhedsadresse.vejadresselinie
      deltagerSoegeresultat.bylinie = vrdeltagerperson.deltagerpersonMetadata.nyesteBeliggenhedsadresse.byLinje
      deltagerSoegeresultat.postnr = vrdeltagerperson.deltagerpersonMetadata.nyesteBeliggenhedsadresse.postnummer
      deltagerSoegeresultat.bynavn = vrdeltagerperson.deltagerpersonMetadata.nyesteBeliggenhedsadresse.bynavn
      deltagerSoegeresultat.fritekst = vrdeltagerperson.deltagerpersonMetadata.nyesteBeliggenhedsadresse.fritekst
      deltagerSoegeresultat.bogstavFra = vrdeltagerperson.deltagerpersonMetadata.nyesteBeliggenhedsadresse.bogstavFra
      deltagerSoegeresultat.bogstavTil = vrdeltagerperson.deltagerpersonMetadata.nyesteBeliggenhedsadresse.bogstavTil
    }

    deltagerSoegeresultat.enhedsNummer = vrdeltagerperson.enhedsNummer
    deltagerSoegeresultat.enhedstype = vrdeltagerperson.enhedstype
    deltagerSoegeresultat.virksomheder = []
    deltagerSoegeresultat.adresser = vrdeltagerperson.beliggenhedsadresse
    vrdeltagerperson.virksomhedSummariskRelation.each {vsr->
      // arbejder p.t. kun med aktuelle data, så der skal være et livsforløb hvor gyldigtil er null
      if (vsr.virksomhed.livsforloeb.find{ !it.periode.gyldigTil}) {
        DeltagerVirksomhed dv = lavDeltagerVirksomhed(vsr)
        if (dv) {
          deltagerSoegeresultat.virksomheder << dv
        }
      }
    }
    return deltagerSoegeresultat
  }

  /**
   * Samler basis informationer om en virksomheds relation
   * @param virksomhedSummariskRelation
   * @return
   */
  DeltagerVirksomhed lavDeltagerVirksomhed(VirksomhedSummariskRelation virksomhedSummariskRelation) {
    DeltagerVirksomhed deltagerVirksomhed = new DeltagerVirksomhed()

    deltagerVirksomhed.cvrnr = virksomhedSummariskRelation.virksomhed.cvrNummer
    deltagerVirksomhed.enhedsNummer = virksomhedSummariskRelation.virksomhed.enhedsNummer

    Navn virksomhedsnavn = virksomhedSummariskRelation.virksomhed.navne.find { it->
      it.periode.gyldigTil == null
    }

    Virksomhedsstatus virksomhedsstatus = virksomhedSummariskRelation.virksomhed.virksomhedsstatus.find {!it.periode.gyldigTil}
    if (virksomhedsstatus) {
      deltagerVirksomhed.status = virksomhedsstatus.status
    } else {
      deltagerVirksomhed.status = 'AKTIV'
    }

    if (virksomhedsnavn) {
      deltagerVirksomhed.navn = virksomhedsnavn.navn
    }


    List<String> roller = hentRoller(virksomhedSummariskRelation.organisationer)

    // hvis der ingen roller er så kan vi lige så godt stoppe her.
    if (!roller) {
      return null
    }

    roller = konverterRoller(roller)

    deltagerVirksomhed.roller = roller.join(", ")
    deltagerVirksomhed.rolleliste = roller

    // ejerandele
    List<Medlemsdata> ejerdata = findAktuelleEjerMedlemsdata(virksomhedSummariskRelation.organisationer)
    if (ejerdata) {
      Medlemsdata e = ejerdata[0]
      deltagerVirksomhed.ejerandel = Ejer.findAktuelVaerdi(e, "EJERANDEL_PROCENT")
      deltagerVirksomhed.stemmeret = Ejer.findAktuelVaerdi(e, "EJERANDEL_STEMMERET_PROCENT")
      deltagerVirksomhed.ejerandeliprocent = Ejer.interval(deltagerVirksomhed.ejerandel)
      deltagerVirksomhed.stemmeretiprocent = Ejer.interval(deltagerVirksomhed.stemmeret)
    }

    return deltagerVirksomhed

  }

  private List<String> hentRoller(List<Organisation> organisationer) {
    // skal finde de roller hvor personen er aktiv
    List<Organisation> aktiveOrganisationer = organisationer.findAll {
      // find funktion og check udløbsdatoen
      Medlemsdata medlemsdata = it.medlemsData.find { m ->
        m.attributter.find { attribut ->
          attribut.type == 'FUNKTION' &&
            attribut.vaerdier.find { vaerdi ->
              vaerdi.periode.gyldigTil == null
            }
        }
      }
      // hvis den fandtes er personen aktiv i rollen
      if (medlemsdata) {
        return true
      }

      return false
    }

    List<String> roller = aktiveOrganisationer.collect { it ->
      return it.organisationsNavn.find { !it.periode.gyldigTil }?.navn
    }
    roller
  }

  List<String> konverterRoller(List<String> roller) {
    return roller.collect { it->
      if (it.endsWith("er")) {
        return it.substring(0,it.length()-2)
      }
      if (it == 'EJERREGISTER') {
        return 'Ejer'
      }

      return it
    }.findAll {it != 'Reelle ejere'}
  }

  List<Medlemsdata> findAktuelleEjerMedlemsdata(List<Organisation> organisationer) {
    Organisation ejerOrganisation = organisationer.find {it.organisationsNavn.find{it.periode.gyldigTil==null}.navn == 'EJERREGISTER'}
    if (ejerOrganisation) {
      return ejerOrganisation.medlemsData
    }

    return null
  }
}
