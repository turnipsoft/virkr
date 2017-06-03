package dk.ts.virkr.integration

import dk.ts.virkr.aarsrapporter.integration.RegnskabXmlParser
import dk.ts.virkr.aarsrapporter.integration.model.regnskabdata.RegnskabData
import dk.ts.virkr.aarsrapporter.integration.model.virksomhedsdata.Virksomhedsdata
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
    String xml = TestUtil.load('/systematic.xml')
    RegnskabXmlParser regnskabXmlParser = new RegnskabXmlParser()
    RegnskabData regnskabData = new RegnskabData()

    when:
    regnskabData = regnskabXmlParser.parseOgBerig(regnskabData, xml)

    then:
    regnskabData.omsaetning == 91909396l
    regnskabData.bruttofortjeneste == 53929716l
    regnskabData.driftsresultat == 9033698l
    regnskabData.resultatfoerskat == 9776870l
    regnskabData.finansielleOmkostninger == null
    regnskabData.skatafaaretsresultat == 2014356l
    regnskabData.aaretsresultat == 7762514l
    regnskabData.gaeldsforpligtelser == 29839752l
    regnskabData.egenkapital == 25742073l

    when:
    Virksomhedsdata virksomhedsdata = regnskabXmlParser.hentVirksomhedsdataFraRegnskab(xml)

    then:
    virksomhedsdata
    virksomhedsdata.cvrnummer == '78834412'
    virksomhedsdata.navn == 'Systematic A/S'
    virksomhedsdata.vejnavn == 'Søren Frichs Vej'
    virksomhedsdata.husnr == '39'
    virksomhedsdata.postnr == '8000'
    virksomhedsdata.bynavn == 'Aarhus C'

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

  void "test parse nine"() {
    given:
    String xml = TestUtil.load('/nine.xml')
    RegnskabXmlParser regnskabXmlParser = new RegnskabXmlParser()
    RegnskabData regnskabData = new RegnskabData()

    when:
    regnskabData = regnskabXmlParser.parseOgBerig(regnskabData, xml)

    then:
    regnskabData.bruttofortjeneste == 87883765l
    regnskabData.driftsresultat == 19658442l
    regnskabData.resultatfoerskat == 19704409l
    regnskabData.finansielleIndtaegter == 63203l
    regnskabData.finansielleOmkostninger == 17236l
    regnskabData.skatafaaretsresultat == 4189184l
    regnskabData.aaretsresultat == 15515225l
    regnskabData.gaeldsforpligtelser == 19030055l
    regnskabData.egenkapital == 31210704l

    when:
    Virksomhedsdata virksomhedsdata = regnskabXmlParser.hentVirksomhedsdataFraRegnskab(xml)

    then:
    virksomhedsdata
    virksomhedsdata.cvrnummer == '30714024'
    virksomhedsdata.navn == 'NineConsult A/S'
    virksomhedsdata.vejnavn == 'Kongens Nytorv'
    virksomhedsdata.husnr == '3-5'
    virksomhedsdata.postnr == '1050'
    virksomhedsdata.bynavn == 'København K'

  }

  void "test parse dagrofa"() {
    given:
    String xml = TestUtil.load('/dagrofa.xml')
    RegnskabXmlParser regnskabXmlParser = new RegnskabXmlParser()
    RegnskabData regnskabData = new RegnskabData()

    when:
    regnskabData = regnskabXmlParser.parseOgBerig(regnskabData, xml)

    then:
    regnskabData.bruttofortjeneste == 87883765l
    regnskabData.driftsresultat == 19658442l
    regnskabData.resultatfoerskat == 19704409l
    regnskabData.finansielleIndtaegter == 63203l
    regnskabData.finansielleOmkostninger == 17236l
    regnskabData.skatafaaretsresultat == 4189184l
    regnskabData.aaretsresultat == 15515225l
    regnskabData.gaeldsforpligtelser == 19030055l
    regnskabData.egenkapital == 31210704l

    when:
    Virksomhedsdata virksomhedsdata = regnskabXmlParser.hentVirksomhedsdataFraRegnskab(xml)

    then:
    virksomhedsdata
    virksomhedsdata.cvrnummer == '30714024'
    virksomhedsdata.navn == 'NineConsult A/S'
    virksomhedsdata.vejnavn == 'Kongens Nytorv'
    virksomhedsdata.husnr == '3-5'
    virksomhedsdata.postnr == '1050'
    virksomhedsdata.bynavn == 'København K'

  }



}
