package org.linesofcode.taurus.webapp

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET

@Controller
class HomeController {
    private val logger = LoggerFactory.getLogger(HomeController::class.java)

    @RequestMapping("/", method = [GET])
    fun home(): String {
        return "index"
    }
}