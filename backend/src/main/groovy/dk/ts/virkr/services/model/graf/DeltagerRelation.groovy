package dk.ts.virkr.services.model.graf

/**
 * Created by sorenhartvig on 20/10/2017.
 */
class DeltagerRelation {
  String deltagerEnhedsnummer
  String virksomhedEnhedsnummer

  DeltagerRelation(String deltagerEnhedsnummer, String virksomhedEnhedsnummer) {
    this.deltagerEnhedsnummer = deltagerEnhedsnummer
    this.virksomhedEnhedsnummer = virksomhedEnhedsnummer
  }
}
