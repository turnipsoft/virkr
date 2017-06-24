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

  public static AaretsresultatTal from(Regnskabsdata rd) {
    AaretsresultatTal aar = new AaretsresultatTal()
    BeanUtils.copyProperties(rd, aar)
    return aar
  }

}
