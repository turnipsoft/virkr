package dk.ts.virkr.services

import dk.ts.virkr.cvr.integration.CvrClient
import dk.ts.virkr.cvr.integration.model.deltager.Vrdeltagerperson
import dk.ts.virkr.cvr.integration.model.virksomhed.Beliggenhedsadresse
import dk.ts.virkr.services.model.DeltagerSoegeresultatWrapper
import dk.ts.virkr.services.model.Links
import dk.ts.virkr.services.model.Metadata
import dk.ts.virkr.services.model.VirksomhedSoegeresultatWrapper
import dk.ts.virkr.services.model.graf.DeltagerGraf
import dk.ts.virkr.services.model.graf.EjerGraf
import dk.ts.virkr.cvr.integration.model.virksomhed.Vrvirksomhed
import dk.ts.virkr.maps.integration.MapService
import dk.ts.virkr.services.internal.CvrInternalService
import dk.ts.virkr.services.model.DeltagerSoegeresultat
import dk.ts.virkr.services.model.VirkrSoegeresultat
import dk.ts.virkr.services.model.VirksomhedSoegeresultat
import dk.ts.virkr.services.model.metrics.Metric
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpRequest
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest

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

  @Autowired
  MetricsService metricsService

  @RequestMapping(value = "/{cvrnummer}", method = RequestMethod.GET)
  Vrvirksomhed regnskab(@PathVariable String cvrnummer) {
    try {
      Vrvirksomhed vrvirksomhed =  cvrClient.hentVirksomhed(cvrnummer)
      metricsService.increment(Metric.VIRKSOMHED)
      return vrvirksomhed
    } catch (Exception e) {
      metricsService.increment(Metric.FEJLVIRKSOMHED)
      throw e
    }
  }

  @RequestMapping(value ="/deltager/{enhedsnummer}", method = RequestMethod.GET)
  DeltagerSoegeresultat hentDeltager(@PathVariable enhedsnummer) {
    Vrdeltagerperson vrdeltagerperson = cvrClient.hentDeltager(enhedsnummer)
    DeltagerSoegeresultat deltagerSoegeresultat = cvrInternalService.tilDeltager(vrdeltagerperson)
    metricsService.increment(Metric.PERSON)
    return deltagerSoegeresultat
  }

  @RequestMapping(value="/deltager/virksomheder/{enhedsnummer}", method = RequestMethod.GET)
  List<Vrvirksomhed> deltagerVirksomheder(@PathVariable String enhedsnummer) {
    List<Vrvirksomhed> resultat = cvrClient.hentVirksomhedsDeltagere(enhedsnummer)
    return resultat
  }

  static final long defaultPageSize = 20
  static final long defaultPage = 1

  @RequestMapping(value = "/search/{navn}", method = RequestMethod.GET)
  VirksomhedSoegeresultatWrapper search(@PathVariable String navn, @RequestParam('page') Optional<Long> page,
                                        @RequestParam('pagesize') Optional<Long>pagesize, HttpServletRequest request ) {
    navn = navn.replace(" ","%20")
    long pgsize = pagesize.isPresent() ? pagesize.get() : defaultPageSize
    long pg = page.isPresent() ? page.get() : defaultPage
    VirksomhedSoegeresultatWrapper resultat = cvrInternalService.soegVirksomhed(navn, pg, pgsize)
    resultat.links = createLinks(request, navn, pg, pgsize, resultat.antalHits)
    return resultat
  }

  @RequestMapping(value = "/searchDeltager/{navn}", method = RequestMethod.GET)
  DeltagerSoegeresultatWrapper searchDeltager(@PathVariable String navn, @RequestParam('page') Optional<Long> page ,
                                              @RequestParam('pagesize') Optional<Long> pagesize, HttpServletRequest request) {
    navn = navn.replace(" ","%20")
    long pgsize = pagesize.isPresent() ? pagesize.get() : defaultPageSize
    long pg = page.isPresent() ? page.get() : defaultPage
    DeltagerSoegeresultatWrapper resultat =  cvrInternalService.soegDeltager(navn, pg, pgsize)
    resultat.links = createLinks(request, navn, pg, pgsize, resultat.antalHits)
    return resultat
  }

  @RequestMapping(value = "/searchVirkr/{navn}", method= RequestMethod.GET)
  VirkrSoegeresultat searchVirkr(@PathVariable String navn, @RequestParam('page') Optional<Long> page,
                                 @RequestParam('pagesize') Optional<Long> pagesize, HttpServletRequest request) {
    VirksomhedSoegeresultatWrapper virksomhedSoegeresultatWrapper = search(navn, page , pagesize, request)
    DeltagerSoegeresultatWrapper deltagerSoegeresultatWrapper = searchDeltager(navn, page, pagesize, request)
    VirkrSoegeresultat virkrSoegeresultat = new VirkrSoegeresultat()
    virkrSoegeresultat.deltagerSoegeresultat = deltagerSoegeresultatWrapper
    virkrSoegeresultat.virksomhedSoegeresultat = virksomhedSoegeresultatWrapper
    virkrSoegeresultat.meta = new Metadata()
    virkrSoegeresultat.meta.deltagerHits = deltagerSoegeresultatWrapper.antalHits
    virkrSoegeresultat.meta.virksomhedHits = virksomhedSoegeresultatWrapper.antalHits
    virkrSoegeresultat.meta.links = deltagerSoegeresultatWrapper.antalHits > virksomhedSoegeresultatWrapper.antalHits ?
      deltagerSoegeresultatWrapper.links : virksomhedSoegeresultatWrapper.links
    metricsService.increment(Metric.SOEGNING)
    return virkrSoegeresultat
  }

  Links createLinks(HttpServletRequest request, String soegning, long page, long pagesize, long hits) {

    long maxPage = Math.floor(hits / pagesize) + 1

    if (page>=maxPage) {
      page = maxPage
    }

    Links links = new Links()
    links.first = createURI(1,pagesize,soegning, request)
    if (page>1) {
      links.previous = createURI(page-1, pagesize, soegning, request)
    } else {
      links.previous = createURI(page, pagesize, soegning, request)
      links.atFirst = true
    }

    if (page != maxPage) {
      links.next = createURI(page+1, pagesize, soegning, request)
    } else {
      links.next = createURI(page, pagesize, soegning, request)
      links.atLast = true
    }

    links.current = page
    links.pagesize = pagesize

    links.last = createURI(maxPage, pagesize, soegning, request)

    return links
  }

  String createURI(long page, long pagesize, String soegning, HttpServletRequest request) {
    String contextPath = request.getContextPath()
    return "$contextPath/#/soegeresultat/$soegning?page=$page&pagesize=$pagesize"
  }

  @RequestMapping(value = "/graf/{cvrnummer}", method = RequestMethod.GET)
  EjerGraf graf(@PathVariable String cvrnummer) {
    EjerGraf ejerGraf = cvrInternalService.hentEjergraf(cvrnummer)
    return ejerGraf
  }

  @RequestMapping(value = "/deltagergraf/{enhedsnummer}", method = RequestMethod.GET)
  public DeltagerGraf deltagergraf(@PathVariable String enhedsnummer) {
    DeltagerGraf deltagerGraf = cvrInternalService.hentEjergrafForPerson(enhedsnummer)
    return deltagerGraf
  }

  @RequestMapping(value = "/virksomhedgraf/{cvrnummer}", method = RequestMethod.GET)
  public DeltagerGraf virksomhedgraf(@PathVariable String cvrnummer) {
    DeltagerGraf deltagerGraf = cvrInternalService.hentDeltagerGrafForVirksomhed(cvrnummer)
    return deltagerGraf
  }

}
