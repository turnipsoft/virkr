package dk.ts.virkr.aarsrapporter.model

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

}
