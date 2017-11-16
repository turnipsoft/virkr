package dk.ts.virkr.aarsrapporter.integration

import dk.ts.virkr.aarsrapporter.integration.model.elasticsearch.ElasticResult
import dk.ts.virkr.aarsrapporter.integration.model.regnskaber.Offentliggoerelse
import dk.ts.virkr.aarsrapporter.integration.model.regnskaber.OffentliggoerelseBeriger
import dk.ts.virkr.aarsrapporter.util.JsonMarshaller
import org.slf4j.Logger

/**
 * Klient til at kalde offentliggørelses service hos ERST
 */
class OffentliggoerelserClient {

  static final Logger log = org.slf4j.LoggerFactory.getLogger(OffentliggoerelserClient.class)

  String regnskaberEnpoint

  OffentliggoerelserClient(String url) {
    regnskaberEnpoint = url
  }

  List<Offentliggoerelse> hentOffentliggoerelserForCvrNummer(String cvrNummer) {
    String url = "${regnskaberEnpoint}?q=cvrNummer:$cvrNummer"

    log.debug("Fetch regnskaber at URL: $url")
    String jsonResult = new URL(url).getText()
    List<Offentliggoerelse> resultat = []
    JsonMarshaller marshaller = new JsonMarshaller(true)
    ElasticResult elasticResult = marshaller.toObject(jsonResult, ElasticResult.class)
    elasticResult.hits.hits.each { hit ->
      resultat << OffentliggoerelseBeriger.berig(hit._source)
    }

    resultat << new Offentliggoerelse()
  }
}
