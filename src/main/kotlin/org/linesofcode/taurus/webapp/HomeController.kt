package org.linesofcode.taurus.webapp

import org.linesofcode.taurus.domain.OrgNode
import org.linesofcode.taurus.webapp.dto.OrgNodeDTO
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET

@Controller
class HomeController {

    @Autowired
    private lateinit var orgNodeService: OrgNodeService

    @Autowired
    private lateinit var identityService: IdentityService

    @RequestMapping("/", method = [GET])
    fun home(model: ModelMap): String {
        model.addAttribute("rootNodes", orgNodeService.getRootNodes().map {
            node: OrgNode ->
            node.toDTO(node.manager?.let { identityService.getById(it) }, identityService.getByIds(node.members))})
        return "index"
    }
}