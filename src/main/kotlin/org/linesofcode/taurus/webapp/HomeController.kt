package org.linesofcode.taurus.webapp

import org.linesofcode.taurus.domain.OrgNode
import org.linesofcode.taurus.redis_import.RedisImporter
import org.linesofcode.taurus.webapp.dto.OrgNodeToOrgNodeDTOTransformer
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

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var redisImporter: RedisImporter

    @Autowired
    private lateinit var orgNodeToOrgNodeDTOTransformer: OrgNodeToOrgNodeDTOTransformer

    @RequestMapping("/", method = [GET])
    fun home(model: ModelMap): String {
        model.addAttribute("rootNodes", orgNodeService.getRootNodes().map {
            node: OrgNode -> orgNodeToOrgNodeDTOTransformer.toDTO(node)
        })
        return "index"
    }

    @RequestMapping("/reload")
    fun reload(): String {
        redisImporter.reload()
        return "redirect:/"
    }
}