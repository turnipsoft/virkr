package dk.ts.virkr.services.cache

import dk.ts.virkr.services.model.graf.DeltagerGraf
import dk.ts.virkr.services.model.graf.EjerGraf

/**
 * Created by shartvig2 on 1/24/18.
 */
class NoOpCvrCache implements CvrCache {

  @Override
  DeltagerGraf hentDeltagerGraf(String enhedsnummer) {
    return null
  }

  @Override
  void gemDeltagerGraf(String enhedsnummer, DeltagerGraf deltagerGraf) {

  }

  @Override
  EjerGraf hentEjerGraf(String cvrnr) {
    return null
  }

  @Override
  void gemEjerGraf(String cvrnr, EjerGraf ejerGraf) {

  }
}
