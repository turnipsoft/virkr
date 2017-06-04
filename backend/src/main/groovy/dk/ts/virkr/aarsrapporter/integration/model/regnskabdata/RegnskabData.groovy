package dk.ts.virkr.aarsrapporter.integration.model.regnskabdata

import dk.ts.virkr.aarsrapporter.integration.model.virksomhedsdata.Virksomhedsdata

/**
 * Created by sorenhartvig on 22/06/16.
 */
class RegnskabData {
  String id
  String cvrNummer
  String startDato
  String slutDato
  String sidsteOpdatering
  String aar
  boolean omgoerelse
  String pdfUrl
  String xbrlUrl
  Long bruttofortjeneste
  Long driftsresultat
  Long aaretsresultat
  Long resultatfoerskat
  Long skatafaaretsresultat
  Long gaeldsforpligtelser
  Long egenkapital
  Long finansielleOmkostninger
  Long finansielleIndtaegter
  Long omsaetning
  Long medarbejderOmkostninger
  Long goodwill

  Long vareforbrug  // CostOfSales
  Long driftsindtaegter // OtherOperatingIncome
  Long andreEksterneOmkostninger //OtherExternalExpenses
  Long regnskabsmaessigeAfskrivninger //DepreciationAmortisationExpenseAndImpairmentLossesOfPropertyPlantAndEquipmentAndIntangibleAssetsRecognisedInProfitOrLoss

  Virksomhedsdata virksomhedsdata
}
