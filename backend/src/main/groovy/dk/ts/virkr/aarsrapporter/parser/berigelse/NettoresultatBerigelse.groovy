package dk.ts.virkr.aarsrapporter.parser.berigelse

import dk.ts.virkr.aarsrapporter.model.Regnskab
import dk.ts.virkr.aarsrapporter.model.RegnskabData

/**
 * Created by sorenhartvig on 25/06/2017.
 */
class NettoresultatBerigelse extends Berigelse {

  boolean berigNoegletal(Regnskab rd) {
    boolean harBeriget = false

    if (berigNettoresultat(rd)) {
      harBeriget = true
    }

    return harBeriget
  }

  boolean berigNettoresultat(Regnskab regnskabData) {

    // man kan berige driftsresultat hvis man har resultatførskat, ved at modregne finansielle indtægter og udgifter

    if (!regnskabData.resultatopgoerelse.nettoresultatTal.driftsresultat.vaerdi &&
        regnskabData.resultatopgoerelse.aaretsresultatTal.resultatfoerskat.vaerdi) {
      long resultat = regnskabData.resultatopgoerelse.aaretsresultatTal.resultatfoerskat.vaerdi +
        val(regnskabData.resultatopgoerelse.nettoresultatTal.finansielleomkostninger)

      resultat-=val(regnskabData.resultatopgoerelse.nettoresultatTal.finansielleindtaegter)

      regnskabData.resultatopgoerelse.nettoresultatTal.driftsresultat.vaerdi = resultat
      return true
    }
  }
}
