package dk.ts.virkr.aarsrapporter.model

/**
 * Created by sorenhartvig on 24/06/2017.
 */
class Passiver extends ModelBase {
  Regnskabstal gaeldsforpligtelser
  Regnskabstal egenkapital
  Regnskabstal udbytte
  Regnskabstal virksomhedskapital
  Regnskabstal overfoertresultat
  Regnskabstal hensaettelserforudskudtskat
  Regnskabstal hensatteforpligtelser
  Regnskabstal gaeldtilrealkredit
  Regnskabstal langfristedegaeldsforpligtelser
  Regnskabstal kortsigtedegaeldsforpligtelser //ShorttermPartOfLongtermLiabilitiesOtherThanProvisions
  Regnskabstal gaeldsforpligtelsertilpengeinstitutter // ShorttermDebtToBanks
  Regnskabstal leverandoereraftjenesteydelser // ShorttermTradePayables
  Regnskabstal gaeldtiltilknyttedevirksomheder // ShorttermPayablesToGroupEnterprises
  Regnskabstal kortfristetskyldigskat // ShorttermTaxPayables
  Regnskabstal andregaeldsforpligtelser // OtherShorttermPayables
  Regnskabstal periodeafgraensningsposter // ShorttermDeferredIncome
  Regnskabstal kortfristedegaeldsforpligtelserialt // ShorttermLiabilitiesOtherThanProvisions
  Regnskabstal passiverialt // LiabilitiesAndEquity
  Regnskabstal andrehensaettelser // OtherProvisions
  Regnskabstal andenlangfristetgaeld // OtherLongtermPayables
  Regnskabstal modtagneforudbetalingerfrakunder //ShorttermPrepaymentsReceivedFromCustomers
  Regnskabstal deposita
  Regnskabstal igangvaerendearbejderforfremmedregning // e:ShorttermContractWorkInProgressLiabilities
}
