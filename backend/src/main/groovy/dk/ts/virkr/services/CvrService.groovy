package dk.ts.virkr.services

import dk.ts.virkr.cvr.integration.CvrClient
import dk.ts.virkr.cvr.integration.model.virksomhed.EjerGraf
import dk.ts.virkr.cvr.integration.model.virksomhed.Vrvirksomhed
import dk.ts.virkr.maps.integration.MapService
import dk.ts.virkr.maps.model.GeoResponse
import dk.ts.virkr.maps.model.GeoResult
import dk.ts.virkr.services.internal.CvrInternalService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Created by sorenhartvig on 29/05/2017.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/cvr")
class CvrService {

  @Autowired
  CvrClient cvrClient

  @Autowired
  MapService mapService

  @Autowired
  CvrInternalService cvrInternalService

  @RequestMapping(value = "/{cvrnummer}", method = RequestMethod.GET)
  public Vrvirksomhed regnskab(@PathVariable String cvrnummer) {
    Vrvirksomhed vrvirksomhed =  cvrClient.hentVirksomhed(cvrnummer)
    return vrvirksomhed
  }

  @RequestMapping(value = "/search/{navn}", method = RequestMethod.GET)
  public List<Vrvirksomhed> search(@PathVariable String navn) {
    navn = navn.replace(" ","%20")
    List<Vrvirksomhed> vrvirksomheder =  cvrClient.soeg(navn)
    return vrvirksomheder
  }

  @RequestMapping(value = "/graf/{cvrnummer}", method = RequestMethod.GET)
  public EjerGraf graf(@PathVariable String cvrnummer) {
    EjerGraf ejerGraf = cvrInternalService.hentEjergraf(cvrnummer)
    return ejerGraf
  }

}
