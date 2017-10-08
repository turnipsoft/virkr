package dk.ts.virkr.cvr.integration.model.deltager

import dk.ts.virkr.cvr.integration.model.virksomhed.Organisation
import dk.ts.virkr.cvr.integration.model.virksomhed.Vrvirksomhed

/**
 * Created by sorenhartvig on 06/10/2017.
 */
class VirksomhedSummariskRelation {
  Vrvirksomhed virksomhed
  List<Organisation> organisationer

}
