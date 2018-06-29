package dk.ts.virkr.aarsrapporter.parser

import dk.ts.virkr.aarsrapporter.dictionary.XbrlDictionary
import dk.ts.virkr.aarsrapporter.dictionary.XbrlElement
import dk.ts.virkr.aarsrapporter.model.Aktiver
import dk.ts.virkr.aarsrapporter.model.Generalforsamling
import dk.ts.virkr.aarsrapporter.model.Passiver
import dk.ts.virkr.aarsrapporter.model.Regnskab
import dk.ts.virkr.aarsrapporter.model.Regnskabstal
import dk.ts.virkr.aarsrapporter.model.Revision
import dk.ts.virkr.aarsrapporter.model.virksomhedsdata.Virksomhedsdata
import dk.ts.virkr.aarsrapporter.model.Resultatopgoerelse
import dk.ts.virkr.aarsrapporter.parser.berigelse.RegnskabBerigelse
import dk.ts.virkr.cvr.integration.model.virksomhed.Beliggenhedsadresse
import groovy.xml.Namespace


/**
 * Created by sorenhartvig on 22/06/16.
 */
class RegnskabXmlParser {

  Virksomhedsdata hentVirksomhedsdataFraRegnskab(RegnskabNodes regnskabNodes) {

    String contextRef = regnskabNodes.aktuelContext
    // hent de relevante felter for dette regnskabsår fra contexten.
    NodeList nl = regnskabNodes.aktuelleNoegletalNodes

    Namespace ns = regnskabNodes.gsdNamespace

    Virksomhedsdata virksomhedsdata = new Virksomhedsdata()
    virksomhedsdata.cvrnummer = getStringValue(nl, ns, "IdentificationNumberCvrOfReportingEntity" )
    virksomhedsdata.navn = getStringValue(nl, ns, "NameOfReportingEntity")
    virksomhedsdata.vejnavn = getStringValue(nl, ns, "AddressOfReportingEntityStreetName")
    virksomhedsdata.husnr = getStringValue(nl, ns, "AddressOfReportingEntityStreetBuildingIdentifier")
    virksomhedsdata.postnr = getStringValue(nl, ns, "AddressOfReportingEntityPostCodeIdentifier")
    virksomhedsdata.bynavn = getStringValue(nl, ns, "AddressOfReportingEntityDistrictName")

    return virksomhedsdata
  }

  String getNSPrefix(Node node) {
    String name = node.name()
    String resultat = name.contains(':')? name.substring(0, name.indexOf(':')+1): ''
    return resultat
  }

  void berigMedPeriode(Regnskab regnskab, RegnskabNodes regnskabNodes, String contextRefName) {
    List<Node> contextNodes = regnskabNodes.contextNodes
    Node contextRefNode = contextNodes.find { it.attribute('id') == contextRefName }
    String nsPrefix = getNSPrefix(contextRefNode)

    String startDate = contextRefNode[nsPrefix+'period'][nsPrefix+'startDate'].text()
    String endDate = contextRefNode[nsPrefix+'period'][nsPrefix+'endDate'].text()
    regnskab.startdato = startDate
    regnskab.slutdato = endDate
    regnskab.aar = startDate.substring(0,4)
  }

  void berigUnderskrivere(Revision revision, RegnskabNodes nodes) {
    List<Node> nl = nodes.cmnNodes
    Namespace ns = nodes.cmnNamespace

    List<Node> direktionNodes = nl.findAll {it.name().toString() == "$ns.prefix:NameAndSurnameOfMemberOfExecutiveBoard"}
    List<Node> bestyrelseNodes = nl.findAll {it.name().toString() == "$ns.prefix:NameAndSurnameOfMemberOfSupervisoryBoard"}

    revision.direktion = direktionNodes.collect {it.text()}
    revision.bestyrelse = bestyrelseNodes.collect{it.text()}
  }

