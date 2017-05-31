package dk.ts.virkr.services

import dk.ts.virkr.cvr.integration.CvrClient
import dk.ts.virkr.cvr.integration.model.virksomhed.Vrvirksomhed
import dk.ts.virkr.maps.integration.MapService
import dk.ts.virkr.maps.model.GeoResponse
import dk.ts.virkr.maps.model.GeoResult
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

  @RequestMapping(value = "/{cvrnummer}", method = RequestMethod.GET)
  public Vrvirksomhed regnskab(@PathVariable String cvrnummer) {
    long start = System.currentTimeMillis()
    Vrvirksomhed vrvirksomhed =  cvrClient.hentVirksomhed(cvrnummer)
    String adresse = (vrvirksomhed.virksomhedMetadata.nyesteBeliggenhedsadresse.vejadresselinie + ", "
      + vrvirksomhed.virksomhedMetadata.nyesteBeliggenhedsadresse.byLinje)
    GeoResponse response = mapService.hentGeoInformationer(adresse)
    GeoResult geoResult = response.results[0]
    vrvirksomhed.virksomhedMetadata.nyesteBeliggenhedsadresse.lat = geoResult.geometry.location.lat
    vrvirksomhed.virksomhedMetadata.nyesteBeliggenhedsadresse.lng = geoResult.geometry.location.lng

    long elapsed = System.currentTimeMillis() - start
    println(" Elapsed "+elapsed)
    return vrvirksomhed
  }

}
