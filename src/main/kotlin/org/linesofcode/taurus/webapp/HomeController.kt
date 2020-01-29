package org.linesofcode.taurus.webapp

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET

@Controller
class HomeController {

    @RequestMapping("/", method = [GET])
    fun home(): String {
        return "index"
    }
}