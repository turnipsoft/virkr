package dk.ts.virkr.aarsrapporter.integration

import dk.ts.virkr.aarsrapporter.config.Config
import dk.ts.virkr.aarsrapporter.integration.model.elasticsearch.ElasticResult
import dk.ts.virkr.aarsrapporter.integration.model.regnskaber.Offentliggoerelse
import dk.ts.virkr.aarsrapporter.integration.model.regnskaber.OffentliggoerelseBeriger
import dk.ts.virkr.aarsrapporter.util.JsonMarshaller
import groovy.json.JsonSlurper
import org.slf4j.Logger

/**
 * Created by sorenhartvig on 22/06/16.
 */
class OffentliggoerelserClient {

  static final Logger log = org.slf4j.LoggerFactory.getLogger(OffentliggoerelserClient.class)

  List<Offentliggoerelse> hentOffentliggoerelserForCvrNummer(String cvrNummer) {
    String url = "$Config.REGNSKABER_ENDPOINT?q=cvrNummer:$cvrNummer"

    log.debug("Invoking URL: $url")
    String jsonResult = new URL(url).getText()

    log.debug("Got result size ${jsonResult.size()}")
    JsonSlurper jsonSlurper = new JsonSlurper()
    Map<String, Object> jsonObject = jsonSlurper.parseText(jsonResult)

    List<Offentliggoerelse> resultat = []
    JsonMarshaller marshaller = new JsonMarshaller(true)
    ElasticResult elasticResult = marshaller.toObject(jsonResult, ElasticResult.class)
    elasticResult.hits.hits.each { hit ->
      resultat << OffentliggoerelseBeriger.berig(hit._source)
    }

    resultat << new Offentliggoerelse()
  }
}
