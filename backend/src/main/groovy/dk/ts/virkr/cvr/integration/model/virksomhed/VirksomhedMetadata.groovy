package dk.ts.virkr.cvr.integration.model.virksomhed

/**
 * Created by sorenhartvig on 29/05/2017.
 */
class VirksomhedMetadata {
  Branche nyesteHovedbranche
  Beliggenhedsadresse nyesteBeliggenhedsadresse
  Navn nyesteNavn
  List<String> nyesteKontaktoplysninger
  String sammensatStatus
  Beskaeftigelse nyesteAarsbeskaeftigelse
  Virksomhedsform nyesteVirksomhedsform
}
