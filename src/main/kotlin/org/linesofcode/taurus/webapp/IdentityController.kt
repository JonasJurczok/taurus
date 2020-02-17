package org.linesofcode.taurus.webapp

import org.linesofcode.taurus.domain.Identity
import org.linesofcode.taurus.domain.OrgNode
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
class IdentityController {

        private val logger = LoggerFactory.getLogger(IdentityController::class.java)

        @Autowired
        private lateinit var identityService: IdentityService

        /**    model.addAttribute("rootNodes", orgNodeService.getRootNodes().map {
        node: OrgNode ->
        node.toDTO(node.manager?.let { identityService.getById(it) }, identityService.getByIds(node.members))})
         **/

        @RequestMapping("/identity", method = [RequestMethod.GET])
        fun identityGet(model: ModelMap): String {
                fillModel(model)
                return "identity"
        }

        @RequestMapping("/identity", method = [RequestMethod.POST])
        fun createOrgNode(@ModelAttribute identity: Identity, bindingResult: BindingResult, model: ModelMap): String {
                if (bindingResult.hasErrors()) {
                        return "identity"
                }

                logger.info("Got Identity [{}] from website.", identity)
                identityService.create(identity)
                fillModel(model, identity.name)
                return "identity"
        }

        private fun fillModel(model: ModelMap, created: String? = null) {
                model.addAttribute("identity", Identity())
                model.addAttribute("created", created)
        }

}