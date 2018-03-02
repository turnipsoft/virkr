package dk.ts.virkr.aarsrapporter.taxonomy

import dk.ts.virkr.aarsrapporter.model.Regnskabstal
import dk.ts.virkr.aarsrapporter.parser.RegnskabNodes

/**
 * Created by sorenhartvig on 16/02/2018.
 */
class TaxonomyRegnskabBuilder {

  Map<String, String> regnskabsomraader = [
    'ResultatOpgørelse' : 'fsa/501isn_def.xml',
    'Balance' : 'fsa/400bsa_def.xml',
    'Revision' : 'arr/1NNarr_def.xml',
    'General'  : 'gsd/000gsd_def.xml'
  ]

  Map<String, Object> bygRegnskab(RegnskabNodes regnskabNodes) {
    TaxonomyParser taxonomyParser = new TaxonomyParser()
    Map<String, Object> struktur = taxonomyParser.parse()

    // noegletal
    Map<String, TaxonomyEntity> noegletal = struktur['fsa/501isn_def.xml']
    Map<String, TaxonomyEntity> balance = struktur['fsa/400bsa_def.xml']
    Map<String, TaxonomyEntity> revision = struktur['arr/1NNarr_def.xml']

    regnskabNodes.aktuelleNoegletalNodes.each { node ->
      berigTaxonomy(noegletal, node)
    }

    regnskabNodes.aktuelleBalanceNodes.each { node ->
      berigTaxonomy(balance, node)
    }

    regnskabNodes.arrNodes.each { node ->
      berigTaxonomy(revision, node)
    }

    return struktur
  }

  Map<String, TaxonomyEntity> quickRefMap = [:]

  boolean berigTaxonomy(Map<String, TaxonomyEntity> struktur, Node n) {
    boolean found = false
    String name = n.name()
    if (name.contains(':')) {
      name = name.substring(name.indexOf(':')+1)
    }

    if (quickRefMap.containsKey(name)) {
      berigEntity(quickRefMap.get(name), n)
      return true
    }

    struktur.entrySet().each { entrySet->
      if (found) return
      TaxonomyEntity entity = entrySet.value

      if (!quickRefMap.containsKey(entity.label)) {
        quickRefMap.put(entity.label, entity)
      }

      if (entity.label == name) {
        berigEntity(entity, n)
        found = true
      }

      if (!found && entity.children) {
        found = berigTaxonomy(entity.children, n)
      }
    }

    return found
  }

  void berigEntity(TaxonomyEntity entity, Node n) {
    if (n.text().isNumber()) {
      Long l = Long.valueOf(n.text())
      String decimaler = n.attribute('decimals')

      Long decimal = 0;

      if (decimaler) {
        try {
          decimal = decimaler ? Long.valueOf(decimaler) : 0
        } catch (Exception e) {
          decimal = 0;
        }
      }

      entity.value = new Regnskabstal(l, decimal)
      println(entity)
    } else {
      entity.stringValue = n.text()
    }
  }

}
