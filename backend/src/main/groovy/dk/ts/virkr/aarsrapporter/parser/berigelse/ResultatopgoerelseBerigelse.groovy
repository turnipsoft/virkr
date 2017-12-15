package dk.ts.virkr.aarsrapporter.parser.berigelse

import dk.ts.virkr.aarsrapporter.model.Regnskab
import dk.ts.virkr.aarsrapporter.model.RegnskabData

/**
 * Created by sorenhartvig on 25/06/2017.
 */
class ResultatopgoerelseBerigelse {

  boolean berigNoegletal(Regnskab rd) {
    boolean harBeriget = false

    AaretsresultatBerigelse aaretsresultatBerigelse = new AaretsresultatBerigelse()
    if (aaretsresultatBerigelse.berigNoegletal(rd)) {
      harBeriget = true
    }

    NettoresultatBerigelse nettoresultatBerigelse = new NettoresultatBerigelse()
    if (nettoresultatBerigelse.berigNoegletal(rd)) {
      harBeriget = true
    }

    BruttoresultatBerigelse bruttoresultatBerigelse = new BruttoresultatBerigelse()
    if (bruttoresultatBerigelse.berigNoegletal(rd)) {
      harBeriget = true
    }

    return harBeriget
  }
}
