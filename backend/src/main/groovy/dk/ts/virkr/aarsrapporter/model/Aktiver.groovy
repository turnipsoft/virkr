package dk.ts.virkr.aarsrapporter.model

import dk.ts.virkr.aarsrapporter.db.Regnskabsdata

/**
 * Created by sorenhartvig on 24/06/2017.
 */
class Aktiver extends ModelBase {

  // Anlægsaktiver
  Regnskabstal langsigtedekapitalandeleitilknyttedevirksomheder //LongtermInvestmentsInGroupEnterprises
  Regnskabstal andreanlaegdriftoginventar //FixturesFittingsToolsAndEquipment
  Regnskabstal materielleanlaegsaktiver //PropertyPlantAndEquipment
  Regnskabstal andretilgodehavender //OtherLongtermReceivables
  Regnskabstal finansielleanlaegsaktiver //LongtermInvestmentsAndReceivables
  Regnskabstal anlaegsaktiver //NoncurrentAssets
  Regnskabstal faerdiggjorteudviklingsprojekter
  Regnskabstal erhvervedeimmaterielleanlaegsaktiver // AcquiredIntangibleAssets
  Regnskabstal immaterielleanlaegsaktiver // IntangibleAssets
  Regnskabstal materielleanlaegsaktiverunderudfoerelse // PropertyPlantAndEquipmentInProgressAndPrepaymentsForPropertyPlantAndEquipment
  Regnskabstal grundeogbygninger // LandAndBuildings

  // Omsætningsaktiver
  Regnskabstal raavareroghjaelpematerialer // RawMaterialsAndConsumables
  Regnskabstal fremstilledevareroghandelsvarer // ManufacturedGoodsAndGoodsForResale
  Regnskabstal varebeholdninger //ManufacturedGoodsAndGoodsForResale
  Regnskabstal tilgodehavenderfrasalogtjenesteydelser // ShorttermTradeReceivables
  Regnskabstal tilgodehaverhostilknyttedevirksomheder //ShorttermReceivablesFromGroupEnterprises
  Regnskabstal andretilgodehavenderomsaetningaktiver //OtherShorttermReceivables
  Regnskabstal periodeafgraensningsposter // DeferredIncomeAssets
  Regnskabstal langfristedetilgodehavenderhosvirksomhedsdeltagereogledelse //LongtermReceivablesFromOwnersAndManagement
  Regnskabstal kortfristedetilgodehavenderhosvirksomhedsdeltagereogledelse //ShorttermReceivablesFromOwnersAndManagement
  Regnskabstal tilgodehavenderfravirksomhedsdeltagereogledelse //ReceivablesFromOwnersAndManagementMember

  Regnskabstal tilgodehavenderialt // ShorttermReceivables
  Regnskabstal andrevaerdipapirerogkapitalandele // OtherShorttermInvestments
  Regnskabstal vaerdipapirerialt // ShorttermInvestments
  Regnskabstal likvidebeholdninger // CashAndCashEquivalents
  Regnskabstal omsaetningsaktiver // CurrentAssets
  Regnskabstal aktiver // Assets

  static Aktiver from(Regnskabsdata rd) {
    Aktiver aktiver = new Aktiver()
    return aktiver
  }

  void berig(Regnskabsdata rd) {

  }
}
