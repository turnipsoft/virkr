package dk.ts.virkr.aarsrapporter.parser

import groovy.xml.Namespace

/**
 * Denne klasse har til ansvar at dele et XML rengskab op i en række nodelists svarende til de forskellige namespaces
 * der er tilstede i regnskabet, tilsvarende holder klassen styr på namespaces navne
 */
class RegnskabNodes {

  public static final String CMN_NAMESPACE = 'http://xbrl.dcca.dk/cmn'
  public static final String ARR_NAMESPACE = 'http://xbrl.dcca.dk/arr'
  public static final String GSD_NAMESPACE = 'http://xbrl.dcca.dk/gsd'
  public static final String SOB_NAMESPACE = 'http://xbrl.dcca.dk/sob'
  public static final String FSA_NAMESPACE = 'http://xbrl.dcca.dk/fsa'

  public Node xmlDokument

  public List<Node> fsaNodes
  public List<Node> arrNodes
  public List<Node> cmnNodes
  public List<Node> sobNodes
  public List<Node> gsdNodes
  public List<Node> ifrsNodes
  public List<Node> ifrsDkNodes
  public List<Node> aktuelleNoegletalNodes
  public List<Node> sidsteAarsNoegletalNodes
  public List<Node> aktuelleBalanceNodes
  public List<Node> sidsteAarsBalanceNodes
  public NodeList contextNodes

  public Namespace fsaNamespace
  public Namespace arrNamespace
  public Namespace cmnNamespace
  public Namespace gsdNamespace
  public Namespace sobNamespace
  public Namespace ifrsNamespace
  public Namespace ifrsDkNamespace

  public String aktuelContext
  public String sidsteAarsContext

  public String aktuelBalanceContext
  public String sidsteAarsBalanceContext

  List<Namespace> noegletalNamespace() {
    List<Namespace> result = []
    if (this.ifrsNamespace) {
      result << this.ifrsNamespace
      if (this.ifrsDkNamespace) {
        result << this.ifrsDkNamespace
      }
    } else {
      result << fsaNamespace
    }

    return result
  }

  List<Node> noegletalNodes() {
    return this.ifrsNodes ? this.ifrsNodes : this.ifrsNamespace
  }

  RegnskabNodes(String xml) {
    XmlParser parser = new XmlParser(false, false)
    Node xmlDokument = parser.parseText(xml)

    this.xmlDokument = xmlDokument

    this.fsaNamespace = hentNamespace(xml, FSA_NAMESPACE)
    this.arrNamespace = hentNamespace(xml, ARR_NAMESPACE)
    this.sobNamespace = hentNamespace(xml, SOB_NAMESPACE)
    this.gsdNamespace = hentNamespace(xml, GSD_NAMESPACE)
    this.cmnNamespace = hentNamespace(xml, CMN_NAMESPACE)
    this.ifrsNamespace = hentIFRSNamespace(xml)
    this.ifrsDkNamespace = hentIFRDKSNamespace(xml)

    if (this.fsaNamespace) {
      this.fsaNodes = hentNodes(xmlDokument, this.fsaNamespace)
    }

    if (this.sobNamespace) {
      this.sobNodes = hentNodes(xmlDokument, this.sobNamespace)
    }

    if (this.arrNamespace) {
      this.arrNodes = hentNodes(xmlDokument, this.arrNamespace)
    }

    if (this.cmnNamespace) {
      this.cmnNodes = hentNodes(xmlDokument, this.cmnNamespace)
    }

    if (this.gsdNamespace) {
      this.gsdNodes = hentNodes(xmlDokument, this.gsdNamespace)
    }

    if (this.ifrsNamespace) {
      this.ifrsNodes = hentNodes(xmlDokument, this.ifrsNamespace)
    }

    if (this.ifrsDkNamespace) {
      this.ifrsDkNodes = hentNodes(xmlDokument, this.ifrsDkNamespace)
    }

    this.contextNodes = hentContextNodes(xmlDokument)
    this.aktuelContext = hentContextRef(xmlDokument, noegletalNamespace(), this.contextNodes, NOEGLETAL_IDENTER, true)
    this.sidsteAarsContext = hentContextRef(xmlDokument, noegletalNamespace(), this.contextNodes, NOEGLETAL_IDENTER, false)

    this.aktuelBalanceContext = hentContextRef(xmlDokument, noegletalNamespace(), this.contextNodes, BALANCE_IDENTER, true)
    this.sidsteAarsBalanceContext = hentContextRef(xmlDokument, noegletalNamespace(), this.contextNodes, BALANCE_IDENTER, false)


    this.aktuelleNoegletalNodes = xmlDokument.findAll {
      it.attribute('contextRef') == this.aktuelContext
    }

    if (this.sidsteAarsContext) {
      this.sidsteAarsNoegletalNodes = xmlDokument.findAll {
        it.attribute('contextRef') == this.sidsteAarsContext
      }
    }

    this.aktuelleBalanceNodes = xmlDokument.findAll {
      it.attribute('contextRef') == this.aktuelBalanceContext
    }

    if (this.sidsteAarsBalanceContext) {
      this.sidsteAarsBalanceNodes = xmlDokument.findAll {
        it.attribute('contextRef') == this.sidsteAarsBalanceContext
      }
    }
  }

  public static List<String> NOEGLETAL_IDENTER = ['GrossResult','TaxExpenseOnOrdinaryActivities','ProfitLoss','GrossProfitLoss']
  public static List<String> BALANCE_IDENTER = ['Equity','Assets','Provisions']

  public static final List<String> IFRS_NAMESPACE_LIST = ["http://xbrl.ifrs.org/taxonomy/2014-03-05/ifrs-full",
                                                          "http://xbrl.ifrs.org/taxonomy/2011-03-25/ifrs",
                                                          "http://xbrl.dcca.dk/ifrs-dk-cor_2013-12-20" ]

