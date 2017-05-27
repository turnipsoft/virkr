package dk.ts.virkr.batch

import dk.ts.virkr.aarsrapporter.db.Regnskabsdata
import dk.ts.virkr.aarsrapporter.db.RegnskabsdataRepository
import dk.ts.virkr.aarsrapporter.integration.model.regnskabdata.RegnskabData
import dk.ts.virkr.aarsrapporter.util.Utils
import dk.ts.virkr.services.internal.RegnskabInternalService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.time.LocalDateTime

/**
 * Created by sorenhartvig on 27/05/2017.
 */
@Service
class AarsrapportOpdatering {

  Logger logger = LoggerFactory.getLogger(AarsrapportOpdatering.class)

  @Autowired
  RegnskabsdataRepository regnskabsdataRepository

  @Autowired
  RegnskabInternalService regnskabInternalService

  @Transactional
  int opdaterRegnskaber() {
    LocalDateTime sidsteAar = LocalDateTime.now().minusYears(1).minusMonths(1)

    List<Regnskabsdata> regnskaber = regnskabsdataRepository.hentSenesteRegnskabstal(sidsteAar)

    int count = 0

    regnskaber.each { regnskab ->
      String cvrnummer = regnskab.cvrnummer
      // hent regnskab fra offentliggoerelse
      List<RegnskabData> regnskaberFraOffentliggoerelsen = regnskabInternalService.hentRegskaberFraOffentliggoerlse(cvrnummer)
      // find alle hvis sidsteopdatering er nyere end dette regnskabs sidsteopdatering
      List<RegnskabData> nyere = regnskaberFraOffentliggoerelsen.findAll {
        Utils.toDateTime(it.sidsteOpdatering).isAfter(regnskab.sidsteopdatering)
      }

      nyere.each { ny ->
        // check om den findes
        List<Regnskabsdata> eksisterende = regnskabsdataRepository.findAllByCvrnummerAndStartdatoAndSlutdato(cvrnummer,
          Utils.toDate(ny.startDato), Utils.toDate(ny.slutDato))
        if (!eksisterende || eksisterende.size()==0) {
          logger.info("Gemmer årsrapport for ${ny.cvrNummer} : ${ny.startDato} - ${ny.slutDato}")
          count++
          regnskabsdataRepository.saveAndFlush(Regnskabsdata.from(ny))
        }
      }
    }

    return count
  }
}
