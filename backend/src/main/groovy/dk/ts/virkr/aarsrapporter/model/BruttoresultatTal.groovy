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

  public static BruttoresultatTal from(Regnskabsdata rd) {
    BruttoresultatTal br = new BruttoresultatTal()
    BeanUtils.copyProperties(rd, br)
    return br
  }

}