  boolean parseOgBerig(Regnskab data, RegnskabNodes regnskabNodes, boolean nyeste = true) {
    List<Namespace> ns = regnskabNodes.noegletalNamespace()

    // bemærk at contextref siger noget om hvilken periode man henter tal for
    String contextRef = nyeste?regnskabNodes.aktuelContext : regnskabNodes.sidsteAarsContext

    if (!contextRef) {
      return false
    }

    // hent de relevante felter for dette regnskabsår fra contexten.
    NodeList nl = nyeste ? regnskabNodes.aktuelleNoegletalNodes : regnskabNodes.sidsteAarsNoegletalNodes

    /* periode dato år */
    berigMedPeriode(data, regnskabNodes, contextRef)
    // regnskabsklasse
    data.regnskabsklasse = getStringValue(nl, ns,'ClassOfReportingEntity' )

    berigResultatopgoerelse(data, nl, ns)

    nl = nyeste ? regnskabNodes.aktuelleBalanceNodes : regnskabNodes.sidsteAarsBalanceNodes

    Aktiver aktiver = data.balance.aktiver
    aktiver.langsigtedekapitalandeleitilknyttedevirksomheder = getRegnskabstal('Langsigtede kapitalandele i tilknyttede virksomheder','aktiver',
      nl, ns, 'LongtermInvestmentsInGroupEnterprises',
      'OtherInvestmentsInSubsidiariesJointVenturesAndAssociates')

    aktiver.andreaktiver =  getRegnskabstal('Andre aktiver','aktiver',nl, ns, 'OtherNoncurrentReceivables')
    aktiver.andreinvesteringer =  getRegnskabstal('Andre investeringer','aktiver',nl, ns, 'OtherInvestmentsInSubsidiariesJointVenturesAndAssociates')

    aktiver.andreanlaegdriftoginventar = getRegnskabstal('Andre anlæg drift og inventar','aktiver',nl, ns, 'FixturesFittingsToolsAndEquipment')
    aktiver.materielleanlaegsaktiver = getRegnskabstal('Materielle anlægsaktiver','aktiver',nl, ns, 'PropertyPlantAndEquipment')
    aktiver.andretilgodehavender = getRegnskabstal('Andre tilgodehavender','aktiver',nl, ns, 'OtherLongtermReceivables','OtherNoncurrentReceivables')
    aktiver.finansielleanlaegsaktiver = getRegnskabstal('Finansielle anlægsaktiver','aktiver',nl, ns, 'LongtermInvestmentsAndReceivables', 'NoncurrentFinancialAssets')
    aktiver.anlaegsaktiver = getRegnskabstal('Anlægsaktiver','aktiver',nl, ns, 'NoncurrentAssets')

    aktiver.kravpaaindbetalingafregistreretkapital  = getRegnskabstal('Krav på indbetaling af registreret kapital','aktiver',nl, ns, 'ContributedCapitalInArrears')
    aktiver.erhvervedeimmaterielleanlaegsaktiver = getRegnskabstal('Erhvervede immaterielle anlægsaktiver','aktiver',nl, ns, 'AcquiredIntangibleAssets')
    aktiver.immaterielleanlaegsaktiver = getRegnskabstal('Immaterielle anlægsaktiver','aktiver',nl, ns, 'IntangibleAssets')
    aktiver.materielleanlaegsaktiverunderudfoerelse = getRegnskabstal('Materielle anlægsaktiver under udførelse','aktiver',nl, ns, 'PropertyPlantAndEquipmentInProgressAndPrepaymentsForPropertyPlantAndEquipment')
    aktiver.grundeogbygninger = getRegnskabstal('Grunde og bygninger','aktiver',nl, ns, 'LandAndBuildings')

    aktiver.raavareroghjaelpematerialer = getRegnskabstal('Råvarer og hjælpematerialer','aktiver',nl, ns, 'RawMaterialsAndConsumables')
    aktiver.fremstilledevareroghandelsvarer = getRegnskabstal('Fremstillede varer og handelsvarer','aktiver',nl, ns, 'ManufacturedGoodsAndGoodsForResale')
    aktiver.varebeholdninger = getRegnskabstal('Varebeholdninger','aktiver',nl, ns, 'ManufacturedGoodsAndGoodsForResale')
    aktiver.tilgodehavenderfrasalogtjenesteydelser  = getRegnskabstal('Tilgodehavender fra salg og tjenesteydelser','aktiver',nl, ns, 'ShorttermTradeReceivables','CurrentTradeReceivables')
    aktiver.tilgodehaverhostilknyttedevirksomheder = getRegnskabstal('Tilgodehavender hos tilknyttede virksomheder','aktiver',nl, ns, 'ShorttermReceivablesFromGroupEnterprises','TradeAndOtherCurrentReceivablesDueFromRelatedParties')
    aktiver.andretilgodehavenderomsaetningaktiver = getRegnskabstal('Andre tilgodehavender og omsætningaktiver','aktiver',nl, ns, 'OtherShorttermReceivables','OtherCurrentReceivables')
    aktiver.langfristedetilgodehavenderhosvirksomhedsdeltagereogledelse = getRegnskabstal('Langfristede tilgodehavender hos virksomhedsdeltagere og ledelse','aktiver',nl, ns, 'LongtermReceivablesFromOwnersAndManagement')
    aktiver.igangvaerendearbejderforfremmedkontrakt = getRegnskabstal('Igangværende arbejder for fremmed kontrakt','aktiver',nl, ns, 'WorkInProgressForThirdPartiesAssets')
    aktiver.kortfristedetilgodehavenderhosvirksomhedsdeltagereogledelse = getRegnskabstal('Kortfristede tilgodehavender hos virksomhedsdeltagere og ledelse','aktiver',nl, ns, 'ShorttermReceivablesFromOwnersAndManagement')
    aktiver.tilgodehavenderfravirksomhedsdeltagereogledelse = getRegnskabstal('Tilgodehavender fra virksomhedsdeltagere og ledelse','aktiver',nl, ns, 'ReceivablesFromOwnersAndManagementMember')
    aktiver.kortfristedetilgodehavenderhosnaertstaaendeparter = getRegnskabstal('Kortfristede tilgodehavender hos nærtstående parter','aktiver',nl, ns, 'TradeAndOtherCurrentReceivablesDueFromRelatedParties')

    aktiver.periodeafgraensningsposter = getRegnskabstal('Periodeafgrænsningsposter','aktiver',nl, ns, 'DeferredIncomeAssets','CurrentPrepayments')
    aktiver.tilgodehavenderialt = getRegnskabstal('Tilgodehavender ialt','aktiver',nl, ns, 'ShorttermReceivables','TradeAndOtherCurrentReceivables')

    aktiver.andrevaerdipapirerogkapitalandele = getRegnskabstal('Andre værdipapirer og kapitalandele','aktiver',nl, ns, 'OtherShorttermInvestments')
    aktiver.vaerdipapirerialt = getRegnskabstal('Værdipapirer ialt','aktiver',nl, ns, 'ShorttermInvestments')
    aktiver.likvidebeholdninger = getRegnskabstal('Likvide beholdninger','aktiver',nl, ns, 'CashAndCashEquivalents','Cash')
    aktiver.omsaetningsaktiver = getRegnskabstal('Omsætningsaktiver','aktiver',nl, ns, 'CurrentAssets')
    aktiver.faerdiggjorteudviklingsprojekter = getRegnskabstal('Færdiggjorte udviklingsprojekter','aktiver',nl, ns, 'CompletedDevelopmentProjects')

    aktiver.aktiver = getRegnskabstal('Aktiver','aktiver',nl, ns, 'Assets')

    aktiver.tilgodehavenderfraassocieredevirksomheder = getRegnskabstal('Tilgodehavender fra associerede virksomheder','aktiver',nl, ns, 'ShorttermReceivablesFromAssociates')
    aktiver.produktionsanlaegogmaskiner = getRegnskabstal('Produktionsanlæg og maskiner','aktiver',nl, ns, 'PlantAndMachinery')
    aktiver.materielleaktiverunderudfoerelseogforudbetalingerformaterielleanlaegsaktiver = getRegnskabstal('Materielle aktiver under udførelse og forudbetalinger for materielle anlægsaktiver','aktiver',nl, ns, 'PropertyPlantAndEquipmentInProgressAndPrepaymentsForPropertyPlantAndEquipment')
    aktiver.langfristedetilgodehavenderhosassocieredevirksomheder = getRegnskabstal('Langfristede tilgodehavender hos associerede virksomhederr','aktiver',nl, ns, 'LongtermReceivablesFromAssociates')

    Passiver passiver = data.balance.passiver
    passiver.gaeldsforpligtelser = getRegnskabstal('Gældsforpligtelser','passiver',nl, ns, 'LiabilitiesOtherThanProvisions','CurrentLiabilities' )
    passiver.egenkapital = getRegnskabstal('Egenkapital','passiver',nl, ns, 'Equity' )
    passiver.udbytte = getRegnskabstal('Udbytte','passiver',nl, ns, 'ProposedDividendRecognisedInEquity', 'ProposedDividend')
    passiver.virksomhedskapital = getRegnskabstal('Virksomhedskapital','passiver',nl, ns, 'ContributedCapital', 'IssuedCapital')
    passiver.overfoertresultat = getRegnskabstal('Overført resultat','passiver',nl, ns, 'RetainedEarnings')
    passiver.hensaettelserforudskudtskat = getRegnskabstal('Hensættelser for udskudt skat','passiver',nl, ns, 'ProvisionsForDeferredTax')
    passiver.hensatteforpligtelser = getRegnskabstal('Hensatte forpligtelser','passiver',nl, ns, 'Provisions')
    passiver.gaeldtilrealkredit = getRegnskabstal('Gæld til realkredit','passiver',nl, ns, 'LongtermMortgageDebt')
    passiver.langfristedegaeldsforpligtelser = getRegnskabstal('Langfristede gældsforpligtelser','passiver',nl, ns, 'LongtermLiabilitiesOtherThanProvisions')
    passiver.kortsigtedegaeldsforpligtelser = getRegnskabstal('Kortsigtede gældsforpligtelser','passiver',nl, ns, 'ShorttermPartOfLongtermLiabilitiesOtherThanProvisions')
    passiver.gaeldsforpligtelsertilpengeinstitutter = getRegnskabstal('Gældsforpligtelser til pengeinstitutter','passiver',nl, ns, 'ShorttermDebtToBanks')
    passiver.leverandoereraftjenesteydelser = getRegnskabstal('Leverandører af tjenesteydelser','passiver',nl, ns, 'ShorttermTradePayables','TradeAndOtherCurrentPayablesToTradeSuppliers')
    passiver.gaeldtiltilknyttedevirksomheder = getRegnskabstal('Gæld til tilknyttede virksomheder','passiver',nl, ns, 'ShorttermPayablesToGroupEnterprises','TradeAndOtherCurrentPayablesToRelatedParties')
    passiver.kortfristetskyldigskat =  getRegnskabstal('Kortfristet skyldig skat','passiver',nl, ns, 'ShorttermTaxPayables','CurrentTaxLiabilitiesCurrent')
    passiver.andregaeldsforpligtelser = getRegnskabstal('Andre gældsforpligtelser','passiver',nl, ns, 'OtherShorttermPayables','OtherCurrentPayables')
    passiver.periodeafgraensningsposter = getRegnskabstal('Periodeafgrænsningsposter','passiver',nl, ns, 'ShorttermDeferredIncome')
    passiver.kortfristedegaeldsforpligtelserialt = getRegnskabstal('Kortfristede gældsforpligtelser ialt','passiver',nl, ns, 'ShorttermLiabilitiesOtherThanProvisions')
    passiver.passiverialt = getRegnskabstal('Passiver ialt','passiver',nl, ns, 'LiabilitiesAndEquity','EquityAndLiabilities')
    passiver.andrehensaettelser = getRegnskabstal('Andre hensættelser','passiver',nl, ns, 'OtherProvisions')
    passiver.andenlangfristetgaeld = getRegnskabstal('Anden langfristet gæld','passiver',nl, ns, 'OtherLongtermPayables')
    passiver.modtagneforudbetalingerfrakunder = getRegnskabstal('Modtagne forudbetalinger fra kunder','passiver',nl, ns, 'ShorttermPrepaymentsReceivedFromCustomers','CurrentPrepaymentsFromCustomers')
    passiver.deposita = getRegnskabstal('Deposita','passiver',nl, ns, 'DepositsLongtermLiabilitiesOtherThanProvisions')
    passiver.igangvaerendearbejderforfremmedregning = getRegnskabstal('Igangværende arbejder for fremmed regning','passiver',nl, ns, 'ShorttermContractWorkInProgressLiabilities')

    passiver.andrereserver = getRegnskabstal('Andre reserver','passiver',nl, ns, 'OtherReserves')
    passiver.reserveforudviklingsomkostninger = getRegnskabstal('Reserve for udviklingsomkostninger','passiver',nl, ns, 'ReserveForDevelopmentExpenditure')
    passiver.kreditinstitutter = getRegnskabstal('Kreditinstitutter','passiver',nl, ns, 'ShorttermDebtToOtherCreditInstitutions')


    // det aktuelle regnskab har også revisionsoplysninger
    if (nyeste) {
      berigMedRevision(data, regnskabNodes)
      berigUnderskrivere(data.revision, regnskabNodes)
    }

    fixSkat(data)

    XbrlDictionary.lock()
    return true
  }

