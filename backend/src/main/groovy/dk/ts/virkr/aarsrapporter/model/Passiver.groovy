package dk.ts.virkr.aarsrapporter.model

import dk.ts.virkr.aarsrapporter.db.Regnskabsdata
import org.springframework.beans.BeanUtils

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

  static Passiver from(Regnskabsdata rd) {
    Passiver p = new Passiver()
    p.gaeldsforpligtelser = new Regnskabstal(rd.gaeldsforpligtelser)
    p.egenkapital = new Regnskabstal(rd.egenkapital)
    return p
  }

  void berig(Regnskabsdata rd) {
    rd.egenkapital = this.egenkapital.vaerdi
    rd.gaeldsforpligtelser = this.gaeldsforpligtelser.vaerdi
  }
}
