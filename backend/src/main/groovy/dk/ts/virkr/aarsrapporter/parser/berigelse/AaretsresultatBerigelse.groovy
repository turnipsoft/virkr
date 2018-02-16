package dk.ts.virkr.aarsrapporter.parser.berigelse

import dk.ts.virkr.aarsrapporter.model.AaretsresultatTal
import dk.ts.virkr.aarsrapporter.model.Regnskab

/**
 * Created by sorenhartvig on 25/06/2017.
 */
class AaretsresultatBerigelse extends Berigelse {

  boolean berigNoegletal(Regnskab rd) {
    boolean harBeriget = false

    if (berigResultatFoerskat(rd.resultatopgoerelse.aaretsresultatTal)) {
      harBeriget = true
    }

    return harBeriget
  }

  boolean berigResultatFoerskat(AaretsresultatTal aaretsresultatTal) {
    // Der er ikke noget resultat før skat, men det kan findes ved at trække skatten af årets resultat fra årets resultat
    if (!aaretsresultatTal.resultatfoerskat.vaerdi &&
         aaretsresultatTal.skatafaaretsresultat.vaerdi &&
         aaretsresultatTal.aaretsresultat.vaerdi) {
      aaretsresultatTal.resultatfoerskat.vaerdi = val(aaretsresultatTal.aaretsresultat) +
        val(aaretsresultatTal.skatafaaretsresultat)
      return true
    }

    return false
  }

}
