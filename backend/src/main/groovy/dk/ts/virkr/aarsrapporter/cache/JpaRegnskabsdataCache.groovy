package dk.ts.virkr.aarsrapporter.cache

import dk.ts.virkr.aarsrapporter.db.Regnskabsdata
import dk.ts.virkr.aarsrapporter.db.RegnskabsdataRepository
import dk.ts.virkr.aarsrapporter.model.RegnskabData
import dk.ts.virkr.aarsrapporter.util.Utils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

import java.time.LocalDateTime

/**
 * JPA regnskabsdata cache, datasource skal være sat op i application.yml
 */
@Service
@Profile('!ERST')
class JpaRegnskabsdataCache implements RegnskabsdataCache {

  Logger logger = LoggerFactory.getLogger(JpaRegnskabsdataCache.class)

  @Autowired
  RegnskabsdataRepository regnskabsdataRepository

  @Override
  List<RegnskabData> hentRegnskaber(String cvrnummer) {
    List<Regnskabsdata> resultat = this.regnskabsdataRepository.findAllByCvrnummerOrderByStartdato(cvrnummer)
    if (resultat && resultat.size() > 0) {
      return resultat.collect {
        it.toJsonModel()
      }
    }

    return null;
  }

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  void gemRegnskabsdata(List<RegnskabData> rd) {
    rd.each {
      Regnskabsdata regnskabsdata = Regnskabsdata.from(it)
      regnskabsdataRepository.saveAndFlush(regnskabsdata)
    }
  }

  @Transactional
  @Override
  int opdaterCacheMedNyeRegnskaber() {
    LocalDateTime sidsteAar = LocalDateTime.now().minusYears(1).minusMonths(1)

    List<Regnskabsdata> regnskaber = regnskabsdataRepository.hentSenesteRegnskabstal(sidsteAar)

    int count = 0

    regnskaber.each { regnskab ->
      String cvrnummer = regnskab.cvrnummer
      // hent regnskab fra offentliggoerelse
      List<RegnskabData> regnskaberFraOffentliggoerelsen = regnskabInternalService.hentRegnskaberFraOffentliggoerelse(cvrnummer)
      // find alle hvis sidsteopdatering er nyere end dette regnskabs sidsteopdatering
      List<RegnskabData> nyere = regnskaberFraOffentliggoerelsen.findAll {
        Utils.toDateTime(it.sidsteopdatering).isAfter(regnskab.sidsteopdatering)
      }

      nyere.each { ny ->
        // check om den findes
        List<Regnskabsdata> eksisterende = regnskabsdataRepository.findAllByCvrnummerAndStartdatoAndSlutdato(cvrnummer,
          Utils.toDate(ny.startdato), Utils.toDate(ny.slutdato))
        if (!eksisterende || eksisterende.size()==0) {
          logger.info("Gemmer årsrapport for ${ny.cvrnummer} : ${ny.startdato} - ${ny.slutdato}")
          count++
          regnskabsdataRepository.saveAndFlush(Regnskabsdata.from(ny))
        }
      }
    }

    return count
  }

  @Override
  boolean supports(String name) {
    return CACHE_TYPE_JPA == name
  }
}
