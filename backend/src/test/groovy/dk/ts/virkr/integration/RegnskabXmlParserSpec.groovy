package dk.ts.virkr.integration

import dk.ts.virkr.aarsrapporter.integration.RegnskabXmlParser
import dk.ts.virkr.aarsrapporter.integration.model.regnskabdata.RegnskabData
import spock.lang.Specification

/**
 * Created by sorenhartvig on 25/05/2017.
 */
class RegnskabXmlParserSpec extends Specification {

  void "test parse ifrs"() {

  }

  void "test parse greener pastures"() {
    given:
    String xml = TestUtil.load('/greener_pastures.xml')
    RegnskabXmlParser regnskabXmlParser = new RegnskabXmlParser()
    RegnskabData regnskabData = new RegnskabData()

    when:
    regnskabData = regnskabXmlParser.parseOgBerig(regnskabData, xml)

    then:
    regnskabData.bruttofortjeneste == 4100142l
    regnskabData.aaretsresultat == 541316l
    regnskabData.gaeldsforpligtelser == 1258900l
    regnskabData.driftsresultat == 727959l
    regnskabData.egenkapital == 1414171l
    regnskabData.resultatfoerskat == 722363l
    regnskabData.skatafaaretsresultat == 181047l

  }

  void "test parse systematic"() {


  }
}
