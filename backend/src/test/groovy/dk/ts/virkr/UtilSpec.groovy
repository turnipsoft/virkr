package dk.ts.virkr

import dk.ts.virkr.aarsrapporter.util.Utils
import dk.ts.virkr.cvr.integration.model.virksomhed.Navn
import dk.ts.virkr.cvr.integration.model.virksomhed.Periode
import spock.lang.Specification

/**
 * Created by sorenhartvig on 08/12/2017.
 */
class UtilSpec extends Specification {

  Navn bygNavn(String navn, String fra, String til) {
    Navn n1 = new Navn()
    n1.periode = new Periode()
    n1.periode.gyldigFra = fra
    n1.periode.gyldigTil = til
    n1.navn = navn

    return n1
  }

  void "test navn"() {
    given:
    List<Navn> navne = []
    navne << bygNavn('Hans', '2012-01-01', '2013-01-01')

    when:
    Navn n = Utils.findNyeste(navne)

    then:
    n.navn == 'Hans'

    when:
    navne << bygNavn('Peter', '2011-01-01', '2011-12-31')
    n = Utils.findNyeste(navne)

    then:
    n.navn == 'Hans'

    when:
    navne << bygNavn('Robin', '2015-01-01', null)
    n = Utils.findNyeste(navne)

    then:
    n.navn == 'Robin'

    when:
    navne << bygNavn('Gerda', '2016-01-01', '2020-01-01')
    n = Utils.findNyeste(navne)

    then:
    n.navn == 'Robin'

  }
}