  public static final List<String> IFRS_DK_NAMESPACE_LIST = ["http://xbrl.dcca.dk/ifrs-dk-cor_2014-12-20",
                                                             "http://xbrl.dcca.dk/ifrs-dk-cor_2013-12-20" ]
  Namespace hentIFRSNamespace(String xml)  {

    for (String namespaceUrl: IFRS_NAMESPACE_LIST) {
      String namespace = getNamespace(xml, namespaceUrl)
      if (namespace) {
        return new Namespace(namespaceUrl, namespace)
      }
    }

    return null
  }

  Namespace hentIFRDKSNamespace(String xml) {
    for (String namespaceUrl: IFRS_DK_NAMESPACE_LIST) {
      String namespace = getNamespace(xml, namespaceUrl)
      if (namespace) {
        return new Namespace(namespaceUrl, namespace)
      }
    }

    return null
  }

  Namespace hentNamespace(String xml, String namespaceUrl) {
    String namespace = getNamespace(xml, namespaceUrl)
    if (namespace) {
      Namespace ns = new Namespace(namespaceUrl, namespace)
      return  ns
    }

    return null
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

  List<Node> hentNodes(Node xmlDokument, Namespace namespace) {
    return xmlDokument.findAll {
      it.name().toString().startsWith(namespace.prefix)
    }
  }

  static XBRL_NS = 'http://www.xbrl.org/2003/instance';

  String hentNamespaceNavn(Node xmlDokument, String namespace) {
    String ns = xmlDokument.attributes().find { attribute->
      attribute.value == namespace && attribute.key!='xmlns'
    }.key

    return ns.substring(6)
  }

  NodeList hentContextNodes(Node xmlDokument) {
    NodeList contextNodes = xmlDokument['context'];
    if (!contextNodes) {
      String namespacenavn = hentNamespaceNavn(xmlDokument,XBRL_NS)
      Namespace nsc = new Namespace(XBRL_NS, namespacenavn)
      contextNodes = xmlDokument[nsc.context];
    }

    return contextNodes
  }

  String getNameWithNamespaceOf(Node n, String name) {
    String nodeName = n.name()
    String ens = nodeName.contains(':')? nodeName.substring(0, nodeName.indexOf(':')+1) : ''
    String result = ens+'scenario'

    return result;
  }

  /**
   * Anvendes til at finde ud af om der er tale om et FSA regnskab for et solo consolidated virksomhed
   * @param n
   * @return
   */
  boolean erContextRefFsaOgKonsolideret(Node n) {
    String scenario = getNameWithNamespaceOf(n, 'scenario');

    boolean soloDimensionFound = false;
    if (n[scenario]) {
      NodeList scenarios = n[scenario];
      scenarios.each { Node scenarioNode->
        scenarioNode.children().each { Node childNode->
          if (childNode.name().contains('explicitMember')) {
            if (childNode.attribute('dimension')!=null &&
              childNode.attribute('dimension').contains('ConsolidatedSoloDimension')) {
              soloDimensionFound = true;
            } else {
              soloDimensionFound = false;
            }
          }
        }
      }
    }

    return soloDimensionFound;
  }

  /**
   * Anvendes til at finde slutdato på en contextnode. denne kan både ligge som endDate og som instant
   * @param node
   * @param nsPrefix
   * @return
   */
  String getEndDate(Node node, String nsPrefix) {
    NodeList endDate = node[nsPrefix+'period'][nsPrefix+'endDate'];
    if (endDate) {
      return endDate.text();
    }
    NodeList instant = node[nsPrefix+'period'][nsPrefix+'instant'];
    return instant.text();
  }

  String hentContextRef(Node xmlDokument,
                        List<Namespace> ns,
                        NodeList contextNodes,
                        List<String> identificerendeElementer,
                        boolean nyeste = true) {
    return hentContextRef(xmlDokument, ns.get(0), contextNodes, identificerendeElementer, nyeste)
  }

  String hentContextRef(Node xmlDokument,
                        Namespace ns,
                        NodeList contextNodes,
                        List<String> identificerendeElementer,
                        boolean nyeste = true) {


    this.contextNodes = contextNodes

    // find det element hvis contextRef ikke er konsolideret og som har nyeste periode.
    NodeList n
    identificerendeElementer.each {
      if (n) return;
      n = xmlDokument[ns.prefix+':'+it]
    }

    if (n != null && n.size() > 0) {
      List<Node> contextRefNodeCandidates = []
      n.each {
        String contextRefCandidate = it.attribute("contextRef")
        Node contextRefNodeCandidate = contextNodes.find { it.attribute('id') == contextRefCandidate }
        // skal ikke have dem der har scenario på i FSA og ikke have de konsoliderede i IFRS'erne

        // men man skal have de konsoliderede i FSA hvis det er.
        String contextRefNodeCandidateName = contextRefNodeCandidate.attribute('id')

        boolean erFsaOgKonsolideret = erContextRefFsaOgKonsolideret(contextRefNodeCandidate);

        String scenario = getNameWithNamespaceOf(contextRefNodeCandidate, 'scenario');

        if (erFsaOgKonsolideret || (contextRefNodeCandidate && !contextRefNodeCandidate[scenario] && !contextRefNodeCandidateName.contains('_C_'))) {
          Node existing = contextRefNodeCandidates.find {
            String name = it.name()
            String ens = name.contains(':')? name.substring(0, name.indexOf(':')+1) : ''
            String e1 = getEndDate(it, ens)
            String e2 = getEndDate(contextRefNodeCandidate, ens)
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

      contextRefNodeCandidates = contextRefNodeCandidates.sort { getEndDate(it, ens) }
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


    return null
  }

}
