package dk.ts.virkr.aarsrapporter.parser.berigelse

import dk.ts.virkr.aarsrapporter.model.RegnskabData
import dk.ts.virkr.aarsrapporter.model.Resultatopgoerelse

/**
 * Created by sorenhartvig on 25/06/2017.
 */
class BruttoresultatBerigelse {

  boolean berigNoegletal(RegnskabData rd) {
    boolean harBeriget = false

    if (berigBruttoresulat(rd)) {
      harBeriget = true
    }

    return harBeriget
  }

  boolean berigBruttoresulat(RegnskabData rd) {
    Resultatopgoerelse ro = rd.resultatopgoerelse

    if (!ro.bruttoresultatTal.bruttofortjeneste &&
         ro.nettoresultatTal.driftsresultat) {
      long resultat = ro.nettoresultatTal.driftsresultat + (ro.bruttoresultatTal.administrationsomkostninger?:0)
      resultat += (ro.bruttoresultatTal.ejendomsomkostninger?:0)
      resultat += (ro.bruttoresultatTal.medarbejderomkostninger?:0)
      resultat += (ro.bruttoresultatTal.regnskabsmaessigeafskrivninger?:0)

      ro.bruttoresultatTal.bruttofortjeneste = resultat

      return true
    }
  }
}
