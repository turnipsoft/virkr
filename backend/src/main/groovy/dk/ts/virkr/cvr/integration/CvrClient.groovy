package dk.ts.virkr.cvr.integration

import dk.ts.virkr.aarsrapporter.util.JsonMarshaller
import dk.ts.virkr.cvr.integration.model.deltager.Vrdeltagerperson
import dk.ts.virkr.cvr.integration.model.deltager.VrdeltagerpersonWrapper
import dk.ts.virkr.cvr.integration.model.deltager.elasticsearch.ElasticGetDeltagerResult
import dk.ts.virkr.cvr.integration.model.virksomhed.elasticsearch.ElasticResult
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

  @Value('${virkr.cvr.deltagerurl}')
  deltagerurl

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

  Vrdeltagerperson hentDeltager(String enhedsnummer) {
    String url = "$deltagerurl/$enhedsnummer"
    String jsonResult = kaldCvr(url)
    JsonMarshaller marshaller = new JsonMarshaller(true)
    ElasticGetDeltagerResult elasticGetDeltagerResult = marshaller.toObject(jsonResult, ElasticGetDeltagerResult.class)
    return elasticGetDeltagerResult._source.vrdeltagerperson
  }


  static String virksomhedsdeltagerSoegning = '''{
\t"_source" : ["Vrvirksomhed.cvrNummer","Vrvirksomhed.enhedsNummer","Vrvirksomhed.virksomhedMetadata.nyesteNavn", "Vrvirksomhed.virksomhedMetadata.nyesteBeliggenhedsadresse", "Vrvirksomhed.deltagerRelation.deltager", "Vrvirksomhed.deltagerRelation.organisationer"],
    "size" : 50,
    "query" : {
    \t"bool" : {
    \t\t"must" : [
    \t\t\t{"term": {"Vrvirksomhed.deltagerRelation.deltager.enhedsNummer":"${enhedsnummer}"}},
    \t\t\t{
        \t\t\t"nested": {
            \t\t\t"path": "Vrvirksomhed.deltagerRelation", 
            \t\t\t"query": {
            \t\t\t"bool": {
                \t\t\t"must": [ 
                \t\t\t\t{
                    \t\t\t\t"match": {
                    \t\t\t\t\t"Vrvirksomhed.deltagerRelation.organisationer.hovedtype": "REGISTER"
                    \t\t\t\t}
\t               \t\t\t\t}
                \t\t\t]
            \t\t\t}
            \t\t}
        \t\t}
        \t}
    \t\t]
    \t}
    }
}
'''

  List<Vrvirksomhed> hentVirksomhedsDeltagere(String enhedsnummer) {
    String soegning = virksomhedsdeltagerSoegning.replace('${enhedsnummer}', enhedsnummer)
    String jsonResult = kaldCvr(url, soegning)
    return marshallVirksomheder(jsonResult)
  }

  private ArrayList<Vrvirksomhed> marshallVirksomheder(String jsonResult) {
    List<Vrvirksomhed> resultat = new ArrayList<>()

    JsonMarshaller marshaller = new JsonMarshaller(true)
    ElasticResult elasticResult = marshaller.toObject(jsonResult, ElasticResult.class)
    elasticResult.hits.hits.each { hit ->
      if (hit._source.vrvirksomhed) {
        resultat << hit._source.vrvirksomhed
      }
    }
    resultat
  }

  String prepareNavneQuery(String navn) {
    navn = navn.replace("{SLASH}","/")
    String navnequery=''
    if (navn.contains('%20')) {
      navn.split('%20').each {
        if (navnequery.length()>0) {
          navnequery += " AND "
        }
        navnequery += it
      }
      navnequery='('+navnequery+')'
    } else {
      navnequery=navn
    }
    return navnequery
  }

  List<Vrvirksomhed> soeg(String navn) {
    String navnequery = prepareNavneQuery(navn)

    navnequery = "Vrvirksomhed.virksomhedMetadata.nyesteNavn.navn:$navnequery"

    String statusquery = '(Vrvirksomhed.virksomhedMetadata.sammensatStatus:(NORMAL OR Normal OR Aktiv OR UNDERFRIVILLIGLIKVIDATION OR UNDERTVANGSOPLOESNING))'
    String include = 'Vrvirksomhed.virksomhedMetadata.nyesteNavn.navn,Vrvirksomhed.virksomhedMetadata.nyesteBeliggenhedsadresse,Vrvirksomhed.cvrNummer'
    String query = "($navnequery OR cvrNummer:$navn) AND $statusquery"
    query = URLEncoder.encode(query,'UTF-8').replace('+','%20')
    String url = "$url?q=$query&_source_include=$include&_source_exclude=entities"

    String jsonResult = kaldCvr(url)

    return marshallVirksomheder(jsonResult)
  }

  List<Vrdeltagerperson> soegDeltagere(String navn) {
    String navnequery = prepareNavneQuery(navn)
    navnequery = "Vrdeltagerperson.navne.navn:$navnequery"

    String include = 'Vrdeltagerperson.navne.navn,Vrdeltagerperson.enhedsNummer,Vrdeltagerperson.enhedstype,Vrdeltagerperson.deltagerpersonMetadata,Vrdeltagerperson.virksomhedSummariskRelation'
    String query = URLEncoder.encode(navnequery,'UTF-8').replace('+','%20')
    String url = "$url?q=$query&_source_include=$include&_source_exclude=entities"

    String jsonResult = kaldCvr(url)

    List<Vrdeltagerperson> resultat = []
    JsonMarshaller marshaller = new JsonMarshaller(true)
    dk.ts.virkr.cvr.integration.model.deltager.elasticsearch.ElasticResult elasticResult =
      marshaller.toObject(jsonResult, dk.ts.virkr.cvr.integration.model.deltager.elasticsearch.ElasticResult.class)

    elasticResult.hits.hits.each { hit->
      if (hit._source.vrdeltagerperson) {
        resultat << hit._source.vrdeltagerperson
      }
    }

    return resultat
  }

  private String kaldCvr(String url, String body=null) {
    log.debug("Invoking URL: $url")
    URL u = new URL(url)

    HttpURLConnection uc = (HttpURLConnection) u.openConnection()
    String userpass = username + ":" + password
    String basicAuth = "Basic " + new String(Base64.encoder.encode(userpass.bytes))
    uc.setRequestProperty("Authorization", basicAuth)
    if (body) {
      uc.doOutput = true
      uc.requestMethod = 'POST'
      uc.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
      uc.outputStream.write(body.bytes)
      uc.outputStream.flush()
    }
    try {
      InputStream i = uc.getInputStream()
      String jsonResult = i.getText()

      log.debug("Got result size ${jsonResult.size()}")
      jsonResult
    } catch (IOException ioe) {
      throw ioe
    }
  }

}
