package dk.ts.virkr.services.model.graf

import dk.ts.virkr.services.model.EjerType

/**
 * Created by sorenhartvig on 20/10/2017.
 */
class DeltagerGraf {
  List<EjerAfVirksomhed> ejere = []
  List<DeltagerRelation> relationer = []

  List<EjerEnhed> getGrupperedeEjere() {
    Map<String, EjerEnhed> unikkeEjere = [:]
    ejere.each { ejer->
      EjerEnhed ejerEnhed = new EjerEnhed()
      if (unikkeEjere.containsKey(ejer.ejer.enhedsnummer)) {
        ejerEnhed = unikkeEjere.get(ejer.ejer.enhedsnummer)
      }
      ejerEnhed.ejedeVirksomheder << ejer
      ejerEnhed.navn = ejer.ejer.navn
      ejerEnhed.cvrnummer = ejer.ejer.forretningsnoegle
      ejerEnhed.enhedsnummer = ejer.ejer.enhedsnummer
      ejerEnhed.ejertype = ejer.ejer.ejertype
      ejerEnhed.adresse = ejer.ejer.adresse

      // skal også have leaf enheden med uagtet at denne ikke har nogle ejerskaber
      if (!unikkeEjere.containsKey(ejer.enhedsnummer)) {
        EjerEnhed leafEnhed = new EjerEnhed()
        leafEnhed.enhedsnummer = ejer.enhedsnummer
        leafEnhed.cvrnummer = ejer.cvrnummer
        leafEnhed.navn = ejer.virksomhedsnavn
        leafEnhed.ejertype = EjerType.VIRKSOMHED
        leafEnhed.adresse = ejer.adresse
        unikkeEjere.put(ejer.enhedsnummer, leafEnhed)
      }
      unikkeEjere.put(ejer.ejer.enhedsnummer, ejerEnhed)
    }

    return unikkeEjere.entrySet().collect { it.value }

  }

  List<Enhed> getUnikkeEjere() {

    Map<String, Enhed> unikkeEnheder = [:]

    ejere.each { ejer->
      if (!unikkeEnheder.containsKey(ejer.ejer.enhedsnummer)) {
        Enhed enhed = new Enhed()
        enhed.enhedsnummer = ejer.ejer.enhedsnummer
        enhed.cvrnummer = ejer.ejer.forretningsnoegle
        enhed.enhedsType = enhed.cvrnummer ? EnhedsType.VIRKSOMHED : EnhedsType.PERSON
        enhed.navn = ejer.ejer.navn
        enhed.adresse = ejer.ejer.adresse
        unikkeEnheder.put(ejer.ejer.enhedsnummer, enhed)
      }

      if (!unikkeEnheder.containsKey(ejer.enhedsnummer)) {
        Enhed enhed = new Enhed()
        enhed.enhedsnummer = ejer.enhedsnummer
        enhed.cvrnummer = ejer.cvrnummer
        enhed.enhedsType = enhed.cvrnummer ? EnhedsType.VIRKSOMHED : EnhedsType.PERSON
        enhed.navn = ejer.virksomhedsnavn
        enhed.adresse = ejer.ejer.adresse
        unikkeEnheder.put(ejer.enhedsnummer, enhed)
      }
    }

    return unikkeEnheder.entrySet().collect {it.value}
  }

}
