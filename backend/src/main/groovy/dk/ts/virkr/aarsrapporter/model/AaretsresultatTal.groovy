package dk.ts.virkr.aarsrapporter.model

import dk.ts.virkr.aarsrapporter.db.Regnskabsdata
import org.springframework.beans.BeanUtils

/**
 * Created by sorenhartvig on 24/06/2017.
 */
class AaretsresultatTal extends ModelBase {
  Regnskabstal aaretsresultat
  Regnskabstal resultatfoerskat
  Regnskabstal skatafaaretsresultat

  static AaretsresultatTal from(Regnskabsdata rd) {
    AaretsresultatTal aar = new AaretsresultatTal()
    aar.aaretsresultat = new Regnskabstal(rd.aaretsresultat)
    aar.resultatfoerskat = new Regnskabstal(rd.resultatfoerskat)
    aar.skatafaaretsresultat = new Regnskabstal(rd.skatafaaretsresultat)
    return aar
  }

  void berig(Regnskabsdata rd) {
    rd.aaretsresultat = this.aaretsresultat.vaerdi
    rd.skatafaaretsresultat = this.skatafaaretsresultat.vaerdi
    rd.resultatfoerskat = this.skatafaaretsresultat.vaerdi
  }

}
