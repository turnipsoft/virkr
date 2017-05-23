package dk.ts.virkr.services.internal

import dk.ts.virkr.aarsrapporter.db.Regnskabsdata
import dk.ts.virkr.aarsrapporter.db.RegnskabsdataRepository
import dk.ts.virkr.aarsrapporter.integration.OffentliggoerelserClient
import dk.ts.virkr.aarsrapporter.integration.RegnskabXmlClient
import dk.ts.virkr.aarsrapporter.integration.model.regnskabdata.RegnskabData
import dk.ts.virkr.aarsrapporter.integration.model.regnskaber.Offentliggoerelse
import dk.ts.virkr.services.model.RegnskaberHentResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by sorenhartvig on 19/05/2017.
 */
@Service
class RegnskabInternalService {

    @Autowired
    RegnskabsdataRepository regnskabsdataRepository

    List<RegnskabData> hentRegnskaberFraDb(String cvrnummer) {
        List<Regnskabsdata> resultat = this.regnskabsdataRepository.findAllByCvrnummerOrderByStartdato(cvrnummer)
        if (resultat && resultat.size()>0) {
            return resultat.collect {
                it.toJsonModel()
            }
        }

        return null;
    }

    void store(List<RegnskabData> rd) {
        rd.each {
            regnskabsdataRepository.saveAndFlush(Regnskabsdata.from(it))
        }
    }

    RegnskaberHentResponse hentRegnskaber(String cvrnummer) {

        RegnskaberHentResponse response = new RegnskaberHentResponse()
        response.cvrNummer = cvrnummer

        List<RegnskabData> rd = hentRegnskaberFraDb(cvrnummer)

        if (rd) {
            response.regnskabsdata = rd
        } else {
            response.regnskabsdata = hentRegskaberFraOffentliggoerlse(cvrnummer)
            // vi havde dem ikke så vi gemmer dem lige lokalt
            store(response.regnskabsdata)
            response.regnskabsdata.each {
                it.aar = it.slutDato.substring(0,4)
                it.id = "regnskab_${it.aar}"
            }

            response.regnskabsdata = response.regnskabsdata.sort {it.aar}

        }

        response.regnskabsdata = response.regnskabsdata.unique{it.aar}.sort {it.aar}

        return response
    }

    List<RegnskabData> hentRegskaberFraOffentliggoerlse(String cvrnummer) {
        OffentliggoerelserClient oc = new OffentliggoerelserClient()
        RegnskabXmlClient rc = new RegnskabXmlClient()

        List<Offentliggoerelse> offentliggoerelser = oc.hentOffentliggoerelserForCvrNummer(cvrnummer)
        return rc.hentRegnskabData(offentliggoerelser)
    }
}
