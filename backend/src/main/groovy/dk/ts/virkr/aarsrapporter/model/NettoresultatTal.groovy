package dk.ts.virkr.aarsrapporter.model

import dk.ts.virkr.aarsrapporter.db.Regnskabsdata
import org.springframework.beans.BeanUtils

/**
 * Created by sorenhartvig on 24/06/2017.
 */
class NettoresultatTal extends ModelBase {
  Long driftsresultat
  Long finansielleomkostninger
  Long finansielleindtaegter

  public static NettoresultatTal from(Regnskabsdata rd) {
    NettoresultatTal nettoresultat = new NettoresultatTal()
    BeanUtils.copyProperties(rd, nettoresultat)
    return nettoresultat
  }
}
