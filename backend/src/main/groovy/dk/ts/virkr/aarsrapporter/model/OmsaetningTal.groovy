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
  Long andreeksterneomkostninger //OtherExternalExpenses
  Long variableomkostninger // RawMaterialsAndConsumablesUsed
  Long lokalomkostninger
  Long eksterneomkostninger
  Long ejendomsomkostninger
  Long vaerdiregulering

  static OmsaetningTal from(Regnskabsdata rd) {
    OmsaetningTal oms = new OmsaetningTal()
    oms.omsaetning = rd.omsaetning
    oms.vareforbrug = rd.vareforbrug
    oms.driftsindtaegter = rd.driftsindtaegter
    oms.andreeksterneomkostninger = rd.andreeksterneomkostninger
    oms.variableomkostninger = rd.variableomkostninger
    oms.lokalomkostninger = rd.lokalomkostninger
    oms.eksterneomkostninger = rd.eksterneomkostninger
    return oms
  }

  void berig(Regnskabsdata oms) {
    oms.omsaetning = this.omsaetning
    oms.vareforbrug = this.vareforbrug
    oms.driftsindtaegter = this.driftsindtaegter
    oms.andreeksterneomkostninger = this.andreeksterneomkostninger
    oms.variableomkostninger = this.variableomkostninger
    oms.lokalomkostninger = this.lokalomkostninger
    oms.eksterneomkostninger = this.eksterneomkostninger
  }

}
