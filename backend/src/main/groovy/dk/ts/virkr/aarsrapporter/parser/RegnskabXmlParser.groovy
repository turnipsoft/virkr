package dk.ts.virkr.aarsrapporter.parser

import dk.ts.virkr.aarsrapporter.model.RegnskabData
import dk.ts.virkr.aarsrapporter.model.virksomhedsdata.Virksomhedsdata
import dk.ts.virkr.aarsrapporter.model.Resultatopgoerelse
import dk.ts.virkr.aarsrapporter.parser.berigelse.RegnskabBerigelse
import groovy.xml.Namespace


/**
 * Created by sorenhartvig on 22/06/16.
 */
class RegnskabXmlParser {

  Virksomhedsdata hentVirksomhedsdataFraRegnskab(String xml, RegnskabData regnskabData) {
    XmlParser parser = new XmlParser(false, false)
    Node result = parser.parseText(xml)
    Namespace ns = hentNamespace(xml)

    String contextRef = hentContextRef(result, ns, regnskabData)
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

  RegnskabData parseOgBerig(RegnskabData data, String xml) {
    XmlParser parser = new XmlParser(false, false)

    Node result = parser.parseText(xml)
    Namespace ns = hentNamespace(xml)

    String contextRef = hentContextRef(result, ns, data)

    String assetsRef = null

    // IFRS regnskaber vil have contexten som duration_CY_C_only hvor C'et står for consolidated og altså en context
    // der dækker en hel gruppe, men vi er ikke interesserede i hele gruppens nøgle tal eller måsker er man
    // i stedet vil man have parentens og det er så duration_CY_only
    // Så for nu, overskrives denne til jeg bliver klogere.
    if (contextRef.equals("duration_CY_C_only")) {
      contextRef = 'duration_CY_only'
    }

    // hent de relevante felter for dette regnskabsår fra contexten.
    NodeList nl = result.findAll {
      it.attribute('contextRef') == contextRef
    }


    /*nl.each {
      println(it.name())
    }*/

    /*
    if (ns.prefix == 'ifrs-full') {
      return haandterIFRS(ns, result, nl, data)
    }
    */

    /** Resultatopgørelsen **/
    Resultatopgoerelse r = data.resultatopgoerelse

    //Omsætning
    r.omsaetningTal.omsaetning = getLongValue(nl, ns, "Revenue")
    r.omsaetningTal.vareforbrug = getLongValue(nl, ns, "CostOfSales")
    // driftsindtæger
    r.omsaetningTal.driftsindtaegter = getLongValue(nl, ns, "OtherOperatingIncome")
    // andre eksterne omkostninger
    r.omsaetningTal.andreeksterneomkostninger = getLongValue(nl, ns, "OtherExternalExpenses")
    r.omsaetningTal.variableomkostninger = getLongValue(nl, ns, "RawMaterialsAndConsumablesUsed")
    r.omsaetningTal.lokalomkostninger = getLongValue(nl, ns, "PropertyCost")
    r.omsaetningTal.eksterneomkostninger = getLongValue(nl,ns, "ExternalExpenses")

    //BruttoresultatTal
    r.bruttoresultatTal.bruttofortjeneste = getLongValue(nl, ns, "GrossProfitLoss", "GrossResult", "GrossProfit")
    r.bruttoresultatTal.medarbejderomkostninger = getLongValue(nl, ns, "EmployeeBenefitsExpense")
    // regnskabsmæssige afskrivninger
    r.bruttoresultatTal.regnskabsmaessigeafskrivninger = getLongValue(nl, ns,
      "DepreciationAmortisationExpenseAndImpairmentLossesOfPropertyPlantAndEquipmentAndIntangibleAssetsRecognisedInProfitOrLoss")
    r.bruttoresultatTal.administrationsomkostninger = getLongValue(nl, ns, "AdministrativeExpenses")
    r.bruttoresultatTal.kapitalandeleiassocieredevirksomheder = getLongValue(nl, ns, "IncomeFromInvestmentsInAssociates",
      "IncomeFromInvestmentsInGroupEnterprises")

    // NettoresultatTal
    r.nettoresultatTal.finansielleomkostninger = getLongValue(nl, ns, "OtherFinanceExpenses", "FinanceCosts",
      "RestOfOtherFinanceExpenses")
    r.nettoresultatTal.driftsresultat = getLongValue(nl, ns, "ProfitLossFromOrdinaryOperatingActivities",
      "ProfitLossFromOperatingActivities")
    r.nettoresultatTal.finansielleindtaegter = getLongValue(nl, ns, "OtherFinanceIncome", "FinanceIncome")

    // Årets resultat
    r.aaretsresultatTal.aaretsresultat = getLongValue(nl, ns, "ProfitLoss")
    r.aaretsresultatTal.resultatfoerskat = getLongValue(nl, ns, "ProfitLossFromOrdinaryActivitiesBeforeTax", "ProfitLossBeforeTax")
    r.aaretsresultatTal.skatafaaretsresultat = getLongValue(nl, ns, "TaxExpenseOnOrdinaryActivities", "TaxExpense",
      "IncomeTaxExpenseContinuingOperations")


    // passiver
    // findes i hele dokumentet denne har en anden context ref som er slutdato på perioden, skal evt. refactores til at
    // finde gennem denne
    data.balance.passiver.gaeldsforpligtelser = getValue(result,ns.LiabilitiesOtherThanProvisions, ns.CurrentLiabilities)

    if (!data.balance.passiver.gaeldsforpligtelser) {
      data.balance.passiver.gaeldsforpligtelser = getValue(result, ns.ShorttermLiabilitiesOtherThanProvisions)
    }

    data.balance.passiver.egenkapital = getValue(result, ns.Equity)

    berigRegnskabdataMedManglendeNoegletal(data)

    return data
  }

  /**
   * Beriger nøgletal rekursivt indtil der ikke er flere nøgletal at berige
   * @param data
   */
  void berigRegnskabdataMedManglendeNoegletal(RegnskabData data) {
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
      ns = new Namespace("http://xbrl.ifrs.org/taxonomy/2014-03-05/ifrs-full", namespace)
    }
    ns
  }