  private void berigResultatopgoerelse(Regnskab data, NodeList nl, List<Namespace> ns) {

    /** Resultatopgørelsen **/
    Resultatopgoerelse r = data.resultatopgoerelse

    //Omsætning
    r.omsaetningTal.omsaetning = getRegnskabstal('Omsætning','resultatopgoerelse',nl, ns, "Revenue")
    r.omsaetningTal.vareforbrug = getRegnskabstal('Vareforbrug','resultatopgoerelse',nl, ns, "CostOfSales")
    // driftsindtæger
    r.omsaetningTal.driftsindtaegter = getRegnskabstal('Driftsindtægter','resultatopgoerelse',nl, ns, "OtherOperatingIncome")
    // andre eksterne omkostninger
    r.omsaetningTal.andreeksterneomkostninger = getRegnskabstal('Andre eksterne omkostninger','resultatopgoerelse',nl, ns, "OtherExternalExpenses")
    r.omsaetningTal.variableomkostninger = getRegnskabstal('Variable omkostninger','resultatopgoerelse',nl, ns, "RawMaterialsAndConsumablesUsed")
    r.omsaetningTal.lokalomkostninger = getRegnskabstal('Lokal omkostninger','resultatopgoerelse',nl, ns, "PropertyCost")
    r.omsaetningTal.eksterneomkostninger = getRegnskabstal('Eksterne omkostninger','resultatopgoerelse',nl, ns, "ExternalExpenses")

    //BruttoresultatTal
    r.bruttoresultatTal.bruttofortjeneste = getRegnskabstal('Bruttofortjeneste','resultatopgoerelse',nl, ns, "GrossProfitLoss", "GrossResult", "GrossProfit")
    r.bruttoresultatTal.medarbejderomkostninger = getRegnskabstal('Medarbejderomkostninger','resultatopgoerelse',nl, ns, "EmployeeBenefitsExpense")
    // regnskabsmæssige afskrivninger
    r.bruttoresultatTal.regnskabsmaessigeafskrivninger = getRegnskabstal('Regnskabsmæssige afskrivninger','resultatopgoerelse',nl, ns,
      "DepreciationAmortisationExpenseAndImpairmentLossesOfPropertyPlantAndEquipmentAndIntangibleAssetsRecognisedInProfitOrLoss")
    r.bruttoresultatTal.administrationsomkostninger = getRegnskabstal('Administrationsomkostninger','resultatopgoerelse',nl, ns, "AdministrativeExpenses")


    // NettoresultatTal
    r.nettoresultatTal.finansielleomkostninger = getRegnskabstal('Finansielle omkostninger','resultatopgoerelse',nl, ns, "OtherFinanceExpenses", "FinanceCosts",
      "RestOfOtherFinanceExpenses")
    r.nettoresultatTal.driftsresultat = getRegnskabstal('Driftsresultat','resultatopgoerelse',nl, ns, "ProfitLossFromOrdinaryOperatingActivities",
      "ProfitLossFromOperatingActivities")
    r.nettoresultatTal.finansielleindtaegter = getRegnskabstal('Finansielleindtaegter','resultatopgoerelse',nl, ns, "OtherFinanceIncome", "FinanceIncome", "IncomeFromOtherLongtermInvestmentsAndReceivables")
    r.nettoresultatTal.kapitalandeleiassocieredevirksomheder = getRegnskabstal('Kapitalandele i associerede virksomheder','resultatopgoerelse',nl, ns, "IncomeFromInvestmentsInAssociates")
    r.nettoresultatTal.kapitalandeleitilknyttedevirksomheder = getRegnskabstal('Kapitalandele i tilknyttede virksomheder','resultatopgoerelse',nl, ns, "IncomeFromInvestmentsInGroupEnterprises")

    // Årets resultat
    r.aaretsresultatTal.aaretsresultat = getRegnskabstal('Årets resultat','resultatopgoerelse',nl, ns, "ProfitLoss")
    r.aaretsresultatTal.resultatfoerskat = getRegnskabstal('Resultat før skat','resultatopgoerelse',nl, ns, "ProfitLossFromOrdinaryActivitiesBeforeTax", "ProfitLossBeforeTax")
    r.aaretsresultatTal.skatafaaretsresultat = getRegnskabstal('Skat af årets resultat','resultatopgoerelse',nl, ns, "TaxExpenseOnOrdinaryActivities", "TaxExpense",
      "IncomeTaxExpenseContinuingOperations")
  }

