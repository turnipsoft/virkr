package dk.ts.virkr.aarsrapporter.parser

import dk.ts.virkr.aarsrapporter.model.Generalforsamling
import dk.ts.virkr.aarsrapporter.model.Regnskab
import dk.ts.virkr.aarsrapporter.model.RegnskabData
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

  Virksomhedsdata hentVirksomhedsdataFraRegnskab(String xml) {
    XmlParser parser = new XmlParser(false, false)
    Node result = parser.parseText(xml)
    Namespace ns = hentNamespace(xml)

    String contextRef = hentContextRef(result, ns)
    // hent de relevante felter for dette regnskabsår fra contexten.
    NodeList nl = result.findAll {
      it.attribute('contextRef') == contextRef
    }

    String gsdNamespace = getGSDNamespace(xml)
    ns = new Namespace("http://xbrl.dcca.dk/gsd", gsdNamespace)

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

  void berigMedPeriode(Regnskab regnskab, Node xmlDokument, Namespace ns, String contextRefName) {
    List<Node> contextNodes = hentContextNodes(xmlDokument, ns)
    Node contextRefNode = contextNodes.find { it.attribute('id') == contextRefName }
    String nsPrefix = getNSPrefix(contextRefNode)

    String startDate = contextRefNode[nsPrefix+'period'][nsPrefix+'startDate'].text()
    String endDate = contextRefNode[nsPrefix+'period'][nsPrefix+'endDate'].text()
    regnskab.startdato = startDate
    regnskab.slutdato = endDate
    regnskab.aar = startDate.substring(0,4)
  }

  boolean parseOgBerig(Regnskab data, String xml, boolean nyeste = true) {
    XmlParser parser = new XmlParser(false, false)

    Node result = parser.parseText(xml)
    Namespace ns = hentNamespace(xml)

    String contextRef = hentContextRef(result, ns, nyeste)

    // IFRS regnskaber vil have contexten som duration_CY_C_only hvor C'et står for consolidated og altså en context
    // der dækker en hel gruppe, men vi er ikke interesserede i hele gruppens nøgle tal eller måsker er man
    // i stedet vil man have parentens og det er så duration_CY_only
    // Så for nu, overskrives denne til jeg bliver klogere.
    //if (contextRef.equals("duration_CY_C_only")) {
    //  contextRef = 'duration_CY_only'
    //}

    if (!contextRef) {
      return false
    }

    // hent de relevante felter for dette regnskabsår fra contexten.
    NodeList nl = result.findAll {
      it.attribute('contextRef') == contextRef
    }

    /* periode dato år */
    berigMedPeriode(data, result, ns, contextRef)

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
    r.omsaetningTal.eksterneomkostninger = getRegnskabstal(nl,ns, "ExternalExpenses")

    //BruttoresultatTal
    r.bruttoresultatTal.bruttofortjeneste = getRegnskabstal(nl, ns, "GrossProfitLoss", "GrossResult", "GrossProfit")
    r.bruttoresultatTal.medarbejderomkostninger = getRegnskabstal(nl, ns, "EmployeeBenefitsExpense")
    // regnskabsmæssige afskrivninger
    r.bruttoresultatTal.regnskabsmaessigeafskrivninger = getRegnskabstal(nl, ns,
      "DepreciationAmortisationExpenseAndImpairmentLossesOfPropertyPlantAndEquipmentAndIntangibleAssetsRecognisedInProfitOrLoss")
    r.bruttoresultatTal.administrationsomkostninger = getRegnskabstal(nl, ns, "AdministrativeExpenses")
    r.bruttoresultatTal.kapitalandeleiassocieredevirksomheder = getRegnskabstal(nl, ns, "IncomeFromInvestmentsInAssociates",
      "IncomeFromInvestmentsInGroupEnterprises")

    // NettoresultatTal
    r.nettoresultatTal.finansielleomkostninger = getRegnskabstal(nl, ns, "OtherFinanceExpenses", "FinanceCosts",
      "RestOfOtherFinanceExpenses")
    r.nettoresultatTal.driftsresultat = getRegnskabstal(nl, ns, "ProfitLossFromOrdinaryOperatingActivities",
      "ProfitLossFromOperatingActivities")
    r.nettoresultatTal.finansielleindtaegter = getRegnskabstal(nl, ns, "OtherFinanceIncome", "FinanceIncome")

    // Årets resultat
     r.aaretsresultatTal.aaretsresultat = getRegnskabstal(nl, ns, "ProfitLoss")
    r.aaretsresultatTal.resultatfoerskat = getRegnskabstal(nl, ns, "ProfitLossFromOrdinaryActivitiesBeforeTax", "ProfitLossBeforeTax")
    r.aaretsresultatTal.skatafaaretsresultat = getRegnskabstal(nl, ns, "TaxExpenseOnOrdinaryActivities", "TaxExpense",
      "IncomeTaxExpenseContinuingOperations")

    // regnskabsklasse
    data.regnskabsklasse = getStringValue(nl, ns,'ClassOfReportingEntity' )

    // passiver
    // findes i hele dokumentet denne har en anden context ref som er slutdato på perioden, skal evt. refactores til at
    // finde gennem denne
    data.balance.passiver.gaeldsforpligtelser = new Regnskabstal(getValue(result,ns.LiabilitiesOtherThanProvisions, ns.CurrentLiabilities))

    if (!data.balance.passiver.gaeldsforpligtelser) {
      data.balance.passiver.gaeldsforpligtelser = new Regnskabstal(getValue(result, ns.ShorttermLiabilitiesOtherThanProvisions))
    }

    data.balance.passiver.egenkapital = new Regnskabstal(getValue(result, ns.Equity))

    if (nyeste) {
      berigMedRevision(data, xml)
    }

    return true
    //berigRegnskabdataMedManglendeNoegletal(data)
  }

  /**
   * Beriger nøgletal rekursivt indtil der ikke er flere nøgletal at berige
   * @param data
   */
  void berigRegnskabdataMedManglendeNoegletal(Regnskab data) {
    RegnskabBerigelse regnskabBerigelse = new RegnskabBerigelse()
    boolean harBeriget = regnskabBerigelse.berigNoegletal(data)
    while (harBeriget) {
      harBeriget = regnskabBerigelse.berigNoegletal(data)
    }
  }

  private berigMedRevision(Regnskab regnskab, String xml) {
    XmlParser parser = new XmlParser(false, false)

    Revision revision = new Revision()
    NodeList nl
    Node xmlDokument = parser.parseText(xml)
    Namespace ns = hentNamespace(xml, CMN_NAMESPACE)

    if (ns) {
      // hent de relevante fra CMN NAMESPACE
      nl = xmlDokument.findAll {
        it.name().toString().startsWith(ns.prefix)
      }

      revision.assistancetype = getStringValue(nl, ns, 'TypeOfAuditorAssistance')
      revision.revisionsfirmaNavn = getStringValue(nl, ns, 'NameOfAuditFirm')
      revision.navnPaaRevisor = getStringValue(nl, ns, 'NameAndSurnameOfAuditor')
      revision.beskrivelseAfRevisor = getStringValue(nl, ns, 'DescriptionOfAuditor')
      revision.revisionsfirmaCvrnummer = getStringValue(nl, ns, 'IdentificationNumberCvrOfAuditFirm')
      revision.mnenummer = getStringValue(nl, ns, 'IdentificationNumberOfAuditor')
    }

    ns = hentNamespace(xml, GSD_NAMESPACE)

    // hent de relevante fra GSD
    nl = xmlDokument.findAll {
      it.name().toString().startsWith(ns.prefix)
    }

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

    ns = hentNamespace(xml, ARR_NAMESPACE)

    // hent de relevante fra ARR
    nl = xmlDokument.findAll {
      it.name().toString().startsWith(ns.prefix)
    }

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

    // findes ikke i IFRS regnskaber
    ns = hentNamespace(xml, SOB_NAMESPACE)

    if (ns) {
      // hent de relevante fra SOB
      nl = xmlDokument.findAll {
        it.name().toString().startsWith(ns.prefix)
      }

      revision.godkendelsesdato = getStringValue(nl, ns, 'DateOfApprovalOfAnnualReport')
      revision.ingenRevision = getStringValue(nl, ns, 'StatementOnOptingOutOfAuditingFinancialStatementsInNextReportingPeriodDueToExemption')

    }

    regnskab.revision = revision
  }

  Namespace hentNamespace(String xml) {
    String namespace = getFSANamespace(xml)
    Namespace ns = null

    if (namespace) {
      ns = new Namespace("http://xbrl.dcca.dk/fsa", namespace)
    } else {
      // prøv ifrs, der er vist lige noget fishy her, som skal refactores
      namespace = getIFRSNamespace(xml)
      if (namespace) {
        ns = new Namespace("http://xbrl.ifrs.org/taxonomy/2014-03-05/ifrs-full", namespace)
      }
    }
    ns
  }

  static final String CMN_NAMESPACE = 'http://xbrl.dcca.dk/cmn'
  static final String ARR_NAMESPACE = 'http://xbrl.dcca.dk/arr'
  static final String GSD_NAMESPACE = 'http://xbrl.dcca.dk/gsd'
  static final String SOB_NAMESPACE = 'http://xbrl.dcca.dk/sob'
  static final String FSA_NAMESPACE = 'http://xbrl.dcca.dk/fsa'

  Namespace hentNamespace(String xml, String namespaceUrl) {
    String namespace = getNamespace(xml, namespaceUrl)
    if (namespace) {
      Namespace ns = new Namespace(namespaceUrl, namespace)
      return  ns
    }

    return null
  }

  Regnskabstal getRegnskabstal(NodeList nodeList, Namespace ns, String nodeName, String altNodename = null,
                               String altNodeName2 =  null){
    nodeName = "$ns.prefix:$nodeName"
    Node n = nodeList.find {
      it.name() == nodeName
    }

    if (n!=null) {
      return new Regnskabstal(getAmount(n))
    } else if (altNodename) {
      Regnskabstal val = getRegnskabstal(nodeList, ns, altNodename)
      if (!val.vaerdi) {
        return getRegnskabstal(nodeList, ns, altNodeName2)
      }
      return val
    }

    return new Regnskabstal()
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

  Long extractLong(String s) {
    String i = ""
    s.chars.each {
      if (it.toString().isNumber()) {
        i+=it.toString()
      }

    }

    if (i.length()==0) {
      return 0
    }

    return i.toLong()
  }

  NodeList hentContextNodes(Node xmlDokument, Namespace ns) {
    NodeList contextNodes = xmlDokument['context'];
    if (!contextNodes) {
      String namespacenavn = hentNamespaceNavn(xmlDokument, "http://www.xbrl.org/2003/instance")
      Namespace nsc = new Namespace("http://www.xbrl.org/2003/instance", namespacenavn)
      contextNodes = xmlDokument[nsc.context];
    }

    return contextNodes
  }

  String hentContextRef(Node xmlDokument, Namespace ns, boolean nyeste = true) {

    NodeList contextNodes = hentContextNodes(xmlDokument, ns)

    // find den profit hvis contextRef ikke er konsolideret og som har nyeste periode.
    if (true || ns.uri=='http://xbrl.dcca.dk/fsa') {
      NodeList n = xmlDokument[ns.ProfitLoss]
      if (!n) {
        // backup til GrossProfitLoss
        n = xmlDokument[ns.GrossProfitLoss]
      }
      if (!n) {
        // backup til GrossResult
        n = xmlDokument[ns.GrossResult]
      }

      if (n != null && n.size() > 0) {
        List<Node> contextRefNodeCandidates = []
        n.each {
          String contextRefCandidate = it.attribute("contextRef")
          Node contextRefNodeCandidate = contextNodes.find { it.attribute('id') == contextRefCandidate }
          // skal ikke have dem der har scenario på i FSA og ikke have de konsoliderede i IFRS'erne
          String contextRefNodeCandidateName = contextRefNodeCandidate.attribute('id')
          if (contextRefNodeCandidate && !contextRefNodeCandidate.scenario && !contextRefNodeCandidateName.contains('_C_')) {
            Node existing = contextRefNodeCandidates.find {
              String name = it.name()
              String ens = name.contains(':')? name.substring(0, name.indexOf(':')+1) : ''
              String e1 = it[ens+'period'][ens+'endDate'].text()
              String e2 = contextRefNodeCandidate[ens+'period'][ens+'endDate'].text()
              e1==e2
            }
            // tilføjer kun hvis den ikke allerede eksisterer i forvejen
            if (!existing) {
              contextRefNodeCandidates << contextRefNodeCandidate
            }
          }
        }

        String name = contextRefNodeCandidates[0].name()
        String ens = name.contains(':')? name.substring(0, name.indexOf(':')+1) : ''

        contextRefNodeCandidates = contextRefNodeCandidates.sort {it[ens+'period'][ens+'endDate'].text() }
        if (nyeste) {
          return contextRefNodeCandidates.last().attribute('id')
        } else {
          if (contextRefNodeCandidates.size()>1) {
            return contextRefNodeCandidates.get(contextRefNodeCandidates.size()-2).attribute('id');
          } else {
            return null;
          }
        }
      }
    }

    // find korrekte namespaces
    //Namespace gsd = new Namespace("http://xbrl.dcca.dk/gsd", hentNamespaceNavn(xmlDokument, "http://xbrl.dcca.dk/gsd"))

    //NodeList n = xmlDokument[gsd.InformationOnTypeOfSubmittedReport]

    //Node n1 = n.get(0)
    //String contextRef = n1.attribute("contextRef")

    return null
    //return contextRef
  }

  String findNode(xmlnodes, n) {

    def nodes = xmlnodes[n]

    if (nodes && nodes.size() > 0) {
      Node node = nodes[0]
      return node[0]
    }

    return null

  }

  String hentNamespaceNavn(Node xmlDokument, String namespace) {
    String ns = xmlDokument.attributes().find { attribute->
      attribute.value == namespace && attribute.key!='xmlns'
    }.key

    return ns.substring(6)
  }

  String getGSDNamespace(String xml) {
    return getNamespace(xml, GSD_NAMESPACE)
  }

  String getFSANamespace(String xml) {
    return getNamespace(xml, FSA_NAMESPACE)
  }

  String getCMNNamespace(String xml) {
    return getNamespace(xml, CMN_NAMESPACE)
  }

  String getARRNamespace(String xml) {
    return getNamespace(xml, ARR_NAMESPACE)
  }

  String getSOBNamespace(String xml) {
    return getNamespace(xml, SOB_NAMESPACE)
  }

  String getIFRSNamespace(String xml) {
    String result = getNamespace(xml, "http://xbrl.ifrs.org/taxonomy/2014-03-05/ifrs-full")
    if (!result) {
      result = getNamespace(xml, "http://xbrl.dcca.dk/ifrs-dk-cor_2013-12-20")
    }

    if (!result) {
      result = getNamespace("http://xbrl.ifrs.org/taxonomy/2014-03-05/ifrs-full")
    }

    return result
  }

  String getNamespace(String xml, String ns) {
    int idx = xml.indexOf(ns)
    if (idx<0) {
      return null;
    }
    xml = xml.substring(0, idx)
    idx = xml.lastIndexOf('xmlns:')
    String namespace = xml.substring(idx + 6, xml.length() - 2)
    return namespace
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
    return Double.valueOf(amount).longValue()
  }
}
