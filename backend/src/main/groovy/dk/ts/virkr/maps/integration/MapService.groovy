package dk.ts.virkr.maps.integration

import dk.ts.virkr.aarsrapporter.util.JsonMarshaller
import dk.ts.virkr.maps.model.GeoResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

/**
 * Created by sorenhartvig on 29/05/2017.
 */
@Service
class MapService {

  @Value('${virkr.maps.url}')
  String url

  @Value('${virkr.maps.apikey}')
  String apikey

  GeoResponse hentGeoInformationer(String adresse) {
    adresse = adresse.replaceAll(' ','+')
    String url = "$url?address=$adresse&key=$apikey"

    String jsonResult = new URL(url).getText()

    JsonMarshaller marshaller = new JsonMarshaller(true)
    GeoResponse result = marshaller.toObject(jsonResult, GeoResponse.class)
    return result
  }
}
