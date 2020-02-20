package org.linesofcode.taurus.webapp

import org.linesofcode.taurus.domain.OrgNode
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.ResponseBody
import java.lang.IllegalStateException
import java.util.UUID

@Controller
class OrgNodeController {
    private val logger = LoggerFactory.getLogger(OrgNodeController::class.java)

    @Autowired
    private lateinit var orgNodeService: OrgNodeService

    @Autowired
    private lateinit var identityService: IdentityService

    @RequestMapping("/orgnode", method = [GET])
    fun orgnodeGet(model: ModelMap): String {
        fillModel(model)
        return "orgnode"
    }

    @RequestMapping("/orgnode", method = [POST])
    fun createOrgNode(@ModelAttribute orgNode: OrgNode, bindingResult: BindingResult, model: ModelMap): String {
        if (bindingResult.hasErrors()) {
            return "orgnode"
        }

        logger.info("Got OrgNode [{}] from website.", orgNode)
        // catch illegalState
        val created = try {
            orgNodeService.createOrUpdate(orgNode)
            orgNode.name
        } catch (e: IllegalStateException) {
            bindingResult.addError(ObjectError("", e.message ?: ""))
            null
        }
        fillModel(model, created)

        return "orgnode"
    }

    private fun fillModel(model: ModelMap, created: String? = null) {
        model.addAttribute("orgNode", OrgNode())
        model.addAttribute("created", created)
        model.addAttribute("orgNodes", orgNodeService.getAll())
        model.addAttribute("identities", identityService.getAll())
    }

    @RequestMapping("/orgnode/{id}", method = [GET], produces = ["application/json"])
    @ResponseBody
    fun getById(@PathVariable id: UUID): OrgNode? {
        return orgNodeService.getById(id)
    }

    @RequestMapping("/orgnode/{id}", method = [GET], produces = ["text/html"])
    fun getDetailsHTMLByID(@PathVariable id: UUID, model: ModelMap): String {
        val node = orgNodeService.getById(id)

        logger.info("Getting nodeDetails as HTML for id [$id]")

        model.addAttribute("node", node?.toDTO(
                node.manager?.let { identityService.getById(it) },
                node.members.mapNotNull { identityService.getById(it) }.toSet())
        )
        return "fragments/nodeDetails :: nodeDetails"
    }

    @RequestMapping("/orgnode/{id}/children", method = [GET])
    fun children(@PathVariable id: UUID, model: ModelMap): String {
        logger.info("Searching for children of [{}].", id)

        model.addAttribute("nodes", orgNodeService.getChildrenById(id).map {
            node: OrgNode ->
            node.toDTO(node.manager?.let { identityService.getById(it) }, identityService.getByIds(node.members))})

        return "fragments/treeNode :: treeNode"
    }
}