  /**
   * Beriger nøgletal rekursivt indtil der ikke er flere nøgletal at berige
   * Er p.t. ude af drift, der er for manger usikkerhedsmomenter i disse beregninger.
   * @param data
   */
  void berigRegnskabdataMedManglendeNoegletal(Regnskab data) {
    RegnskabBerigelse regnskabBerigelse = new RegnskabBerigelse()
    boolean harBeriget = regnskabBerigelse.berigNoegletal(data)
    while (harBeriget) {
      harBeriget = regnskabBerigelse.berigNoegletal(data)
    }
  }

  private berigMedRevision(Regnskab regnskab, RegnskabNodes regnskabNodes) {
    Revision revision = new Revision()
    NodeList nl = regnskabNodes.cmnNodes
    Namespace ns = regnskabNodes.cmnNamespace

    if (ns) {
      // hent de relevante fra CMN NAMESPACE

      revision.assistancetype = getStringValue(nl, ns, 'TypeOfAuditorAssistance')
      revision.revisionsfirmaNavn = getStringValue(nl, ns, 'NameOfAuditFirm')
      revision.navnPaaRevisor = getStringValue(nl, ns, 'NameAndSurnameOfAuditor')
      revision.beskrivelseAfRevisor = getStringValue(nl, ns, 'DescriptionOfAuditor')
      revision.revisionsfirmaCvrnummer = getStringValue(nl, ns, 'IdentificationNumberCvrOfAuditFirm')
      revision.mnenummer = getStringValue(nl, ns, 'IdentificationNumberOfAuditor')
    }

    ns = regnskabNodes.gsdNamespace

    // hent de relevante fra GSD
    nl = regnskabNodes.gsdNodes

    // Det er ikke altid felterne ovenfor kommer fra CMN så forsøges GSD
    revision.assistancetype = !revision.assistancetype? getStringValue(nl, ns, 'TypeOfAuditorAssistance') : revision.assistancetype
    revision.revisionsfirmaNavn =  !revision.revisionsfirmaNavn? getStringValue(nl, ns, 'NameOfAuditFirm') :revision.revisionsfirmaNavn
    revision.navnPaaRevisor =  !revision.navnPaaRevisor? getStringValue(nl, ns, 'NameAndSurnameOfAuditor') : revision.navnPaaRevisor
    revision.beskrivelseAfRevisor =  !revision.beskrivelseAfRevisor? getStringValue(nl, ns, 'DescriptionOfAuditor') : revision.beskrivelseAfRevisor
    revision.revisionsfirmaCvrnummer =  !revision.revisionsfirmaCvrnummer? getStringValue(nl, ns, 'IdentificationNumberCvrOfAuditFirm') : revision.revisionsfirmaCvrnummer
    revision.mnenummer =  !revision.mnenummer ? getStringValue(nl, ns, 'IdentificationNumberOfAuditor') : revision.mnenummer

    // hvis der ikke var audit felter, så hent audit fra submitting enterprise
    if (!revision.revisionsfirmaNavn) {
      revision.revisionsfirmaNavn = getStringValue(nl, ns, 'NameOfSubmittingEnterprise')
    }

    if (!revision.revisionsfirmaNavn) {
      revision.revisionsfirmaNavn = getStringValue(nl, ns, 'NameOfReportingEntity')
    }

    if (!revision.revisionsfirmaCvrnummer) {
      revision.revisionsfirmaCvrnummer = getStringValue(nl, ns, 'IdentificationNumberCvrOfSubmittingEnterprise')
    }

    if (!revision.revisionsfirmaCvrnummer) {
      revision.revisionsfirmaCvrnummer = getStringValue(nl, ns, 'IdentificationNumberCvrOfReportingEntity')
    }

    revision.beliggenhedsadresse = new Beliggenhedsadresse()
    revision.beliggenhedsadresse.vejnavn = getStringValue(nl, ns, 'AddressOfAuditorStreetName')
    revision.beliggenhedsadresse.husnummerFra = getStringValue(nl, ns, 'AddressOfAuditorStreetBuildingIdentifier')
    revision.beliggenhedsadresse.postnummer = getStringValue(nl, ns, 'AddressOfAuditorPostCodeIdentifier')
    revision.beliggenhedsadresse.postdistrikt = getStringValue(nl, ns, 'AddressOfAuditorDistrictName')
    revision.beliggenhedsadresse.land = getStringValue(nl, ns, 'AddressOfAuditorCountry')
    revision.telefonnummer = getStringValue(nl, ns, 'TelephoneNumberOfAuditor')
    revision.email = getStringValue(nl, ns, 'EmailOfAuditor')

    revision.generalforsamling = new Generalforsamling()
    revision.generalforsamling.dato = getStringValue(nl, ns, 'DateOfGeneralMeeting')
    revision.generalforsamling.formand = getStringValue(nl, ns, 'NameAndSurnameOfChairmanOfGeneralMeeting')

    ns = regnskabNodes.arrNamespace

    // hent de relevante fra ARR
    nl = regnskabNodes.arrNodes

    if (ns) {
      revision.revisionUnderskriftsted = getStringValue(nl, ns, 'SignatureOfAuditorsPlace')
      revision.revisionUnderskriftdato = getStringValue(nl, ns, 'SignatureOfAuditorsDate')
      revision.adressant = getStringValue(nl, ns, 'AddresseeOfAuditorsReportOnAuditedFinancialStatements')

      // Hvis revisoren i sig selv ikke lå i CMN/GSD, kan de ligge i ARR
      if (!revision.navnPaaRevisor) {
        revision.navnPaaRevisor = getStringValue(nl, ns, 'NameAndSurnameOfAuditor')
        revision.beskrivelseAfRevisor = getStringValue(nl, ns, 'DescriptionOfAuditor')
      }

      if (!revision.navnPaaRevisor) {
        // hvis de stadig ikke er der, kan de ligge i nogle andre felter
        revision.navnPaaRevisor = getStringValue(nl, ns, 'NameAndSurnameOfAuditorAppointedToPerformAudit')
        revision.beskrivelseAfRevisor = getStringValue(nl, ns, 'DescriptionOfAuditorAppointedToPerformAudit')
      }

      if (!revision.revisionsfirmaCvrnummer) {
        revision.revisionsfirmaCvrnummer = getStringValue(nl, ns, 'IdentificationNumberCvrOfAuditFirm')
      }

      revision.supplerendeInformationOmAndreForhold = getStringValue(nl, ns, 'SupplementaryInformationOnOtherMatters')
      revision.supplerendeInformationOmAarsrapport = getStringValue(nl, ns, 'SupplementaryInformationOnMattersPertainingToAuditedFinancialStatement')
      revision.supplerendeInformationOmRevision = getStringValue(nl, ns, 'SupplementaryInformationOnAudit')
      revision.vaesentligUsikkerhedVedrFortsatDrift = getStringValue(nl, ns, 'MaterialUncertaintyConcerningGoingConcernAudit')

      revision.grundlagForKonklusion = getStringValue(nl, ns, 'DescriptionOfQualificationsOfAuditedFinancialStatements','OpinionOnAuditedFinancialStatements')
      revision.konklusionMedForbehold = getStringValue(nl, ns, 'TypeOfModifiedOpinionOnAuditedFinancialStatements', 'TypeOfBasisForModifiedOpinionOnAuditedFinancialStatements ')

    }

    // findes ikke i IFRS regnskaber
    ns = regnskabNodes.sobNamespace

    if (ns) {
      // hent de relevante fra SOB
      nl = regnskabNodes.sobNodes

      revision.godkendelsesdato = getStringValue(nl, ns, 'DateOfApprovalOfAnnualReport')
      revision.ingenRevision = getStringValue(nl, ns, 'StatementOnOptingOutOfAuditingFinancialStatementsInNextReportingPeriodDueToExemption')

    }

    regnskab.revision = revision
  }

