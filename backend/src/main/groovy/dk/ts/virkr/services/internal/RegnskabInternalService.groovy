package dk.ts.virkr.services.internal

import dk.ts.virkr.aarsrapporter.cache.RegnskabsdataCacheFactory
import dk.ts.virkr.aarsrapporter.integration.OffentliggoerelserClient
import dk.ts.virkr.aarsrapporter.integration.RegnskabXmlClient
import dk.ts.virkr.aarsrapporter.model.RegnskabData
import dk.ts.virkr.aarsrapporter.integration.model.regnskaber.Offentliggoerelse
import dk.ts.virkr.aarsrapporter.parser.kontroller.RegnskabsKontroller
import dk.ts.virkr.services.model.RegnskaberHentResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by sorenhartvig on 19/05/2017.
 */
@Service
@Transactional
class RegnskabInternalService {

  Logger logger = LoggerFactory.getLogger(RegnskabInternalService.class)

  @Autowired
  RegnskabsdataCacheFactory regnskabsdataCacheFactory

  @Value('${virkr.aarsrapporter.caching}')
  Boolean useCaching;

  @Value('${virkr.aarsrapporter.url}')
  String aarsrapporterUrl

  RegnskaberHentResponse hentRegnskaber(String cvrnummer) {

    RegnskaberHentResponse response = new RegnskaberHentResponse()
    response.cvrNummer = cvrnummer

    List<RegnskabData> rd = (useCaching? regnskabsdataCacheFactory.getRegnskabsdataCache().hentRegnskaber(cvrnummer) : null)

    if (rd) {
      response.regnskabsdata = rd
    } else {
      response.regnskabsdata = hentRegnskaberFraOffentliggoerelse(cvrnummer)
      // Der fandtes ikke regnskabsdata i forvejen så de gemmes i databasen, med mindre caching er slået fra
      if (useCaching) {
        logger.info("Using cache to store regnskaber for ${cvrnummer}")
        regnskabsdataCacheFactory.getRegnskabsdataCache().gemRegnskabsdata(response.regnskabsdata)
      }
      response.regnskabsdata.each {
        it.aar = it.slutdato.substring(0, 4)
        it.id = "regnskab_${it.aar}"
      }

      // berig med kontroller
      RegnskabsKontroller kontroller = new RegnskabsKontroller()
      kontroller.kontrollerOgBerig(response.regnskabsdata)
      response.regnskabsdata = response.regnskabsdata.sort { it.aar }

    }

    response.regnskabsdata = response.regnskabsdata.unique { it.aar }.sort { it.aar }

    return response
  }

  List<RegnskabData> hentRegnskaberFraOffentliggoerelse(String cvrnummer) {
    OffentliggoerelserClient oc = new OffentliggoerelserClient(aarsrapporterUrl)
    RegnskabXmlClient rc = new RegnskabXmlClient()

    List<Offentliggoerelse> offentliggoerelser = oc.hentOffentliggoerelserForCvrNummer(cvrnummer)
    offentliggoerelser = offentliggoerelser.findAll{ it.regnskab != null }.unique {
      it.regnskab.regnskabsperiode.startDato
    }
    return rc.hentRegnskabData(offentliggoerelser)
  }
}
