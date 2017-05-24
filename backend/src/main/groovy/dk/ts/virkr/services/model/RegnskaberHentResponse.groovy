package dk.ts.virkr.services.model

import dk.ts.virkr.aarsrapporter.integration.model.regnskabdata.RegnskabData

/**
 * Created by sorenhartvig on 22/06/16.
 */
class RegnskaberHentResponse {
    List<RegnskabData> regnskabsdata
    String cvrNummer
}
