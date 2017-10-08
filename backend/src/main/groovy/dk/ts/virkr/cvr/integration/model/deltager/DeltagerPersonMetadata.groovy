package dk.ts.virkr.cvr.integration.model.deltager

import dk.ts.virkr.cvr.integration.model.virksomhed.Beliggenhedsadresse
import dk.ts.virkr.cvr.integration.model.virksomhed.Navn

/**
 * Created by sorenhartvig on 06/10/2017.
 */
class DeltagerPersonMetadata {
  Beliggenhedsadresse nyesteBeliggenhedsadresse
  Navn nyesteNavn
  List<String> nyesteKontaktoplysninger
}
