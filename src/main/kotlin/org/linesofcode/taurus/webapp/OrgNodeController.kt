package org.linesofcode.taurus.webapp

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
class OrgNodeController {
    private val logger = LoggerFactory.getLogger(OrgNodeController::class.java)

    @Autowired
    private lateinit var orgNodeService: OrgNodeService

    @RequestMapping("/staff", method = [RequestMethod.GET, RequestMethod.POST])
    fun staff(model: ModelMap): String {
        model.mergeAttributes(
                mapOf(
                        "orgNode" to OrgNode(),
                        "created" to null,
                        "orgNodes" to orgNodeService.getAll()
                )
        )
        return "staff"
    }

    @RequestMapping("/orgnode", method = [RequestMethod.POST])
    fun orgNode(@ModelAttribute orgNode: OrgNode, bindingResult: BindingResult, model: ModelMap): String {
        if (bindingResult.hasErrors()) {
            return "staff"
        }

        logger.info("Got OrgNode [{}] from website.", orgNode)
        orgNodeService.create(orgNode)

        model.addAttribute("created", orgNode.name)
        model.addAttribute("orgNode", OrgNode())
        model.addAttribute("orgNodes", orgNodeService.getAll())
        return "staff"
    }


}