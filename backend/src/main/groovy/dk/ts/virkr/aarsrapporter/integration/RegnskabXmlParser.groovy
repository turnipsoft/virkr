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
    String fsaNamespace = getFSANamespace(xml)

    Namespace ns = new Namespace("http://xbrl.dcca.dk/fsa", fsaNamespace)

    String contextRef = hentContextRef(result)

    // hent de relevante felter for dette regnskabsår fra contexten.
    NodeList nl = result.findAll {
      it.attribute('contextRef') == contextRef
    }


    nl.each {
      println(it.name())
    }


    data.bruttofortjeneste = getLongValue(nl, ns, "GrossProfitLoss")
    if (!data.bruttofortjeneste) {
      data.bruttofortjeneste = getLongValue(nl, ns, "GrossResult")
    }

    data.driftsresultat = getLongValue(nl, ns, "ProfitLossFromOrdinaryOperatingActivities")
    data.resultatfoerskat = getLongValue(nl, ns, "ProfitLossFromOrdinaryActivitiesBeforeTax")
    data.aaretsresultat = getLongValue(nl, ns, "ProfitLoss")
    data.finansielleOmkostninger = getLongValue(nl, ns, "OtherFinanceExpenses")

    data.skatafaaretsresultat = getLongValue(nl, ns, "TaxExpenseOnOrdinaryActivities")
    // prøver tax expense hvis den anden ikke er der
    if (!data.skatafaaretsresultat) {
      data.skatafaaretsresultat = getLongValue(nl, ns, "TaxExpense")
    }

    // findes i hele dokumentet denne har en anden context ref som er slutdato på perioden, skal evt. refactores til at
    // finde gennem denne
    data.gaeldsforpligtelser = getValue(result,ns.LiabilitiesOtherThanProvisions)
    data.egenkapital = getValue(result, ns.Equity)

    // fix up evt. manglende data med lidt matematik
    // resultat før skat mangler og kan udregnes ved at trække finansielle omkostning fra driftsresultatet
    if (!data.resultatfoerskat && data.finansielleOmkostninger && data.driftsresultat) {
      data.resultatfoerskat = data.driftsresultat - data.finansielleOmkostninger
    }

    return data
  }

  Long getLongValue(NodeList nodeList, Namespace ns, String nodeName){
    nodeName = "$ns.prefix:$nodeName"
    Node n = nodeList.find {
      it.name() == nodeName
    }

    if (n!=null) {
      return getAmount(n)
    }

    return null
  }

  String hentContextRef(Node xmlDokument) {
    Namespace c = new Namespace("http://xbrl.dcca.dk/gsd", "c")
    NodeList n = xmlDokument[c.InformationOnTypeOfSubmittedReport]
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
    int idx = xml.indexOf("http://xbrl.dcca.dk/fsa")
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
