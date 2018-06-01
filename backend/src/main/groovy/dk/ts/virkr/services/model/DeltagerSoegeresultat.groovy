package dk.ts.virkr.services.model

import dk.ts.virkr.cvr.integration.model.virksomhed.Beliggenhedsadresse

/**
 * Created by sorenhartvig on 06/10/2017.
 */
class DeltagerSoegeresultat {
  String navn
  String adresselinie
  String bylinie
  String postnr
  String bynavn
  String enhedstype
  String enhedsNummer
  String fritekst
  String bogstavFra
  String bogstavTil

  List<Beliggenhedsadresse> adresser
  List<DeltagerVirksomhed> virksomheder

  String getVirksomhedsliste() {
    if (virksomheder) {
      return virksomheder.collect { virksomhed ->
        return "$virksomhed.navn ($virksomhed.roller)"
      }.join(', ')
    }

    return ''
  }

  String getAdresseTekst() {
    if (adresselinie && postnr) {
      return adresselinie + ', '+postnr +' '+bynavn
    }

    return ''
  }
}
