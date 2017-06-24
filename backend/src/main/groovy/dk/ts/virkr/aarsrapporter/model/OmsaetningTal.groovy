package dk.ts.virkr.aarsrapporter.model

import dk.ts.virkr.aarsrapporter.db.Regnskabsdata
import org.springframework.beans.BeanUtils

/**
 * Created by sorenhartvig on 24/06/2017.
 */
class OmsaetningTal extends ModelBase {
  Long omsaetning
  Long vareforbrug  // CostOfSales
  Long driftsindtaegter // OtherOperatingIncome
  Long andreeksterneOmkostninger //OtherExternalExpenses
  Long variableomkostninger // RawMaterialsAndConsumablesUsed
  Long lokalomkostninger
  Long eksterneomkostninger
  Long ejendomsomkostninger
  Long vaerdiregulering

  public static OmsaetningTal from(Regnskabsdata rd) {
    OmsaetningTal oms = new OmsaetningTal()
    BeanUtils.copyProperties(rd, oms)
    return oms
  }

}
