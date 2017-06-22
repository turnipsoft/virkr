package dk.ts.virkr.aarsrapporter.integration

import dk.ts.virkr.aarsrapporter.integration.model.regnskabdata.RegnskabData
import dk.ts.virkr.aarsrapporter.integration.model.virksomhedsdata.Virksomhedsdata
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

  RegnskabData parseOgBerig(RegnskabData data, String xml) {
    XmlParser parser = new XmlParser(false, false)

    Node result = parser.parseText(xml)
    Namespace ns = hentNamespace(xml)

    String contextRef = hentContextRef(result, ns)

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

    if (ns.prefix == 'ifrs-full') {
      return haandterIFRS(ns, result, nl, data)
    }

    data.omsaetning = getLongValue(nl, ns, "Revenue")
    data.goodwill = getLongValue(nl, ns, "Goodwill")
    data.bruttofortjeneste = getLongValue(nl, ns, "GrossProfitLoss", "GrossResult", "GrossProfit")

    data.medarbejderOmkostninger = getLongValue(nl, ns, "EmployeeBenefitsExpense")
    data.driftsresultat = getLongValue(nl, ns, "ProfitLossFromOrdinaryOperatingActivities",
      "ProfitLossFromOperatingActivities")

    data.resultatfoerskat = getLongValue(nl, ns, "ProfitLossFromOrdinaryActivitiesBeforeTax", "ProfitLossBeforeTax")

    data.aaretsresultat = getLongValue(nl, ns, "ProfitLoss")

    data.finansielleOmkostninger = getLongValue(nl, ns, "OtherFinanceExpenses", "FinanceCosts",
      "RestOfOtherFinanceExpenses")

    data.finansielleIndtaegter = getLongValue(nl, ns, "OtherFinanceIncome", "FinanceIncome")

    data.skatafaaretsresultat = getLongValue(nl, ns, "TaxExpenseOnOrdinaryActivities", "TaxExpense",
      "IncomeTaxExpenseContinuingOperations")

    // vareforbrug
    data.vareforbrug = getLongValue(nl, ns, "CostOfSales")

    // driftsindtæger
    data.driftsindtaegter = getLongValue(nl, ns, "OtherOperatingIncome")

    // andre eksterne omkostninger
    data.andreEksterneOmkostninger = getLongValue(nl, ns, "OtherExternalExpenses")

    // regnskabsmæssige afskrivninger
    data.regnskabsmaessigeAfskrivninger = getLongValue(nl, ns,
      "DepreciationAmortisationExpenseAndImpairmentLossesOfPropertyPlantAndEquipmentAndIntangibleAssetsRecognisedInProfitOrLoss")

    data.variableOmkostninger = getLongValue(nl, ns, "RawMaterialsAndConsumablesUsed")

    data.lokalomkostninger = getLongValue(nl, ns, "PropertyCost")

    data.administrationsomkostninger = getLongValue(nl, ns, "AdministrativeExpenses")

    data.eksterneomkostninger = getLongValue(nl,ns, "ExternalExpenses")

    // FIXME: den ligger ikke i det namespace. den har sit eget, kig på scenarios
    data.udbytte = getLongValue(nl, ns, "ProposedDividend")

    // findes i hele dokumentet denne har en anden context ref som er slutdato på perioden, skal evt. refactores til at
    // finde gennem denne
    data.gaeldsforpligtelser = getValue(result,ns.LiabilitiesOtherThanProvisions, ns.CurrentLiabilities)

    if (!data.gaeldsforpligtelser) {
      data.gaeldsforpligtelser = getValue(result, ns.ShorttermLiabilitiesOtherThanProvisions)
    }

    data.egenkapital = getValue(result, ns.Equity)

    berigRegnskabdataMedManglendeNoegletal(data)

    return data
  }

  void berigRegnskabdataMedManglendeNoegletal(RegnskabData data) {

    // fix up evt. manglende data med lidt matematik
    // resultat før skat mangler og kan udregnes ved at trække finansielle omkostning fra driftsresultatet
    if (!data.resultatfoerskat && data.driftsresultat) {
      long finans = 0
      finans = (data.finansielleIndtaegter?data.finansielleIndtaegter:0) - (data.finansielleOmkostninger?data.finansielleOmkostninger:0)
      data.resultatfoerskat = data.driftsresultat + finans
    }

    if (!data.bruttofortjeneste) {
      if (data.andreEksterneOmkostninger && data.vareforbrug && data.driftsindtaegter && data.omsaetning) {
        data.bruttofortjeneste = data.omsaetning - data.andreEksterneOmkostninger - data.vareforbrug +
          data.driftsindtaegter
      }
    }

    if (!data.driftsresultat && data.bruttofortjeneste && data.medarbejderOmkostninger && data.regnskabsmaessigeAfskrivninger) {
      data.driftsresultat = data.bruttofortjeneste - data.medarbejderOmkostninger - data.regnskabsmaessigeAfskrivninger
    }

    forsoegBruttofortjeneste(data)
    berigMedDriftsresultat(data)
    berigMedBruttofortjeneste(data)

    println(data.bruttofortjeneste)
  }

  void berigMedDriftsresultat(RegnskabData data) {
    // forsøger at regne baglæns for at få driftsresultatet
    if (!data.driftsresultat) {
      if (data.aaretsresultat) {
        data.driftsresultat = (data.aaretsresultat + data.finansielleOmkostninger ?: 0) - data.finansielleIndtaegter ?: 0
      }
    }
  }

  void berigMedBruttofortjeneste(RegnskabData data) {
    if (!data.bruttofortjeneste) {
      if (data.driftsresultat) {
        data.bruttofortjeneste = data.driftsresultat + data.regnskabsmaessigeAfskrivninger?:0
        data.bruttofortjeneste = data.bruttofortjeneste + data.medarbejderOmkostninger?:0
        data.bruttofortjeneste
      }
    }
  }

  void forsoegBruttofortjeneste(RegnskabData data) {
    if (!data.omsaetning || !data.bruttofortjeneste) {
      return
    }
    // hvis bruttoresultatet ikke stemmer, så kan der mangle variable omkostninger. dette kan regnes ud hvis man også har omsætnignen
    long kalkuleretOmsaetning = (data.andreEksterneOmkostninger?:0) + (data.variableOmkostninger?:0) + (data.eksterneomkostninger?:0) +
      (data.vareforbrug?:0) + data.bruttofortjeneste

    if (kalkuleretOmsaetning != data.omsaetning ) {
      if (!data.variableOmkostninger && data.omsaetning) {
        data.variableOmkostninger = data.omsaetning-
          (data.andreEksterneOmkostninger?data.andreEksterneOmkostninger:0)-
          (data.eksterneomkostninger?data.eksterneomkostninger:0);
      }
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
    data.bruttofortjeneste = getLongValue(nl, ns, "ProfitLossFromOperatingActivities")
    data.resultatfoerskat = getLongValue(nl, ns, "ProfitLossBeforeTax")
    data.aaretsresultat = getLongValue(nl, ns, "ProfitLoss")
    data.finansielleIndtaegter = getLongValue(nl, ns, "FinanceIncome")
    data.finansielleOmkostninger = getLongValue(nl, ns, "FinanceCosts")

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

  String hentContextRef(Node xmlDokument, Namespace ns) {
    if (ns.prefix=='fsa') {
      // så er det noget tricky at finde contexten forsøger med revenue
      NodeList n = xmlDokument[ns.Revenue]

      if (n!=null && n.size()>0) {
        Node node = n.get(0)
        return node.attribute("contextRef")
      }
    }

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
