package dk.ts.virkr.services

import dk.ts.virkr.cvr.integration.CvrClient
import dk.ts.virkr.cvr.integration.model.deltager.Vrdeltagerperson
import dk.ts.virkr.cvr.integration.model.virksomhed.Beliggenhedsadresse
import dk.ts.virkr.services.model.graf.DeltagerGraf
import dk.ts.virkr.services.model.graf.EjerGraf
import dk.ts.virkr.cvr.integration.model.virksomhed.Vrvirksomhed
import dk.ts.virkr.maps.integration.MapService
import dk.ts.virkr.services.internal.CvrInternalService
import dk.ts.virkr.services.model.DeltagerSoegeresultat
import dk.ts.virkr.services.model.VirkrSoegeresultat
import dk.ts.virkr.services.model.VirksomhedSoegeresultat
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
  Vrvirksomhed regnskab(@PathVariable String cvrnummer) {
    Vrvirksomhed vrvirksomhed =  cvrClient.hentVirksomhed(cvrnummer)
    return vrvirksomhed
  }

  @RequestMapping(value ="/deltager/{enhedsnummer}", method = RequestMethod.GET)
  DeltagerSoegeresultat hentDeltager(@PathVariable enhedsnummer) {
    Vrdeltagerperson vrdeltagerperson = cvrClient.hentDeltager(enhedsnummer)
    DeltagerSoegeresultat deltagerSoegeresultat = cvrInternalService.tilDeltager(vrdeltagerperson)
    return deltagerSoegeresultat
  }

  @RequestMapping(value="/deltager/virksomheder/{enhedsnummer}", method = RequestMethod.GET)
  List<Vrvirksomhed> deltagerVirksomheder(@PathVariable String enhedsnummer) {
    List<Vrvirksomhed> resultat = cvrClient.hentVirksomhedsDeltagere(enhedsnummer)
    return resultat
  }

  @RequestMapping(value = "/search/{navn}", method = RequestMethod.GET)
  List<Vrvirksomhed> search(@PathVariable String navn) {
    navn = navn.replace(" ","%20")
    List<Vrvirksomhed> vrvirksomheder =  cvrClient.soeg(navn)
    return vrvirksomheder
  }

  @RequestMapping(value = "/searchDeltager/{navn}", method = RequestMethod.GET)
  List<DeltagerSoegeresultat> searchDeltager(@PathVariable String navn) {
    navn = navn.replace(" ","%20")
    List<Vrdeltagerperson> vrdeltagerpersoner =  cvrClient.soegDeltagere(navn)
    return vrdeltagerpersoner.collect {it->
      if (it.enhedstype == 'ANDEN_DELTAGER') {
        return null
      }
      DeltagerSoegeresultat deltagerSoegeresultat = cvrInternalService.tilDeltager(it)
      return deltagerSoegeresultat
    } - null
  }

  @RequestMapping(value = "/searchVirkr/{navn}", method= RequestMethod.GET)
  VirkrSoegeresultat searchVirkr(@PathVariable String navn) {
    List<Vrvirksomhed> virksomheder = search(navn)
    List<DeltagerSoegeresultat> deltagerSoegeresultater = searchDeltager(navn)
    VirkrSoegeresultat virkrSoegeresultat = new VirkrSoegeresultat()
    virkrSoegeresultat.virksomheder = virksomheder.collect {
      VirksomhedSoegeresultat virksomhedSoegeresultat = new VirksomhedSoegeresultat()
      virksomhedSoegeresultat.cvrnr = it.cvrNummer
      virksomhedSoegeresultat.navn = it.virksomhedMetadata.nyesteNavn.navn
      virksomhedSoegeresultat.enhedsNummer = it.enhedsNummer
      if (it.virksomhedMetadata.nyesteBeliggenhedsadresse) {
        Beliggenhedsadresse b = it.virksomhedMetadata.nyesteBeliggenhedsadresse
        if (b.vejnavn && b.postnummer) {
          virksomhedSoegeresultat.adresseTekst = b.adresselinie
        }
      }
      return virksomhedSoegeresultat
    }
    virkrSoegeresultat.deltagere = deltagerSoegeresultater

    return virkrSoegeresultat
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
