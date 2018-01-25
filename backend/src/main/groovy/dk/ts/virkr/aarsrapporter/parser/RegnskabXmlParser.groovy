package dk.ts.virkr.aarsrapporter.parser

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

  void berigMedPeriode(Regnskab regnskab, RegnskabNodes regnskabNodes, Namespace ns, String contextRefName) {
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
    Namespace ns = regnskabNodes.noegletalNamespace()

    // bemærk at contextref siger noget om hvilken periode man henter tal for
    String contextRef = nyeste?regnskabNodes.aktuelContext : regnskabNodes.sidsteAarsContext

    if (!contextRef) {
      return false
    }

    // hent de relevante felter for dette regnskabsår fra contexten.
    NodeList nl = nyeste ? regnskabNodes.aktuelleNoegletalNodes : regnskabNodes.sidsteAarsNoegletalNodes

    /* periode dato år */
    berigMedPeriode(data, regnskabNodes, ns, contextRef)
    // regnskabsklasse
    data.regnskabsklasse = getStringValue(nl, ns,'ClassOfReportingEntity' )

    berigResultatopgoerelse(data, nl, ns)

    nl = nyeste ? regnskabNodes.aktuelleBalanceNodes : regnskabNodes.sidsteAarsBalanceNodes

    Aktiver aktiver = data.balance.aktiver
    aktiver.langsigtedekapitalandeleitilknyttedevirksomheder = getRegnskabstal(nl, ns, 'LongtermInvestmentsInGroupEnterprises')
    aktiver.andreanlaegdriftoginventar = getRegnskabstal(nl, ns, 'FixturesFittingsToolsAndEquipment')
    aktiver.materielleanlaegsaktiver = getRegnskabstal(nl, ns, 'PropertyPlantAndEquipment')
    aktiver.andretilgodehavender = getRegnskabstal(nl, ns, 'OtherLongtermReceivables')
    aktiver.finansielleanlaegsaktiver = getRegnskabstal(nl, ns, 'LongtermInvestmentsAndReceivables')
    aktiver.anlaegsaktiver = getRegnskabstal(nl, ns, 'NoncurrentAssets')
    aktiver.erhvervedeimmaterielleanlaegsaktiver = getRegnskabstal(nl, ns, 'AcquiredIntangibleAssets')
    aktiver.immaterielleanlaegsaktiver = getRegnskabstal(nl, ns, 'IntangibleAssets')
    aktiver.materielleanlaegsaktiverunderudfoerelse = getRegnskabstal(nl, ns, 'PropertyPlantAndEquipmentInProgressAndPrepaymentsForPropertyPlantAndEquipment')
    aktiver.grundeogbygninger = getRegnskabstal(nl, ns, 'LandAndBuildings')

    aktiver.raavareroghjaelpematerialer = getRegnskabstal(nl, ns, 'RawMaterialsAndConsumables')
    aktiver.fremstilledevareroghandelsvarer = getRegnskabstal(nl, ns, 'ManufacturedGoodsAndGoodsForResale')
    aktiver.varebeholdninger = getRegnskabstal(nl, ns, 'ManufacturedGoodsAndGoodsForResale')
    aktiver.tilgodehavenderfrasalogtjenesteydelser  = getRegnskabstal(nl, ns, 'ShorttermTradeReceivables')
    aktiver.tilgodehaverhostilknyttedevirksomheder = getRegnskabstal(nl, ns, 'ShorttermReceivablesFromGroupEnterprises')
    aktiver.andretilgodehavenderomsaetningaktiver = getRegnskabstal(nl, ns, 'OtherShorttermReceivables')
    aktiver.langfristedetilgodehavenderhosvirksomhedsdeltagereogledelse = getRegnskabstal(nl, ns, 'LongtermReceivablesFromOwnersAndManagement')
    aktiver.kortfristedetilgodehavenderhosvirksomhedsdeltagereogledelse = getRegnskabstal(nl, ns, 'ShorttermReceivablesFromOwnersAndManagement')
    aktiver.tilgodehavenderfravirksomhedsdeltagereogledelse = getRegnskabstal(nl, ns, 'ReceivablesFromOwnersAndManagementMember')

    aktiver.periodeafgraensningsposter = getRegnskabstal(nl, ns, 'DeferredIncomeAssets')
    aktiver.tilgodehavenderialt = getRegnskabstal(nl, ns, 'ShorttermReceivables')
    aktiver.andrevaerdipapirerogkapitalandele = getRegnskabstal(nl, ns, 'OtherShorttermInvestments')
    aktiver.vaerdipapirerialt = getRegnskabstal(nl, ns, 'ShorttermInvestments')
    aktiver.likvidebeholdninger = getRegnskabstal(nl, ns, 'CashAndCashEquivalents')
    aktiver.omsaetningsaktiver = getRegnskabstal(nl, ns, 'CurrentAssets')
    aktiver.faerdiggjorteudviklingsprojekter = getRegnskabstal(nl, ns, 'CompletedDevelopmentProjects')

    aktiver.aktiver = getRegnskabstal(nl, ns, 'Assets')

    Passiver passiver = data.balance.passiver
    passiver.gaeldsforpligtelser = getRegnskabstal(nl, ns, 'LiabilitiesOtherThanProvisions','CurrentLiabilities' )
    passiver.egenkapital = getRegnskabstal(nl, ns, 'Equity' )
    passiver.udbytte = getRegnskabstal(nl, ns, 'ProposedDividendRecognisedInEquity' )
    passiver.virksomhedskapital = getRegnskabstal(nl, ns, 'ContributedCapital')
    passiver.overfoertresultat = getRegnskabstal(nl, ns, 'RetainedEarnings')
    passiver.hensaettelserforudskudtskat = getRegnskabstal(nl, ns, 'ProvisionsForDeferredTax')
    passiver.hensatteforpligtelser = getRegnskabstal(nl, ns, 'Provisions')
    passiver.gaeldtilrealkredit = getRegnskabstal(nl, ns, 'LongtermMortgageDebt')
    passiver.langfristedegaeldsforpligtelser = getRegnskabstal(nl, ns, 'LongtermLiabilitiesOtherThanProvisions')
    passiver.kortsigtedegaeldsforpligtelser = getRegnskabstal(nl, ns, 'ShorttermPartOfLongtermLiabilitiesOtherThanProvisions')
    passiver.gaeldsforpligtelsertilpengeinstitutter = getRegnskabstal(nl, ns, 'ShorttermDebtToBanks')
    passiver.leverandoereraftjenesteydelser = getRegnskabstal(nl, ns, 'ShorttermTradePayables')
    passiver.gaeldtiltilknyttedevirksomheder = getRegnskabstal(nl, ns, 'ShorttermPayablesToGroupEnterprises')
    passiver.kortfristetskyldigskat =  getRegnskabstal(nl, ns, 'ShorttermTaxPayables')
    passiver.andregaeldsforpligtelser = getRegnskabstal(nl, ns, 'OtherShorttermPayables')
    passiver.periodeafgraensningsposter = getRegnskabstal(nl, ns, 'ShorttermDeferredIncome')
    passiver.kortfristedegaeldsforpligtelserialt = getRegnskabstal(nl, ns, 'ShorttermLiabilitiesOtherThanProvisions')
    passiver.passiverialt = getRegnskabstal(nl, ns, 'LiabilitiesAndEquity')
    passiver.andrehensaettelser = getRegnskabstal(nl, ns, 'OtherProvisions')
    passiver.andenlangfristetgaeld = getRegnskabstal(nl, ns, 'OtherLongtermPayables')
    passiver.modtagneforudbetalingerfrakunder = getRegnskabstal(nl, ns, 'ShorttermPrepaymentsReceivedFromCustomers')
    passiver.deposita = getRegnskabstal(nl, ns, 'DepositsLongtermLiabilitiesOtherThanProvisions')
    passiver.igangvaerendearbejderforfremmedregning = getRegnskabstal(nl, ns, 'ShorttermContractWorkInProgressLiabilities')

    // det aktuelle regnskab har også revisionsoplysninger
    if (nyeste) {
      berigMedRevision(data, regnskabNodes)
      berigUnderskrivere(data.revision, regnskabNodes)
    }

    fixSkat(data)

    return true
  }

  private void berigResultatopgoerelse(Regnskab data, NodeList nl, Namespace ns) {

    /** Resultatopgørelsen **/
    Resultatopgoerelse r = data.resultatopgoerelse

    //Omsætning
    r.omsaetningTal.omsaetning = getRegnskabstal(nl, ns, "Revenue")
    r.omsaetningTal.vareforbrug = getRegnskabstal(nl, ns, "CostOfSales")
    // driftsindtæger
    r.omsaetningTal.driftsindtaegter = getRegnskabstal(nl, ns, "OtherOperatingIncome")
    // andre eksterne omkostninger
    r.omsaetningTal.andreeksterneomkostninger = getRegnskabstal(nl, ns, "OtherExternalExpenses")
    r.omsaetningTal.variableomkostninger = getRegnskabstal(nl, ns, "RawMaterialsAndConsumablesUsed")
    r.omsaetningTal.lokalomkostninger = getRegnskabstal(nl, ns, "PropertyCost")
    r.omsaetningTal.eksterneomkostninger = getRegnskabstal(nl, ns, "ExternalExpenses")

    //BruttoresultatTal
    r.bruttoresultatTal.bruttofortjeneste = getRegnskabstal(nl, ns, "GrossProfitLoss", "GrossResult", "GrossProfit")
    r.bruttoresultatTal.medarbejderomkostninger = getRegnskabstal(nl, ns, "EmployeeBenefitsExpense")
    // regnskabsmæssige afskrivninger
    r.bruttoresultatTal.regnskabsmaessigeafskrivninger = getRegnskabstal(nl, ns,
      "DepreciationAmortisationExpenseAndImpairmentLossesOfPropertyPlantAndEquipmentAndIntangibleAssetsRecognisedInProfitOrLoss")
    r.bruttoresultatTal.administrationsomkostninger = getRegnskabstal(nl, ns, "AdministrativeExpenses")


    // NettoresultatTal
    r.nettoresultatTal.finansielleomkostninger = getRegnskabstal(nl, ns, "OtherFinanceExpenses", "FinanceCosts",
      "RestOfOtherFinanceExpenses")
    r.nettoresultatTal.driftsresultat = getRegnskabstal(nl, ns, "ProfitLossFromOrdinaryOperatingActivities",
      "ProfitLossFromOperatingActivities")
    r.nettoresultatTal.finansielleindtaegter = getRegnskabstal(nl, ns, "OtherFinanceIncome", "FinanceIncome", "IncomeFromOtherLongtermInvestmentsAndReceivables")
    r.nettoresultatTal.kapitalandeleiassocieredevirksomheder = getRegnskabstal(nl, ns, "IncomeFromInvestmentsInAssociates")
    r.nettoresultatTal.kapitalandeleitilknyttedevirksomheder = getRegnskabstal(nl, ns, "IncomeFromInvestmentsInGroupEnterprises")

    // Årets resultat
    r.aaretsresultatTal.aaretsresultat = getRegnskabstal(nl, ns, "ProfitLoss")
    r.aaretsresultatTal.resultatfoerskat = getRegnskabstal(nl, ns, "ProfitLossFromOrdinaryActivitiesBeforeTax", "ProfitLossBeforeTax")
    r.aaretsresultatTal.skatafaaretsresultat = getRegnskabstal(nl, ns, "TaxExpenseOnOrdinaryActivities", "TaxExpense",
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

  Regnskabstal getRegnskabstal(NodeList nodeList, Namespace ns, String ...nodeName){
    Node node

    nodeName.each {
      if (node) return
      String nodenameMedPrefix = "$ns.prefix:$it"
      node = nodeList.find {
        it.name().toString() == nodenameMedPrefix
      }
    }

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
      return new Regnskabstal(getAmount(node), decimal)
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