  RegnskabData haandterIFRS(Namespace ns, Node xmlRoot, NodeList nl, RegnskabData data) {
    // TBD
    return data
  }

  Long getLongValue(NodeList nodeList, Namespace ns, String nodeName, String altNodename = null,
                    String altNodeName2 =  null){
    nodeName = "$ns.prefix:$nodeName"
    Node n = nodeList.find {
      it.name() == nodeName
    }

    if (n!=null) {
      return getAmount(n)
    } else if (altNodename) {
      Long val = getLongValue(nodeList, ns, altNodename)
      if (!val) {
        return getLongValue(nodeList, ns, altNodeName2)
      }
      return val
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


  String hentContextRef(Node xmlDokument, Namespace ns, RegnskabData regnskabData) {

    NodeList contextNodes = xmlDokument['context'];
    if (!contextNodes) {
      String namespacenavn = hentNamespaceNavn(xmlDokument, "http://www.xbrl.org/2003/instance")
      Namespace nsc = new Namespace("http://www.xbrl.org/2003/instance", namespacenavn)
      contextNodes = xmlDokument[nsc.context];
    }

    // find den profit hvis contextRef ikke er konsolideret og som har nyeste periode.
    if (ns.uri=='http://xbrl.dcca.dk/fsa') {
      NodeList n = xmlDokument[ns.ProfitLoss]
      if (n != null && n.size() > 0) {
        Node contextRefNode
        n.each {
          String contextRefCandidate = it.attribute("contextRef")
          Node contextRefNodeCandidate = contextNodes.find { it.attribute('id') == contextRefCandidate }
          if ((!contextRefNode ||
            (contextRefNodeCandidate.period.endDate.text() > contextRefNode.period.endDate.text()) && !contextRefNodeCandidate.scenario)) {
            if (!contextRefNodeCandidate.scenario) {
              contextRefNode = contextRefNodeCandidate
            }
          }
        }
        if (contextRefNode) {
          return contextRefNode.attribute('id')
        }
      }
    }

    // find korrekte namespaces
    Namespace gsd = new Namespace("http://xbrl.dcca.dk/gsd", hentNamespaceNavn(xmlDokument, "http://xbrl.dcca.dk/gsd"))

    NodeList n = xmlDokument[gsd.InformationOnTypeOfSubmittedReport]

    Node n1 = n.get(0)
    String contextRef = n1.attribute("contextRef")

    return contextRef
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
    return xmlDokument.attributes().find { attribute->
      attribute.value == namespace
    }.key.substring(6)
  }

  String getGSDNamespace(String xml) {
    return getNamespace(xml, "http://xbrl.dcca.dk/gsd")
  }

  String getFSANamespace(String xml) {
    return getNamespace(xml, "http://xbrl.dcca.dk/fsa")
  }

  String getIFRSNamespace(String xml) {
    return getNamespace(xml, "http://xbrl.ifrs.org/taxonomy/2014-03-05/ifrs-full")
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
