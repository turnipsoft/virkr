package dk.ts.virkr.aarsrapporter.model

import dk.ts.virkr.aarsrapporter.db.Regnskabsdata
import org.springframework.beans.BeanUtils

/**
 * Created by sorenhartvig on 24/06/2017.
 */
class OmsaetningTal extends ModelBase {
  Regnskabstal omsaetning
  Regnskabstal vareforbrug  // CostOfSales
  Regnskabstal driftsindtaegter // OtherOperatingIncome
  Regnskabstal andreeksterneomkostninger //OtherExternalExpenses
  Regnskabstal variableomkostninger // RawMaterialsAndConsumablesUsed
  Regnskabstal lokalomkostninger
  Regnskabstal eksterneomkostninger
  Regnskabstal ejendomsomkostninger
  Regnskabstal vaerdiregulering

  static OmsaetningTal from(Regnskabsdata rd) {
    OmsaetningTal oms = new OmsaetningTal()
    oms.omsaetning = new Regnskabstal(rd.omsaetning)
    oms.vareforbrug = new Regnskabstal(rd.vareforbrug)
    oms.driftsindtaegter = new Regnskabstal(rd.driftsindtaegter)
    oms.andreeksterneomkostninger = new Regnskabstal(rd.andreeksterneomkostninger)
    oms.variableomkostninger = new Regnskabstal(rd.variableomkostninger)
    oms.lokalomkostninger = new Regnskabstal(rd.lokalomkostninger)
    oms.eksterneomkostninger = new Regnskabstal(rd.eksterneomkostninger)
    return oms
  }

  void berig(Regnskabsdata oms) {
    oms.omsaetning = this.omsaetning.vaerdi
    oms.vareforbrug = this.vareforbrug.vaerdi
    oms.driftsindtaegter = this.driftsindtaegter.vaerdi
    oms.andreeksterneomkostninger = this.andreeksterneomkostninger.vaerdi
    oms.variableomkostninger = this.variableomkostninger.vaerdi
    oms.lokalomkostninger = this.lokalomkostninger.vaerdi
    oms.eksterneomkostninger = this.eksterneomkostninger.vaerdi
  }

}
