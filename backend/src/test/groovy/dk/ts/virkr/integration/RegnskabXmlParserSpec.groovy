package dk.ts.virkr.integration

import dk.ts.virkr.aarsrapporter.parser.RegnskabXmlParser
import dk.ts.virkr.aarsrapporter.model.RegnskabData
import dk.ts.virkr.aarsrapporter.model.Resultatopgoerelse
import dk.ts.virkr.aarsrapporter.model.virksomhedsdata.Virksomhedsdata
import spock.lang.Ignore
import spock.lang.Specification

/**
 * Created by sorenhartvig on 25/05/2017.
 */
class RegnskabXmlParserSpec extends Specification {

  void "test parse greener pastures"() {
    given:
    String xml = TestUtil.load('/greener_pastures.xml')
    RegnskabXmlParser regnskabXmlParser = new RegnskabXmlParser()
    RegnskabData regnskabData = new RegnskabData()

    when:
    regnskabData = regnskabXmlParser.parseOgBerig(regnskabData, xml)

    Resultatopgoerelse ro = regnskabData.resultatopgoerelse

    then:
    ro.bruttoresultatTal.bruttofortjeneste == 4100142l
    ro.aaretsresultatTal.aaretsresultat == 541316l
    ro.nettoresultatTal.driftsresultat == 727959l
    ro.aaretsresultatTal.resultatfoerskat == 722363l
    ro.aaretsresultatTal.skatafaaretsresultat == 181047l

    regnskabData.balance.passiver.gaeldsforpligtelser == 1258900l
    regnskabData.balance.passiver.egenkapital == 1414171l

  }

  void "test parse capgemini"() {
    given:
    String xml = TestUtil.load('/capgemini.xml')
    RegnskabXmlParser regnskabXmlParser = new RegnskabXmlParser()
    RegnskabData regnskabData = new RegnskabData()

    when:
    regnskabData = regnskabXmlParser.parseOgBerig(regnskabData, xml)

    Resultatopgoerelse ro = regnskabData.resultatopgoerelse

    then:
    ro.bruttoresultatTal.bruttofortjeneste == 68539000l
    ro.nettoresultatTal.driftsresultat == 18567000l
    ro.aaretsresultatTal.resultatfoerskat == 18762000l
    ro.aaretsresultatTal.skatafaaretsresultat == 4240000l
    ro.aaretsresultatTal.aaretsresultat == 14522000l
    regnskabData.balance.passiver.gaeldsforpligtelser == 80864000l
    regnskabData.balance.passiver.egenkapital == 61554000l

  }

  void "test parse nine"() {
    given:
    String xml = TestUtil.load('/nine.xml')
    RegnskabXmlParser regnskabXmlParser = new RegnskabXmlParser()
    RegnskabData regnskabData = new RegnskabData()

    when:
    regnskabData = regnskabXmlParser.parseOgBerig(regnskabData, xml)

    Resultatopgoerelse ro = regnskabData.resultatopgoerelse

    then:
    ro.bruttoresultatTal.bruttofortjeneste == 87883765l
    ro.nettoresultatTal.driftsresultat == 19658442l
    ro.aaretsresultatTal.resultatfoerskat == 19704409l
    ro.nettoresultatTal.finansielleindtaegter == 63203l
    ro.nettoresultatTal.finansielleomkostninger == 17236l
    ro.aaretsresultatTal.skatafaaretsresultat == 4189184l
    ro.aaretsresultatTal.aaretsresultat == 15515225l
    regnskabData.balance.passiver.gaeldsforpligtelser == 19030055l
    regnskabData.balance.passiver.egenkapital == 31210704l

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

  // FIXME: FSA regnskab... hmmmm how to parse
  @Ignore
  void "test parse dagrofa"() {
    given:
    String xml = TestUtil.load('/dagrofa.xml')
    RegnskabXmlParser regnskabXmlParser = new RegnskabXmlParser()
    RegnskabData regnskabData = new RegnskabData()

    when:
    regnskabData = regnskabXmlParser.parseOgBerig(regnskabData, xml)

    then:
    regnskabData.bruttofortjeneste == 87883765l

    regnskabData.driftsresultat == 50897000l
    regnskabData.resultatfoerskat == 19704409l
    regnskabData.finansielleIndtaegter == 63203l
    regnskabData.finansielleOmkostninger == 17236l
    regnskabData.skatafaaretsresultat == 4189184l
    regnskabData.aaretsresultat == 15515225l
    regnskabData.gaeldsforpligtelser == 19030055l
    regnskabData.egenkapital == 31210704l
  }

  void "test parse lagkagehuset"() {
    given:
    String xml = TestUtil.load('/lagkagehuset.xml')
    RegnskabXmlParser regnskabXmlParser = new RegnskabXmlParser()
    RegnskabData regnskabData = new RegnskabData()

    when:
    regnskabData = regnskabXmlParser.parseOgBerig(regnskabData, xml)
    Resultatopgoerelse ro = regnskabData.resultatopgoerelse

    then:
    ro.omsaetningTal.omsaetning == 552449000l
    ro.bruttoresultatTal.bruttofortjeneste == 292764000l
    ro.bruttoresultatTal.regnskabsmaessigeafskrivninger == 28137000l
    ro.bruttoresultatTal.medarbejderomkostninger == 213730000l

    ro.nettoresultatTal.driftsresultat == 50897000l
    ro.nettoresultatTal.finansielleindtaegter == 795000

    ro.aaretsresultatTal.resultatfoerskat == 47316000l
    ro.nettoresultatTal.finansielleomkostninger == 4376000l
    ro.aaretsresultatTal.skatafaaretsresultat == 9175000l
    ro.aaretsresultatTal.aaretsresultat == 38141000l
    regnskabData.balance.passiver.gaeldsforpligtelser == 97955000l
    regnskabData.balance.passiver.egenkapital == 75968l
  }



}
