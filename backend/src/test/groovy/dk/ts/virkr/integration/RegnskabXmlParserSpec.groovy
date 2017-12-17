package dk.ts.virkr.integration

import dk.ts.virkr.aarsrapporter.model.Regnskab
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

  void "test parse nc-2014.xml"() {
    given:
    String xml = TestUtil.load("/nc-2014.xml")
    RegnskabXmlParser regnskabXmlParser = new RegnskabXmlParser()
    RegnskabData regnskabData = new RegnskabData()

    when:
    regnskabXmlParser.parseOgBerig(regnskabData, xml)

    Resultatopgoerelse ro = regnskabData.resultatopgoerelse

    then:
    ro.aaretsresultatTal.aaretsresultat.vaerdi == 28686000l
  }

  void "test parse nc-ifrs.xml"() {
    given:
    String xml = TestUtil.load("/nc-ifrs.xml")
    RegnskabXmlParser regnskabXmlParser = new RegnskabXmlParser()
    RegnskabData regnskabData = new RegnskabData()

    when:
    regnskabXmlParser.parseOgBerig(regnskabData, xml)

    Resultatopgoerelse ro = regnskabData.resultatopgoerelse

    then:
    ro.aaretsresultatTal.aaretsresultat.vaerdi == 108187000l
  }

  void "test csc consulting"() {
    given:
    String xml = TestUtil.load("/csc-consulting.xml")
    RegnskabXmlParser regnskabXmlParser = new RegnskabXmlParser()
    RegnskabData regnskabData = new RegnskabData()

    when:
    regnskabXmlParser.parseOgBerig(regnskabData, xml)

    Resultatopgoerelse ro = regnskabData.resultatopgoerelse

    then:
    ro.aaretsresultatTal.aaretsresultat.vaerdi == 149267l
  }

  void "test csc 2013"() {
    given:
    String xml = TestUtil.load("/csc-2013.xml")
    RegnskabXmlParser regnskabXmlParser = new RegnskabXmlParser()
    RegnskabData regnskabData = new RegnskabData()

    when:
    regnskabXmlParser.parseOgBerig(regnskabData, xml)

    Resultatopgoerelse ro = regnskabData.resultatopgoerelse

    then:
    ro.aaretsresultatTal.resultatfoerskat.vaerdi == -210000000l
    ro.aaretsresultatTal.aaretsresultat.vaerdi == -202000000l
  }

  void "test parse greener pastures"() {
    given:
    String xml = TestUtil.load('/greener_pastures.xml')
    RegnskabXmlParser regnskabXmlParser = new RegnskabXmlParser()
    RegnskabData regnskabData = new RegnskabData()

    when:
    regnskabXmlParser.parseOgBerig(regnskabData, xml)

    Resultatopgoerelse ro = regnskabData.resultatopgoerelse

    then:
    ro.bruttoresultatTal.bruttofortjeneste.vaerdi == 4100142l
    ro.aaretsresultatTal.aaretsresultat.vaerdi == 541316l
    ro.nettoresultatTal.driftsresultat.vaerdi == 727959l
    ro.aaretsresultatTal.skatafaaretsresultat.vaerdi == 181047l

    regnskabData.balance.passiver.gaeldsforpligtelser.vaerdi == 1258900l
    regnskabData.balance.passiver.egenkapital.vaerdi == 1414171l

  }

  void "test parse capgemini"() {
    given:
    String xml = TestUtil.load('/capgemini.xml')
    RegnskabXmlParser regnskabXmlParser = new RegnskabXmlParser()
    RegnskabData regnskabData = new RegnskabData()

    when:
    regnskabXmlParser.parseOgBerig(regnskabData, xml)

    Resultatopgoerelse ro = regnskabData.resultatopgoerelse

    then:
    ro.bruttoresultatTal.bruttofortjeneste.vaerdi == 68539000l
    ro.nettoresultatTal.driftsresultat.vaerdi == 18567000l
    ro.aaretsresultatTal.resultatfoerskat.vaerdi == 18762000l
    ro.aaretsresultatTal.skatafaaretsresultat.vaerdi == 4240000l
    ro.aaretsresultatTal.aaretsresultat.vaerdi == 14522000l
    regnskabData.balance.passiver.gaeldsforpligtelser.vaerdi == 80864000l
    regnskabData.balance.passiver.egenkapital.vaerdi == 61554000l

  }

  void "test parse nine"() {
    given:
    String xml = TestUtil.load('/nine.xml')
    RegnskabXmlParser regnskabXmlParser = new RegnskabXmlParser()
    RegnskabData regnskabData = new RegnskabData()

    when:
    regnskabXmlParser.parseOgBerig(regnskabData, xml)

    Resultatopgoerelse ro = regnskabData.resultatopgoerelse

    then:
    ro.bruttoresultatTal.bruttofortjeneste.vaerdi == 87883765l
    ro.nettoresultatTal.driftsresultat.vaerdi == 19658442l
    ro.aaretsresultatTal.resultatfoerskat.vaerdi == 19704409l
    ro.nettoresultatTal.finansielleindtaegter.vaerdi == 63203l
    ro.nettoresultatTal.finansielleomkostninger.vaerdi == 17236l
    ro.aaretsresultatTal.skatafaaretsresultat.vaerdi == 4189184l
    ro.aaretsresultatTal.aaretsresultat.vaerdi == 15515225l
    regnskabData.balance.passiver.gaeldsforpligtelser.vaerdi == 19030055l
    regnskabData.balance.passiver.egenkapital.vaerdi == 31210704l

    // revision
    regnskabData.revision
    regnskabData.revision.navnPaaRevisor == 'Marian Fruergaard'
    regnskabData.revision.revisionsfirmaNavn == 'Redmark, Statsautoriseret Revisionspartnerselskab'
    regnskabData.revision.revisionsfirmaCvrnummer == '29442789'
    regnskabData.revision.beskrivelseAfRevisor == 'statsautoriseret revisor'
    regnskabData.revision.beliggenhedsadresse
    regnskabData.revision.beliggenhedsadresse.vejadresselinie == 'Hasseris Bymidte 6'
    regnskabData.revision.beliggenhedsadresse.byLinje == '9000 Aalborg'
    regnskabData.revision.beliggenhedsadresse.land == 'Danmark'
    regnskabData.revision.telefonnummer == '98183333'
    regnskabData.revision.email == 'aalborg@redmark.dk'
    regnskabData.revision.generalforsamling
    regnskabData.revision.generalforsamling.dato == '2016-10-11'
    regnskabData.revision.generalforsamling.formand == 'Finn Peder Hove\n  '
    regnskabData.revision.assistancetype == 'Revisionspåtegning'
    regnskabData.regnskabsklasse == 'Regnskabsklasse C, mellemstor virksomhed'

    regnskabData.revision.godkendelsesdato == '2016-10-10'
    regnskabData.revision.revisionUnderskriftsted == 'Aalborg'
    regnskabData.revision.revisionUnderskriftdato == '2016-10-10'
    regnskabData.revision.adressant == 'aktionærerne\n  '

    when:
    Virksomhedsdata virksomhedsdata = regnskabXmlParser.hentVirksomhedsdataFraRegnskab(xml)

    then:
    virksomhedsdata
    virksomhedsdata.cvrnummer == '30714024'
    virksomhedsdata.navn == 'NineConsult A/S'
    virksomhedsdata.vejnavn == 'Kongens Nytorv'
    virksomhedsdata.postnr == '1050'
    virksomhedsdata.bynavn == 'København K'

    when:
    Regnskab sidsteAar = new Regnskab()
    regnskabXmlParser.parseOgBerig(sidsteAar, xml, false )

    then:
    sidsteAar

  }

  void "test mne"() {
    given:
    String xml = TestUtil.load('/FR77a_2.xml')
    RegnskabXmlParser regnskabXmlParser = new RegnskabXmlParser()
    RegnskabData regnskabData = new RegnskabData()

    when:
    regnskabXmlParser.parseOgBerig(regnskabData, xml)

    then:
    regnskabData.revision.mnenummer == 'mne33215'
  }

  void "test assistance"() {
    given:
    String xml = TestUtil.load('/ingen_revision.xml')
    RegnskabXmlParser regnskabXmlParser = new RegnskabXmlParser()
    RegnskabData regnskabData = new RegnskabData()

    when:
    regnskabXmlParser.parseOgBerig(regnskabData, xml)

    then:
    regnskabData.revision.assistancetype == 'Ingen bistand'
  }

  void "test parse lagkagehuset"() {
    given:
    String xml = TestUtil.load('/lagkagehuset.xml')
    RegnskabXmlParser regnskabXmlParser = new RegnskabXmlParser()
    RegnskabData regnskabData = new RegnskabData()

    when:
    regnskabXmlParser.parseOgBerig(regnskabData, xml)
    Resultatopgoerelse ro = regnskabData.resultatopgoerelse

    then:
    ro.omsaetningTal.omsaetning.vaerdi == 552449000l
    ro.bruttoresultatTal.regnskabsmaessigeafskrivninger.vaerdi == 28137000l
    ro.bruttoresultatTal.medarbejderomkostninger.vaerdi == 213730000l
    ro.nettoresultatTal.finansielleindtaegter.vaerdi == 795000

    ro.aaretsresultatTal.resultatfoerskat.vaerdi == 47316000l
    ro.nettoresultatTal.finansielleomkostninger.vaerdi == 4376000l
    ro.aaretsresultatTal.skatafaaretsresultat.vaerdi == 9175000l
    ro.aaretsresultatTal.aaretsresultat.vaerdi == 38141000l
    regnskabData.balance.passiver.gaeldsforpligtelser.vaerdi == 97955000l
    regnskabData.balance.passiver.egenkapital.vaerdi == 75968l
  }
}
