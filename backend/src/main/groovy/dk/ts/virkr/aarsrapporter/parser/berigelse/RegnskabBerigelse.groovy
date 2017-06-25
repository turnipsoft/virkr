package dk.ts.virkr.aarsrapporter.parser.berigelse

import dk.ts.virkr.aarsrapporter.model.RegnskabData
import dk.ts.virkr.aarsrapporter.model.Resultatopgoerelse

/**
 * Created by sorenhartvig on 25/06/2017.
 */
class RegnskabBerigelse {

  boolean berigNoegletal(RegnskabData regnskabData) {
    boolean harBeriget = false

    ResultatopgoerelseBerigelse resultatopgoerelseBerigelse = new ResultatopgoerelseBerigelse()
    if (resultatopgoerelseBerigelse.berigNoegletal(regnskabData)) {
      harBeriget = true
    }

    return harBeriget
  }
}
