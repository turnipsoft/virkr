package dk.ts.virkr.cvr.integration

import dk.ts.virkr.aarsrapporter.util.JsonMarshaller
import dk.ts.virkr.cvr.integration.model.elasticsearch.ElasticResult
import dk.ts.virkr.cvr.integration.model.virksomhed.Vrvirksomhed
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

/**
 * Created by sorenhartvig on 29/05/2017.
 */
@Service
class CvrClient {

  @Value('${virkr.cvr.url}')
  url

  @Value('${virkr.cvr.username}')
  username

  @Value('${virkr.cvr.password}')
  password

  static final Logger log = org.slf4j.LoggerFactory.getLogger(CvrClient.class)

  Vrvirksomhed hentVirksomhed(String cvrNummer) {
    String url = "$url?q=cvrNummer:$cvrNummer"

    String jsonResult = kaldCvr(url)

    Vrvirksomhed resultat = null

    JsonMarshaller marshaller = new JsonMarshaller(true)
    ElasticResult elasticResult = marshaller.toObject(jsonResult, ElasticResult.class)
    elasticResult.hits.hits.each { hit ->
      resultat = hit._source.vrvirksomhed
    }

    return resultat
  }

  List<Vrvirksomhed> soeg(String navn) {
    //String virksomhedsformquery = '(Vrvirksomhed.virksomhedMetadata.nyesteVirksomhedsform.virksomhedsformkode:(40 OR 45 OR 60 OR 70 OR 80))'
    String statusquery = '(Vrvirksomhed.virksomhedMetadata.sammensatStatus:(NORMAL OR Normal OR Aktiv))'
    String include = 'Vrvirksomhed.virksomhedMetadata.nyesteNavn.navn,Vrvirksomhed.cvrNummer'
    String query = "(Vrvirksomhed.virksomhedMetadata.nyesteNavn.navn:$navn OR cvrNummer:$navn) AND $statusquery";
    query = URLEncoder.encode(query,'UTF-8');
    String url = "$url?q=$query&_source_include=$include&_source_exclude=entities"

    String jsonResult = kaldCvr(url)

    List<Vrvirksomhed> resultat = new ArrayList<>()

    JsonMarshaller marshaller = new JsonMarshaller(true)
    ElasticResult elasticResult = marshaller.toObject(jsonResult, ElasticResult.class)
    elasticResult.hits.hits.each { hit ->
      if (hit._source.vrvirksomhed) {
        resultat << hit._source.vrvirksomhed
      }
    }

    return resultat
  }

  private String kaldCvr(String url) {
    log.debug("Invoking URL: $url")
    URL u = new URL(url)

    URLConnection uc = u.openConnection()
    String userpass = username + ":" + password
    String basicAuth = "Basic " + new String(Base64.encoder.encode(userpass.bytes))
    uc.setRequestProperty("Authorization", basicAuth)
    InputStream i = uc.getInputStream()
    String jsonResult = i.getText()

    log.debug("Got result size ${jsonResult.size()}")
    jsonResult
  }


}
