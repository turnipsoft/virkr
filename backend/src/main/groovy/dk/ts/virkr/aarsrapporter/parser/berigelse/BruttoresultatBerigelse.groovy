package dk.ts.virkr.aarsrapporter.parser.berigelse

import dk.ts.virkr.aarsrapporter.model.Regnskab
import dk.ts.virkr.aarsrapporter.model.Resultatopgoerelse

/**
 * Created by sorenhartvig on 25/06/2017.
 */
class BruttoresultatBerigelse extends Berigelse {

  boolean berigNoegletal(Regnskab rd) {
    boolean harBeriget = false

    if (berigBruttoresulat(rd)) {
      harBeriget = true
    }

    return harBeriget
  }

  boolean berigBruttoresulat(Regnskab rd) {
    Resultatopgoerelse ro = rd.resultatopgoerelse

    if (!ro.bruttoresultatTal.bruttofortjeneste.vaerdi &&
         ro.nettoresultatTal.driftsresultat.vaerdi) {
      long resultat = ro.nettoresultatTal.driftsresultat.vaerdi + val(ro.bruttoresultatTal.administrationsomkostninger)
      resultat += val(ro.bruttoresultatTal.ejendomsomkostninger)
      resultat += val(ro.bruttoresultatTal.medarbejderomkostninger)
      resultat += val(ro.bruttoresultatTal.regnskabsmaessigeafskrivninger)
      resultat -= val(ro.bruttoresultatTal.kapitalandeleiassocieredevirksomheder)

      ro.bruttoresultatTal.bruttofortjeneste.vaerdi = resultat

      return true
    }
  }
}
