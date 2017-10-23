package dk.ts.virkr.services.model.graf

import dk.ts.virkr.services.model.EjerType

/**
 * Created by sorenhartvig on 20/10/2017.
 */
class EjerEnhed {
  String cvrnummer
  String enhedsnummer
  String navn
  EjerType ejertype

  List<EjerAfVirksomhed> ejedeVirksomheder = []
}
