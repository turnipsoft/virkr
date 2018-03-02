package dk.ts.virkr.integration

import dk.ts.virkr.aarsrapporter.parser.RegnskabNodes
import dk.ts.virkr.aarsrapporter.taxonomy.TaxonomyEntity
import dk.ts.virkr.aarsrapporter.taxonomy.TaxonomyRegnskabBuilder
import spock.lang.Specification

/**
 * Created by sorenhartvig on 16/02/2018.
 */
class TaxonomyRegnskabBuilderSpec extends Specification {
  void "test parse greener pastures"() {
    given:
    String xml = TestUtil.load('/greener_pastures.xml')
    TaxonomyRegnskabBuilder rb = new TaxonomyRegnskabBuilder()
    RegnskabNodes regnskabNodes = new RegnskabNodes(xml)

    when:
    Map<String, Object> map = rb.bygRegnskab(regnskabNodes)
    Map<String, TaxonomyEntity> isf = map['fsa/501isn_def.xml']
    isf.values().each {v->
      println(v.toString())
    }

    isf = map['fsa/400bsa_def.xml']
    isf.values().each {v->
      println(v.toString())
    }

    isf = map['arr/1NNarr_def.xml']
    isf.values().each {v->
      println(v.toString())
    }

    then:
    map
  }
}
