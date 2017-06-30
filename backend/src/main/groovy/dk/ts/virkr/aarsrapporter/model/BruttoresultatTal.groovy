package dk.ts.virkr.aarsrapporter.model

import dk.ts.virkr.aarsrapporter.db.Regnskabsdata
import org.springframework.beans.BeanUtils

/**
 * Created by sorenhartvig on 24/06/2017.
 */
class BruttoresultatTal extends ModelBase {
  Long bruttofortjeneste
  Long medarbejderomkostninger
  Long administrationsomkostninger
  Long ejendomsomkostninger
  Long regnskabsmaessigeafskrivninger //DepreciationAmortisationExpenseAndImpairmentLossesOfPropertyPlantAndEquipmentAndIntangibleAssetsRecognisedInProfitOrLoss
  Long kapitalandeleiassocieredevirksomheder

  static BruttoresultatTal from(Regnskabsdata rd) {
    BruttoresultatTal br = new BruttoresultatTal()

    br.bruttofortjeneste = rd.bruttofortjeneste
    br.medarbejderomkostninger = rd.medarbejderomkostninger
    br.administrationsomkostninger = rd.administrationsomkostninger
    br.ejendomsomkostninger = rd.lokalomkostninger
    br.regnskabsmaessigeafskrivninger = rd.regnskabsmaessigeafskrivninger
    br.kapitalandeleiassocieredevirksomheder = rd.kapitalandeleiassocieredevirksomheder
    return br
  }

  void berig(Regnskabsdata br) {
    br.bruttofortjeneste = this.bruttofortjeneste
    br.medarbejderomkostninger = this.medarbejderomkostninger
    br.administrationsomkostninger = this.administrationsomkostninger
    br.lokalomkostninger = this.ejendomsomkostninger
    br.regnskabsmaessigeafskrivninger = this.regnskabsmaessigeafskrivninger
    br.kapitalandeleiassocieredevirksomheder = this.kapitalandeleiassocieredevirksomheder
  }

}
