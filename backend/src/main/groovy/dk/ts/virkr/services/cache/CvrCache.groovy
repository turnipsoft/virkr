package dk.ts.virkr.services.cache

import dk.ts.virkr.services.model.graf.DeltagerGraf
import dk.ts.virkr.services.model.graf.EjerGraf

/**
 * Created by shartvig2 on 1/24/18.
 */
interface CvrCache {
  DeltagerGraf hentDeltagerGraf(String enhedsnummer)

  void gemDeltagerGraf(String enhedsnummer, DeltagerGraf deltagerGraf)

  EjerGraf hentEjerGraf(String cvrnr)

  void gemEjerGraf(String cvrnr, EjerGraf ejerGraf)

}
