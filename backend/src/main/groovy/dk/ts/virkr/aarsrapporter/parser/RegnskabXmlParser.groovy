package dk.ts.virkr.aarsrapporter.parser

import dk.ts.virkr.aarsrapporter.model.Generalforsamling
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

    data.balance.passiver.gaeldsforpligtelser = getRegnskabstal(nl, ns, 'LiabilitiesOtherThanProvisions','CurrentLiabilities', 'ShorttermLiabilitiesOtherThanProvisions' )
    data.balance.passiver.egenkapital = getRegnskabstal(nl, ns, 'Equity' )
    data.balance.passiver.udbytte = getRegnskabstal(nl, ns, 'ProposedDividendRecognisedInEquity' )

    // det aktuelle regnskab har også revisionsoplysninger
    if (nyeste) {
      berigMedRevision(data, regnskabNodes)
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
    XmlParser parser = new XmlParser(false, false)

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
      revision.grundlagForKonklusion = getStringValue(nl, ns, 'DescriptionOfQualificationsOfAuditedFinancialStatements')
      if (revision.grundlagForKonklusion && !revision.grundlagForKonklusion.toLowerCase().contains('forbehold')) {
        // kun hvis der står noget om forbehold
        revision.grundlagForKonklusion = null
      }
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
      return new Regnskabstal(getAmount(node))
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
    // så er der lige noget med decimalerne
    String decimaler = n.attribute('decimals')
    // man kan desværre ikke rigtig regne med de decimaler der.. hmmmm

    return vaerdi
  }
}
