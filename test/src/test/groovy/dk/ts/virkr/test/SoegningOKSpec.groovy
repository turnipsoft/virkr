package dk.ts.virkr.test

import dk.ts.virkr.test.pages.VirkrSoegningPage
import geb.spock.GebReportingSpec


class SoegningOKSpec extends GebReportingSpec {

  void "Åbn siden"(){
    when:
    to VirkrSoegningPage

    then:
    at VirkrSoegningPage
  }

}
