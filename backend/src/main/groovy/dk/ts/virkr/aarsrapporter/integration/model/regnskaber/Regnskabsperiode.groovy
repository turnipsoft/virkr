package dk.ts.virkr.aarsrapporter.integration.model.regnskaber

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Created by sorenhartvig on 22/06/16.
 */
@JsonIgnoreProperties(['startDatoDate','slutDatoDate'])
class Regnskabsperiode {
    String startDatoDate
    String slutDatoDate
    String startDato
    String slutDato
}
