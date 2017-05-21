package dk.ts.virkr.services

import dk.ts.virkr.services.internal.RegnskabInternalService
import dk.ts.virkr.services.model.RegnskaberHentResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Created by sorenhartvig on 19/05/2017.
 */
@CrossOrigin(origins = "http://localhost:8001")
@RestController
@RequestMapping("/regnskab")
class RegnskabService {

    @Autowired
    RegnskabInternalService regnskabInternalService

    @RequestMapping(value = "/{cvrnummer}", method = RequestMethod.GET)
    public RegnskaberHentResponse regnskab(@PathVariable String cvrnummer) {
        return regnskabInternalService.hentRegnskaber(cvrnummer)
    }
}
