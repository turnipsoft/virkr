package dk.ts.virkr.aarsrapporter.parser.berigelse

import dk.ts.virkr.aarsrapporter.model.RegnskabData

/**
 * Created by sorenhartvig on 25/06/2017.
 */
class NettoresultatBerigelse {

  boolean berigNoegletal(RegnskabData rd) {
    boolean harBeriget = false

    if (berigNettoresultat(rd)) {
      harBeriget = true
    }

    return harBeriget
  }

  boolean berigNettoresultat(RegnskabData regnskabData) {

    // man kan berige driftsresultat hvis man har resultatførskat, ved at modregne finansielle indtægter og udgifter

    if (!regnskabData.resultatopgoerelse.nettoresultatTal.driftsresultat &&
        regnskabData.resultatopgoerelse.aaretsresultatTal.resultatfoerskat) {
      long resultat = regnskabData.resultatopgoerelse.aaretsresultatTal.resultatfoerskat +
        (regnskabData.resultatopgoerelse.nettoresultatTal.finansielleomkostninger?:0)
      resultat-=(regnskabData.resultatopgoerelse.nettoresultatTal.finansielleindtaegter?:0)
      regnskabData.resultatopgoerelse.nettoresultatTal.driftsresultat = resultat
      return true
    }
  }
}
