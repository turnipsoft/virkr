package dk.ts.virkr.aarsrapporter.parser

import dk.ts.virkr.aarsrapporter.model.Regnskab
import dk.ts.virkr.aarsrapporter.model.RegnskabData
import dk.ts.virkr.aarsrapporter.model.Regnskabstal
import dk.ts.virkr.aarsrapporter.model.virksomhedsdata.Virksomhedsdata
import dk.ts.virkr.aarsrapporter.model.Resultatopgoerelse
import dk.ts.virkr.aarsrapporter.parser.berigelse.RegnskabBerigelse
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

  void parseOgBerig(Regnskab data, String xml, boolean nyeste = true) {
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


    // passiver
    // findes i hele dokumentet denne har en anden context ref som er slutdato på perioden, skal evt. refactores til at
    // finde gennem denne
    data.balance.passiver.gaeldsforpligtelser = new Regnskabstal(getValue(result,ns.LiabilitiesOtherThanProvisions, ns.CurrentLiabilities))

    if (!data.balance.passiver.gaeldsforpligtelser) {
      data.balance.passiver.gaeldsforpligtelser = new Regnskabstal(getValue(result, ns.ShorttermLiabilitiesOtherThanProvisions))
    }

    data.balance.passiver.egenkapital = new Regnskabstal(getValue(result, ns.Equity))

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

  private Namespace hentNamespace(String xml) {
    String namespace = getFSANamespace(xml)
    Namespace ns = null

    if (namespace) {
      ns = new Namespace("http://xbrl.dcca.dk/fsa", namespace)
    } else {
      // prøv ifrs
      namespace = getIFRSNamespace(xml)
      if (namespace) {
        ns = new Namespace("http://xbrl.ifrs.org/taxonomy/2014-03-05/ifrs-full", namespace)
      }
    }
    ns
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
    return getNamespace(xml, "http://xbrl.dcca.dk/gsd")
  }

  String getFSANamespace(String xml) {
    return getNamespace(xml, "http://xbrl.dcca.dk/fsa")
  }

  String getIFRSNamespace(String xml) {
    String result = getNamespace(xml, "http://xbrl.ifrs.org/taxonomy/2014-03-05/ifrs-full")
    if (!result) {
      result = getNamespace(xml, "http://xbrl.dcca.dk/ifrs-dk-cor_2013-12-20")
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
