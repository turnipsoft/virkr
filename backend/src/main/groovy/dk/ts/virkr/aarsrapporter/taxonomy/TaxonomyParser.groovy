package dk.ts.virkr.aarsrapporter.taxonomy

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by shartvig2 on 2/15/18.
 */
class TaxonomyParser {

  static final Logger logger = LoggerFactory.getLogger(TaxonomyParser.class)

  static void main(String [] args) {
    TaxonomyParser tp = new TaxonomyParser()
    tp.parse()
  }

  static String load(String filename) {
    logger.info("Trying to load "+filename)
    return TaxonomyParser.class.getResource(filename).text
  }

  static String mainXsd = '/taxonomy/XBRL20171001/20171001/entryDanishGAAPBalanceSheetAccountFormIncomeStatementByNatureIncludingManagementsReviewStatisticsAndTax20171001.xsd'

  static String path = '/taxonomy/XBRL20171001/20171001/'

  Map<String, Object> parse() {
    String mainXsd = TaxonomyParser.load(mainXsd)
    XmlParser parser = new XmlParser(false, false)
    Node xmlDokument = parser.parseText(mainXsd)
    Node appinfo = xmlDokument['xsd:annotation']['xsd:appinfo'].first()
    Map<String, Object> struktur = [:]
    appinfo.children().each { Node node->
      String href = node.attribute('xlink:href')
      berigStruktur(href, struktur)
    }

    berigStrukturMedText(struktur)
    return struktur
  }

  void berigStrukturMedText(Map<String, Object> map) {
    map.entrySet().each { entrySet->
      Map<String, TaxonomyEntity> m = entrySet.value
      berigRekursivt(m)
    }
  }

  static Map<String, String> refMap = [:]



  void berigTaxonomyEntity(TaxonomyEntity taxonomyEnitity) {
    String path = getPath(taxonomyEnitity.ref)
    if (!refMap.containsKey(path)) {
      refMap.put(path, loadTextMap(taxonomyEnitity))
    }

    taxonomyEnitity.text = refMap.get(path).get(taxonomyEnitity.label)
  }

  String getPath(String ref) {
    if (ref.startsWith('../')) {
      return ref.substring(3, ref.indexOf('.xsd'))
    } else {
      String res = ref.substring(0,3) + File.separator+ ref.substring(0, ref.indexOf('.xsd'))
      return res
    }
  }
  Map<String, String> loadTextMap(TaxonomyEntity taxonomyEnitity) {
    Map<String, String> texts = [:]
    String r = taxonomyEnitity.ref
    String shortRef = getPath(r)
    String langRef = shortRef+"-lab-da.xml"
    String fullpath = TaxonomyParser.path+langRef
    String xml = TaxonomyParser.load(fullpath)
    xml = xml.replace("\uFEFF", "")
    XmlParser parser = new XmlParser(false, false)
    Node xmlDokument = parser.parseText(xml)
    NodeList nodes = xmlDokument['link:labelLink']
    Node first = nodes.first()

    String currentLabel
    String currentText
    boolean foundText=false
    first.children().each { Node n->
      if (n.name()=='link:loc') {
        if (currentLabel && currentText) {
          texts.put(currentLabel, currentText)
        }
        currentLabel = n.attribute('xlink:label')
        foundText = false
      }
      if (n.name()=='link:label') {
        if (!foundText) {
          currentText = n.text()
          foundText = true
        }
      }
    }

    texts.put(currentLabel, currentText)

    return texts
  }

  void berigRekursivt(Map<String, TaxonomyEntity> map) {
    map.entrySet().each { es->
      TaxonomyEntity taxonomyEnitity = es.value
      berigTaxonomyEntity(taxonomyEnitity)
      if (taxonomyEnitity.children) {
        berigRekursivt(taxonomyEnitity.children)
      }
    }
  }

  void berigStruktur(String href, Map<String, Object> struktur) {
    if (!href.contains('def')) {
      return
    }

    Map<String, TaxonomyEntity> map = [:]
    struktur.put(href,map)


    String filnavn = TaxonomyParser.path + href
    String xsd = TaxonomyParser.load(filnavn)
    XmlParser parser = new XmlParser(false, false)
    Node xmlDokument = parser.parseText(xsd)
    NodeList definitionLinkList = xmlDokument['link:definitionLink']
    TaxonomyEntity currentTaxonomyEntity = null
    TaxonomyEntity ownerTaxonomyEntity = null

    logger.info("Beriger struktur ${href} ${filnavn}")
    if (filnavn.contains('isf')) {
      logger.debug("DEBUG ME")
    }

    definitionLinkList.each { Node definitionLink ->
      if (!definitionLink.children()) {
        return
      }

      definitionLink.children().each { Node node->
        if (node.name()=='link:loc') {
          if (currentTaxonomyEntity) {
            if (ownerTaxonomyEntity) {
              ownerTaxonomyEntity.children.put(currentTaxonomyEntity.label, currentTaxonomyEntity)
            } else {
              TaxonomyEntity tmpOwnerTaxonomyEntity = findTaxonomyEntity(map, currentTaxonomyEntity.label)
              if (tmpOwnerTaxonomyEntity) {
                tmpOwnerTaxonomyEntity.children.put(currentTaxonomyEntity.label, currentTaxonomyEntity)
              } else {
                map.put(currentTaxonomyEntity.label, currentTaxonomyEntity)
              }
            }
          }
          String ref = node.attribute('xlink:href')
          String label = node.attribute('xlink:label')
          if (label.contains('EmployeeBenefits')) {
            logger.info('Found Benefits')

          }
          currentTaxonomyEntity = new TaxonomyEntity(ref, label)
        }
        if (node.name()=='link:definitionArc') {
          String from = node.attribute('xlink:from')
          String order = node.attribute('order')
          currentTaxonomyEntity.order = order
          ownerTaxonomyEntity = findTaxonomyEntity(map, from)
        }
      }
    }

    if (currentTaxonomyEntity) {
      if (ownerTaxonomyEntity) {
        ownerTaxonomyEntity.children.put(currentTaxonomyEntity.label, currentTaxonomyEntity)
      } else {
        TaxonomyEntity tmpOwnerTaxonomyEntity = findTaxonomyEntity(map, currentTaxonomyEntity.label)
        if (tmpOwnerTaxonomyEntity) {
          tmpOwnerTaxonomyEntity.children.put(currentTaxonomyEntity.label, currentTaxonomyEntity)
        } else {
          map.put(currentTaxonomyEntity.label, currentTaxonomyEntity)
        }
      }
    }
  }

  TaxonomyEntity findTaxonomyEntity(Map<String, TaxonomyEntity> struktur, String label) {
    if (struktur.containsKey(label)) {
      return struktur[label]
    }

    TaxonomyEntity fundet = null
    struktur.entrySet().each { entry->
      if (fundet) {
        return
      }
      fundet = findTaxonomyEntity(entry.value.children, label)
    }

    return fundet
  }

}
