package dk.ts.virkr.aarsrapporter.parser.berigelse

import dk.ts.virkr.aarsrapporter.model.RegnskabData
import dk.ts.virkr.aarsrapporter.model.Resultatopgoerelse

/**
 * Created by sorenhartvig on 25/06/2017.
 */
class BruttoresultatBerigelse extends Berigelse {

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
      long resultat = ro.nettoresultatTal.driftsresultat + val(ro.bruttoresultatTal.administrationsomkostninger)
      resultat += val(ro.bruttoresultatTal.ejendomsomkostninger)
      resultat += val(ro.bruttoresultatTal.medarbejderomkostninger)
      resultat += val(ro.bruttoresultatTal.regnskabsmaessigeafskrivninger)
      resultat -= val(ro.bruttoresultatTal.kapitalandeleiassocieredevirksomheder)

      ro.bruttoresultatTal.bruttofortjeneste = resultat

      return true
    }
  }
}
