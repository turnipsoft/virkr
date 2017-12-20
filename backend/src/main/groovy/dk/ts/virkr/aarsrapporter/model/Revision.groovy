package dk.ts.virkr.aarsrapporter.model

import dk.ts.virkr.cvr.integration.model.virksomhed.Beliggenhedsadresse

/**
 * Created by sorenhartvig on 17/12/2017.
 */
class Revision {
  String revisionsfirmaNavn
  String revisionsfirmaCvrnummer
  String navnPaaRevisor
  String beskrivelseAfRevisor
  Beliggenhedsadresse beliggenhedsadresse
  String telefonnummer
  String email
  String regnskabsklasse
  String assistancetype
  String adressant
  String revisionUnderskriftsted
  String revisionUnderskriftdato
  String godkendelsesdato
  String mnenummer

  String ingenRevision
  String supplerendeInformationOmAndreForhold
  String supplerendeInformationOmAarsrapport
  String grundlagForKonklusion
  String konklusionMedForbehold
  String supplerendeInformationOmRevision
  String vaesentligUsikkerhedVedrFortsatDrift

  Generalforsamling generalforsamling

  List<String> direktion = []
  List<String> bestyrelse = []

  List<String> getUnderskrivere() {
    List<String> alle = []
    alle.addAll(direktion)
    alle.addAll(bestyrelse)

    return alle.sort{it}.unique{it}
  }
}
