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

  static NettoresultatTal from(Regnskabsdata rd) {
    NettoresultatTal nettoresultat = new NettoresultatTal()
    nettoresultat.driftsresultat = rd.driftsresultat
    nettoresultat.finansielleindtaegter = rd.finansielleindtaegter
    nettoresultat.finansielleomkostninger = rd.finansielleomkostninger
    return nettoresultat
  }

  void berig(Regnskabsdata rd) {
    rd.driftsresultat = this.driftsresultat
    rd.finansielleindtaegter = this.finansielleindtaegter
    rd.finansielleomkostninger = this.finansielleomkostninger
  }
}
