package dk.ts.virkr.cvr.integration.model.deltager

import dk.ts.virkr.cvr.integration.model.virksomhed.Attribut
import dk.ts.virkr.cvr.integration.model.virksomhed.Beliggenhedsadresse
import dk.ts.virkr.cvr.integration.model.virksomhed.Navn

/**
 * Created by sorenhartvig on 06/10/2017.
 */
class Vrdeltagerperson {

  List<Navn> navne
  String forretningsnoegle
  List<Beliggenhedsadresse> beliggenhedsadresse
  String statusKode
  List<Attribut> attributter
  String enhedsNummer
  String enhedstype
  String sidstIndlaest
  String sidstOpdateret
  String stilling
  String virkningsAktoer
  DeltagerPersonMetadata deltagerpersonMetadata
  List<VirksomhedSummariskRelation> virksomhedSummariskRelation

  String getNavn() {
    if (navne && navne.size()>0) {
      return navne.get(0).navn
    }

    return null
  }
}
