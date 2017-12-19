package dk.ts.virkr.aarsrapporter.model

import dk.ts.virkr.aarsrapporter.db.Regnskabsdata
import org.springframework.beans.BeanUtils

/**
 * Created by sorenhartvig on 24/06/2017.
 */
class NettoresultatTal extends ModelBase {
  Regnskabstal driftsresultat
  Regnskabstal finansielleomkostninger
  Regnskabstal finansielleindtaegter
  Regnskabstal kapitalandeleiassocieredevirksomheder
  Regnskabstal kapitalandeleitilknyttedevirksomheder

  static NettoresultatTal from(Regnskabsdata rd) {
    NettoresultatTal nettoresultat = new NettoresultatTal()
    nettoresultat.driftsresultat = new Regnskabstal(rd.driftsresultat)
    nettoresultat.finansielleindtaegter = new Regnskabstal(rd.finansielleindtaegter)
    nettoresultat.finansielleomkostninger = new Regnskabstal(rd.finansielleomkostninger)
    nettoresultat.kapitalandeleiassocieredevirksomheder = new Regnskabstal(rd.kapitalandeleiassocieredevirksomheder)
    return nettoresultat
  }

  void berig(Regnskabsdata rd) {
    rd.driftsresultat = this.driftsresultat.vaerdi
    rd.finansielleindtaegter = this.finansielleindtaegter.vaerdi
    rd.finansielleomkostninger = this.finansielleomkostninger.vaerdi
    rd.kapitalandeleiassocieredevirksomheder = this.kapitalandeleiassocieredevirksomheder.vaerdi
  }
}
