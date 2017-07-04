package dk.ts.virkr.services.internal

import dk.ts.virkr.cvr.integration.CvrClient
import dk.ts.virkr.cvr.integration.model.virksomhed.Ejer
import dk.ts.virkr.cvr.integration.model.virksomhed.EjerAfVirksomhed
import dk.ts.virkr.cvr.integration.model.virksomhed.EjerGraf
import dk.ts.virkr.cvr.integration.model.virksomhed.EjerRelation
import dk.ts.virkr.cvr.integration.model.virksomhed.EjerType
import dk.ts.virkr.cvr.integration.model.virksomhed.Vrvirksomhed
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by sorenhartvig on 03/07/2017.
 */
@Service
class CvrInternalService {

  @Autowired
  CvrClient cvrClient

  EjerGraf hentEjergraf(String cvrnummer) {
    EjerGraf ejerGraf = new EjerGraf()
    Vrvirksomhed vrvirksomhed = cvrClient.hentVirksomhed(cvrnummer)

    EjerAfVirksomhed ejerAfVirksomhed = new EjerAfVirksomhed()
    ejerAfVirksomhed.cvrnummer = vrvirksomhed.cvrNummer
    ejerAfVirksomhed.virksomhedsnavn = vrvirksomhed.virksomhedMetadata.nyesteNavn.navn
    ejerAfVirksomhed.ejer = new Ejer()

    // Pseudo ejer for at have øverste niveau med ud, blive muligvis recactored så man mere eksplicit sætter roden
    ejerAfVirksomhed.ejer.navn = ejerAfVirksomhed.virksomhedsnavn
    ejerAfVirksomhed.ejer.forretningsnoegle = ejerAfVirksomhed.cvrnummer
    ejerAfVirksomhed.ejer.enhedsnummer = vrvirksomhed.enhedsNummer
    ejerAfVirksomhed.ejer.ejertype = EjerType.ROD

    ejerGraf.ejere << ejerAfVirksomhed

    berigEjergraf(vrvirksomhed, ejerGraf, ejerAfVirksomhed)

    return ejerGraf
  }

  void berigEjergraf(Vrvirksomhed vrvirksomhed, EjerGraf ejerGraf, EjerAfVirksomhed virksomhed) {

    vrvirksomhed.ejere.each { ejer->
      EjerAfVirksomhed ejerAfVirksomhed = new EjerAfVirksomhed()
      ejerAfVirksomhed.cvrnummer = vrvirksomhed.cvrNummer
      ejerAfVirksomhed.virksomhedsnavn = vrvirksomhed.virksomhedMetadata.nyesteNavn.navn
      ejerAfVirksomhed.ejer = ejer
      ejerGraf.ejere << ejerAfVirksomhed
      EjerRelation ejerRelation = new EjerRelation()
      ejerRelation.virksomhed = virksomhed
      ejerRelation.ejer = ejerAfVirksomhed
      ejerGraf.ejerRelationer << ejerRelation
      if (ejer.ejertype != EjerType.PERSON) {
        Vrvirksomhed v = cvrClient.hentVirksomhed(ejer.forretningsnoegle)
        if (v) {
          berigEjergraf(v, ejerGraf, ejerAfVirksomhed)
        }
      }
    }
  }


}
