package dk.ts.virkr.views

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Created by sorenhartvig on 19/05/2017.
 */
@Controller
class HomeController {
    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }
}
