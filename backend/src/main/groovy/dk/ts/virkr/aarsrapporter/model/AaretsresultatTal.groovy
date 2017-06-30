package dk.ts.virkr.aarsrapporter.model

import dk.ts.virkr.aarsrapporter.db.Regnskabsdata
import org.springframework.beans.BeanUtils

/**
 * Created by sorenhartvig on 24/06/2017.
 */
class AaretsresultatTal extends ModelBase {
  Long aaretsresultat
  Long resultatfoerskat
  Long skatafaaretsresultat

  static AaretsresultatTal from(Regnskabsdata rd) {
    AaretsresultatTal aar = new AaretsresultatTal()
    aar.aaretsresultat = rd.aaretsresultat
    aar.resultatfoerskat = rd.resultatfoerskat
    aar.skatafaaretsresultat = rd.skatafaaretsresultat
    return aar
  }

  void berig(Regnskabsdata rd) {
    rd.aaretsresultat = this.aaretsresultat
    rd.skatafaaretsresultat = this.skatafaaretsresultat
    rd.resultatfoerskat = this.skatafaaretsresultat
  }

}
