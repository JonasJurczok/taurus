package org.linesofcode.taurus.webapp

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET

@Controller
class HomeController {
    private val logger = LoggerFactory.getLogger(HomeController::class.java)

    @Autowired
    private lateinit var orgNodeService: OrgNodeService

    @RequestMapping("/", method = [GET])
    fun home(model: ModelMap): String {
        model.addAttribute("rootNodes", orgNodeService.getRootNodes())
        return "index"
    }
}