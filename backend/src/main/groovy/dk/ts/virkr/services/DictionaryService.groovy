package dk.ts.virkr.services

import dk.ts.virkr.aarsrapporter.dictionary.OrganizedReport
import dk.ts.virkr.aarsrapporter.dictionary.XbrlDictionary
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Created by sorenhartvig on 29/06/2018.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/dictionary")
class DictionaryService {

  @RequestMapping(value = "", method = RequestMethod.GET)
  OrganizedReport hentDictionary() {
    return XbrlDictionary.organizedReport
  }
}
