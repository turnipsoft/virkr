package dk.ts.virkr.cvr.integration.model.virksomhed

import dk.ts.virkr.aarsrapporter.util.Utils

/**
 * Created by sorenhartvig on 01/07/2017.
 */
class DeltagerRelation {
  Deltager deltager
  List<Organisation> organisationer

  String getNyesteNavn() {
    if (this.deltager) {
      return Utils.findNyeste(this.deltager.navne)?.navn
    }

    return null
  }

  Beliggenhedsadresse getNyesteAdresse() {
    return this.getAdresser()? this.getAdresser().last() : null

  }

  List<Beliggenhedsadresse> getAdresser() {
    if (this.deltager) {
      return this.deltager.beliggenhedsadresse ? this.deltager.beliggenhedsadresse.sort { it.periode.gyldigFra } : null
    }
    return []
  }

  List<String> getRoller() {
    return hentRoller(this.organisationer)
  }

  String getRollerAsString() {
    return getRoller().join(", ")
  }

  private static Set<String> ledelsesRoller = ['direktion','direktør','bestyrelse','adm. direktør']

  boolean isLedelse() {
    return this.getRoller().find{ledelsesRoller.contains(it.toLowerCase())}
  }

  private List<String> hentRoller(List<Organisation> organisationer) {
    // skal finde de roller hvor personen er aktiv
    List<Organisation> aktiveOrganisationer = organisationer.findAll {
      // find funktion og check udløbsdatoen
      Medlemsdata medlemsdata = it.medlemsData.find { m ->
        m.attributter.find { attribut ->
          attribut.type == 'FUNKTION' &&
            attribut.vaerdier.find { vaerdi ->
              vaerdi.periode.gyldigTil == null
            }
        }
      }
      // hvis den fandtes er personen aktiv i rollen
      if (medlemsdata) {
        return true
      }

      return false
    }

    List<String> roller = aktiveOrganisationer.collect { it ->
      return it.organisationsNavn.find { !it.periode.gyldigTil }?.navn
    }
    roller
  }
}