  void fixSkat(Regnskab regnskab) {
    // forsøg at fix skat til at være negativt eller positivt tal
    if (regnskab.resultatopgoerelse.aaretsresultatTal.resultatfoerskat &&
      regnskab.resultatopgoerelse.aaretsresultatTal.skatafaaretsresultat &&
      regnskab.resultatopgoerelse.aaretsresultatTal.aaretsresultat) {

      if (regnskab.resultatopgoerelse.aaretsresultatTal.resultatfoerskat.vaerdi<
        regnskab.resultatopgoerelse.aaretsresultatTal.aaretsresultat.vaerdi) {
        regnskab.resultatopgoerelse.aaretsresultatTal.skatafaaretsresultat.vaerdi =
          Math.abs(regnskab.resultatopgoerelse.aaretsresultatTal.skatafaaretsresultat.vaerdi)
      } else {
        regnskab.resultatopgoerelse.aaretsresultatTal.skatafaaretsresultat.vaerdi =
          Math.abs(regnskab.resultatopgoerelse.aaretsresultatTal.skatafaaretsresultat.vaerdi)*-1
      }
    }
  }

  Regnskabstal getRegnskabstal(String feltnavn, String placering, NodeList nodeList, List<Namespace> ns, String ...nodeName) {

    for (Namespace n: ns) {
      Regnskabstal regnskabstal = getRegnskabstal(feltnavn, placering, nodeList, n, nodeName)
      if (regnskabstal) {
        return regnskabstal;
      }
    }

    return null;
  }

