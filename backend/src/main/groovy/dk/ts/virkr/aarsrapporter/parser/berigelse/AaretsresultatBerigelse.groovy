package dk.ts.virkr.aarsrapporter.parser.berigelse

import dk.ts.virkr.aarsrapporter.db.Regnskabsdata
import dk.ts.virkr.aarsrapporter.model.AaretsresultatTal
import dk.ts.virkr.aarsrapporter.model.RegnskabData

/**
 * Created by sorenhartvig on 25/06/2017.
 */
class AaretsresultatBerigelse {

  boolean berigNoegletal(RegnskabData rd) {
    boolean harBeriget = false

    if (berigResultatFoerskat(rd.resultatopgoerelse.aaretsresultatTal)) {
      harBeriget = true
    }

    return harBeriget
  }

  boolean berigResultatFoerskat(AaretsresultatTal aaretsresultatTal) {
    // Der er ikke noget resultat før skat, men det kan findes ved at trække skatten af årets resultat fra årets resultat
    if (!aaretsresultatTal.resultatfoerskat &&
         aaretsresultatTal.skatafaaretsresultat &&
         aaretsresultatTal.aaretsresultat) {
      aaretsresultatTal.resultatfoerskat = aaretsresultatTal.aaretsresultat + aaretsresultatTal.skatafaaretsresultat
      return true
    }

    return false
  }

}
