package dk.ts.virkr.services.model.graf

import dk.ts.virkr.cvr.integration.model.virksomhed.Vrvirksomhed

/**
 * Created by sorenhartvig on 03/07/2017.
 */
class EjerGraf {

  Vrvirksomhed virksomhed

  List<EjerAfVirksomhed> ejere = []
  List<EjerRelation> ejerRelationer = []

  List<EjerAfVirksomhed> getUnikkeEjere() {
    List<String> unikkeEjerId = []
    List<EjerAfVirksomhed> unikkeEjere = []

    ejere.each { ejer->
      if (!unikkeEjerId.contains(ejer.ejer.enhedsnummer) && !unikkeEjerId.contains(ejer.ejer.enhedsnummer)) {
        unikkeEjerId << ejer.ejer.enhedsnummer
        unikkeEjere << ejer
      }
    }

    return unikkeEjere
  }
}
