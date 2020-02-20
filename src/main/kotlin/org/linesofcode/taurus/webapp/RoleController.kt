package org.linesofcode.taurus.webapp

import org.linesofcode.taurus.domain.IdentityRole
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class RoleController {
    private val logger = LoggerFactory.getLogger(RoleController::class.java)

    @RequestMapping("/identityrole", method = [POST])
    @ResponseBody
    fun create(identityRole: IdentityRole) {
        logger.info("Got role $identityRole")
    }
}