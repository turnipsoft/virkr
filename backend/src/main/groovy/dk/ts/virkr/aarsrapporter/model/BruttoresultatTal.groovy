package dk.ts.virkr.aarsrapporter.model

import dk.ts.virkr.aarsrapporter.db.Regnskabsdata
import org.springframework.beans.BeanUtils

/**
 * Created by sorenhartvig on 24/06/2017.
 */
class BruttoresultatTal extends ModelBase {
  Regnskabstal bruttofortjeneste
  Regnskabstal medarbejderomkostninger
  Regnskabstal administrationsomkostninger
  Regnskabstal ejendomsomkostninger
  Regnskabstal regnskabsmaessigeafskrivninger //DepreciationAmortisationExpenseAndImpairmentLossesOfPropertyPlantAndEquipmentAndIntangibleAssetsRecognisedInProfitOrLoss
  Regnskabstal kapitalandeleiassocieredevirksomheder

  static BruttoresultatTal from(Regnskabsdata rd) {
    BruttoresultatTal br = new BruttoresultatTal()

    br.bruttofortjeneste = new Regnskabstal(rd.bruttofortjeneste)
    br.medarbejderomkostninger = new Regnskabstal(rd.medarbejderomkostninger)
    br.administrationsomkostninger = new Regnskabstal(rd.administrationsomkostninger)
    br.ejendomsomkostninger = new Regnskabstal(rd.lokalomkostninger)
    br.regnskabsmaessigeafskrivninger = new Regnskabstal(rd.regnskabsmaessigeafskrivninger)
    br.kapitalandeleiassocieredevirksomheder = new Regnskabstal(rd.kapitalandeleiassocieredevirksomheder)
    return br
  }

  void berig(Regnskabsdata br) {
    br.bruttofortjeneste = this.bruttofortjeneste.vaerdi
    br.medarbejderomkostninger = this.medarbejderomkostninger.vaerdi
    br.administrationsomkostninger = this.administrationsomkostninger.vaerdi
    br.lokalomkostninger = this.ejendomsomkostninger.vaerdi
    br.regnskabsmaessigeafskrivninger = this.regnskabsmaessigeafskrivninger.vaerdi
    br.kapitalandeleiassocieredevirksomheder = this.kapitalandeleiassocieredevirksomheder.vaerdi
  }

}
