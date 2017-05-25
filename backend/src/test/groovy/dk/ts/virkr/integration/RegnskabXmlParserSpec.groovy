package dk.ts.virkr.integration

import dk.ts.virkr.aarsrapporter.integration.RegnskabXmlParser
import dk.ts.virkr.aarsrapporter.integration.model.regnskabdata.RegnskabData
import spock.lang.Specification

/**
 * Created by sorenhartvig on 25/05/2017.
 */
class RegnskabXmlParserSpec extends Specification {

  void "test parse netcompany ifrs"() {
    given:
    String xml = TestUtil.load('/netcompany.xml')
    RegnskabXmlParser regnskabXmlParser = new RegnskabXmlParser()
    RegnskabData regnskabData = new RegnskabData()

    when:
    regnskabData = regnskabXmlParser.parseOgBerig(regnskabData, xml)

    then:
    regnskabData.omsaetning == null
    regnskabData.medarbejderOmkostninger == null

    regnskabData.bruttofortjeneste == -5782000l
    regnskabData.driftsresultat == null
    regnskabData.resultatfoerskat == 103893000l
    regnskabData.finansielleIndtaegter == 31000l
    regnskabData.finansielleOmkostninger == 3856000l
    regnskabData.skatafaaretsresultat == null
    regnskabData.aaretsresultat == 108187000l
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

  void "test parse capgemini"() {
    given:
    String xml = TestUtil.load('/capgemini.xml')
    RegnskabXmlParser regnskabXmlParser = new RegnskabXmlParser()
    RegnskabData regnskabData = new RegnskabData()

    when:
    regnskabData = regnskabXmlParser.parseOgBerig(regnskabData, xml)

    then:
    regnskabData.bruttofortjeneste == 68539000l
    regnskabData.driftsresultat == 18567000l
    regnskabData.resultatfoerskat == 18762000l
    regnskabData.skatafaaretsresultat == 4240000l
    regnskabData.aaretsresultat == 14522000l
    regnskabData.gaeldsforpligtelser == 80864000l
    regnskabData.egenkapital == 61554000l

  }
  void "test parse systematic"() {
    given:
    String xml = TestUtil.load('/capgemini.xml')
    RegnskabXmlParser regnskabXmlParser = new RegnskabXmlParser()
    RegnskabData regnskabData = new RegnskabData()

    when:
    regnskabData = regnskabXmlParser.parseOgBerig(regnskabData, xml)

    then:
    regnskabData.omsaetning == 91909395l
    regnskabData.bruttofortjeneste == 53929716l
    regnskabData.driftsresultat == 9033698l
    regnskabData.resultatfoerskat == 9776870l
    regnskabData.finansielleOmkostninger == 37380l
    regnskabData.skatafaaretsresultat == 2074356l
    regnskabData.aaretsresultat == 7762514l
    regnskabData.gaeldsforpligtelser == 80864000l
    regnskabData.egenkapital == 33723425l

  }

  void "test parse kmd"() {
    given:
    String xml = TestUtil.load('/kmd.xml')
    RegnskabXmlParser regnskabXmlParser = new RegnskabXmlParser()
    RegnskabData regnskabData = new RegnskabData()

    when:
    regnskabData = regnskabXmlParser.parseOgBerig(regnskabData, xml)

    then:
    regnskabData.omsaetning == 5073800000l
    // FIXME: hmmm ingen bruttofortjeneste og f.eks. proff.dk siger at det er samme brutto fortjeneste som omsætning
    regnskabData.bruttofortjeneste == null
    regnskabData.driftsresultat == 340800000l
    regnskabData.resultatfoerskat == 325800000l
    regnskabData.finansielleIndtaegter == 11900000l
    regnskabData.finansielleOmkostninger == 26900000l
    regnskabData.skatafaaretsresultat == 81400000l
    regnskabData.aaretsresultat == 244400000l
    regnskabData.gaeldsforpligtelser == 1858700000l
    regnskabData.medarbejderOmkostninger == 2049900000l
    regnskabData.egenkapital == 1164600000l
  }

  void "test parse NC it"() {
    given:
    String xml = TestUtil.load('/nc-itbusiness.xml')
    RegnskabXmlParser regnskabXmlParser = new RegnskabXmlParser()
    RegnskabData regnskabData = new RegnskabData()

    when:
    regnskabData = regnskabXmlParser.parseOgBerig(regnskabData, xml)

    then:
    regnskabData.omsaetning == 887878000l
    regnskabData.bruttofortjeneste == 666101000l
    regnskabData.medarbejderOmkostninger == 475902000l
    regnskabData.driftsresultat == 184923000l
    regnskabData.finansielleIndtaegter == 616000l
    regnskabData.finansielleOmkostninger == 3936000l
    regnskabData.resultatfoerskat == 200894000l
    regnskabData.skatafaaretsresultat == 31008000l
    regnskabData.aaretsresultat == 169886000l
    regnskabData.gaeldsforpligtelser == 212880000l
    regnskabData.egenkapital == 224488000l
  }


}
