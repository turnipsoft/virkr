package dk.ts.virkr.aarsrapporter.integration

import dk.ts.virkr.aarsrapporter.integration.model.regnskabdata.RegnskabData
import groovy.xml.Namespace


/**
 * Created by sorenhartvig on 22/06/16.
 */
class RegnskabXmlParser {
  RegnskabData parseOgBerig(RegnskabData data, String xml) {
    XmlParser parser = new XmlParser(false, false)

    Node result = parser.parseText(xml)
    String namespace = getFSANamespace(xml)
    Namespace ns = null

    if (namespace) {
      ns = new Namespace("http://xbrl.dcca.dk/fsa", namespace)
    } else {
      // prøv ifrs
      namespace = getIFRSNamespace(xml)
      ns = new Namespace("http://xbrl.ifrs.org/taxonomy/2014-03-05/ifrs-full", namespace)
    }

    String contextRef = hentContextRef(result)

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

    /*
    nl.each {
      println(it.name())
    }*/

    if (ns.prefix == 'ifrs-full') {
      return haandterIFRS(ns, result, nl, data)
    }

    data.omsaetning = getLongValue(nl, ns, "Revenue")
    data.goodwill = getLongValue(nl, ns, "Goodwill")
    data.bruttofortjeneste = getLongValue(nl, ns, "GrossProfitLoss", "GrossResult")
    if (!data.bruttofortjeneste) {
      data.bruttofortjeneste = getLongValue(nl, ns, "GrossProfit")
    }

    data.medarbejderOmkostninger = getLongValue(nl, ns, "EmployeeBenefitsExpense")
    data.driftsresultat = getLongValue(nl, ns, "ProfitLossFromOrdinaryOperatingActivities",
      "ProfitLossFromOperatingActivities")

    data.resultatfoerskat = getLongValue(nl, ns, "ProfitLossFromOrdinaryActivitiesBeforeTax", "ProfitLossBeforeTax")

    data.aaretsresultat = getLongValue(nl, ns, "ProfitLoss")

    data.finansielleOmkostninger = getLongValue(nl, ns, "OtherFinanceExpenses", "FinanceCosts")
    data.finansielleIndtaegter = getLongValue(nl, ns, "OtherFinanceIncome", "FinanceIncome")

    data.skatafaaretsresultat = getLongValue(nl, ns, "TaxExpenseOnOrdinaryActivities", "TaxExpense")
    if (!data.skatafaaretsresultat) {
      data.skatafaaretsresultat = getLongValue(nl, ns, "IncomeTaxExpenseContinuingOperations")
    }

    // findes i hele dokumentet denne har en anden context ref som er slutdato på perioden, skal evt. refactores til at
    // finde gennem denne
    data.gaeldsforpligtelser = getValue(result,ns.LiabilitiesOtherThanProvisions, ns.CurrentLiabilities)

    if (!data.gaeldsforpligtelser) {
      data.gaeldsforpligtelser = getValue(result, ns.ShorttermLiabilitiesOtherThanProvisions)
    }

    data.egenkapital = getValue(result, ns.Equity)

    // fix up evt. manglende data med lidt matematik
    // resultat før skat mangler og kan udregnes ved at trække finansielle omkostning fra driftsresultatet
    if (!data.resultatfoerskat && data.driftsresultat) {
      long finans = 0
      finans = (data.finansielleIndtaegter?data.finansielleIndtaegter:0) - (data.finansielleOmkostninger?data.finansielleOmkostninger:0)
      data.resultatfoerskat = data.driftsresultat + finans
    }

    // hvis der ikke er noget bruttoresultat kan man tilsyneladende bruge goodwill i sit regnskab.. hmmm
    if (!data.bruttofortjeneste && data.driftsresultat && data.goodwill) {
      data.bruttofortjeneste = data.driftsresultat + data.goodwill
    }

    return data
  }

  RegnskabData haandterIFRS(Namespace ns, Node xmlRoot, NodeList nl, RegnskabData data) {
    data.bruttofortjeneste = getLongValue(nl, ns, "ProfitLossFromOperatingActivities")
    data.resultatfoerskat = getLongValue(nl, ns, "ProfitLossBeforeTax")
    data.aaretsresultat = getLongValue(nl, ns, "ProfitLoss")
    data.finansielleIndtaegter = getLongValue(nl, ns, "FinanceIncome")
    data.finansielleOmkostninger = getLongValue(nl, ns, "FinanceCosts")

    return data
  }

  Long getLongValue(NodeList nodeList, Namespace ns, String nodeName, String altNodename = null){
    nodeName = "$ns.prefix:$nodeName"
    Node n = nodeList.find {
      it.name() == nodeName
    }

    if (n!=null) {
      return getAmount(n)
    } else if (altNodename) {
      return getLongValue(nodeList, ns, altNodename)
    }

    return null
  }

  String hentContextRef(Node xmlDokument) {
    Namespace c = new Namespace("http://xbrl.dcca.dk/gsd", "c")
    Namespace gsd = new Namespace("http://xbrl.dcca.dk/gsd", "gsd")

    NodeList n = xmlDokument[c.InformationOnTypeOfSubmittedReport]

    if (n.size()==0) {
      n = xmlDokument[gsd.InformationOnTypeOfSubmittedReport]
    }
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
    return Long.valueOf(amount)
  }
}
