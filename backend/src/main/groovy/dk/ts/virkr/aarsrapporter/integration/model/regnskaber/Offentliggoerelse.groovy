package dk.ts.virkr.aarsrapporter.integration.model.regnskaber

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Created by sorenhartvig on 22/06/16.
 */
@JsonIgnoreProperties(['id'])
class Offentliggoerelse {
    String id
    String cvrNummer
    boolean omgoerelse
    String offentliggoerelsestype
    String sagsNr
    Regnskab regnskab
    Date offentliggoerelsesTidspunkt
    Date indlaesningsTidspunkt
    Date sidstOpdateret
    List<Dokument> dokumenter
}