  Regnskabstal getRegnskabstal(String feltnavn, String placering, NodeList nodeList, Namespace ns, String ...nodeName){
    Node node
    List<String> xbrlNames = nodeName.collect{ it }
    String nn

    nodeName.each {
      if (node) return
      nn = it
      String nodenameMedPrefix = "$ns.prefix:$it"
      node = nodeList.find {
        it.name().toString() == nodenameMedPrefix
      }
    }

    XbrlElement xbrlElement = new XbrlElement(xbrlNames, feltnavn, placering)
    XbrlDictionary.addElement(xbrlElement)


    if (node!=null) {
      String decimaler = node.attribute('decimals')
      Long decimal = 0;

      if (decimaler) {
        try {
          decimal = decimaler ? Long.valueOf(decimaler) : 0
        } catch (Exception e) {
          decimal = 0;
        }
      }

      // man kan desværre ikke rigtig regne med de decimaler der.. hmmmm, nu kan der tilmed stå INF
      return new Regnskabstal(getAmount(node), decimal, nn, feltnavn, placering)
    }

    return null
  }

  String getStringValue(NodeList nodeList, List<Namespace> ns, String nodeName, String altNodename = null){
    for (Namespace n: ns) {
      String value = getStringValue(nodeList, n, nodeName, altNodename)
      if (value) {
        return value
      }
    }
    return null
  }


  String getStringValue(NodeList nodeList, Namespace ns, String nodeName, String altNodename = null){
    nodeName = "$ns.prefix:$nodeName"
    Node n = nodeList.find {
      it.name() == nodeName
    }

    if (n!=null) {
      return n.text()
    } else if (altNodename) {
      return getStringValue(nodeList, ns, altNodename)
    }

    return null
  }

  Long getValue(root, nodeName, alternateNodeName = null) {
    def nodes = root[nodeName]
    if (nodes && nodes.size() > 0) {
      Node node = nodes[0]
      return getAmount(node)
    } else if (alternateNodeName) {
      return getValue(root, alternateNodeName)
    }

    return null
  }

  Long getAmount(Node n) {
    String amount = n.text()
    Long vaerdi = Double.valueOf(amount).longValue()
    return vaerdi
  }
}
