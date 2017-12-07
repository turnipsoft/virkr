package dk.ts.virkr.services.model.graf

import dk.ts.virkr.cvr.integration.model.virksomhed.Vrvirksomhed

/**
 * Created by sorenhartvig on 03/07/2017.
 */
class EjerGraf {

  Vrvirksomhed virksomhed

  List<EjerAfVirksomhed> ejere = []
  List<EjerRelation> ejerRelationer = []
}
