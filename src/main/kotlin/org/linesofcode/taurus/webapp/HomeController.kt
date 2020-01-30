package org.linesofcode.taurus.webapp

import org.linesofcode.taurus.domain.Action
import org.linesofcode.taurus.domain.OrgNode
import org.linesofcode.taurus.domain.OrgNodeChangeEvent
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import java.util.UUID

@Controller
class HomeController {
    private val logger = LoggerFactory.getLogger(HomeController::class.java)

    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<UUID, OrgNodeChangeEvent>

    @RequestMapping("/", method = [GET])
    fun home(): String {
        return "index"
    }

    @RequestMapping("/staff", method = [GET])
    fun staff(model: ModelMap): String {
        model.addAttribute("orgNode", OrgNode())
        return "staff"
    }

    @RequestMapping("/orgnode", method = [POST])
    fun orgNode(@ModelAttribute orgNode: OrgNode, bindingResult: BindingResult, model: ModelMap): String {
        if (bindingResult.hasErrors()) {
            return "staff"
        }

        logger.info("Got OrgNode [{}].", orgNode)
        val event = OrgNodeChangeEvent(UUID.randomUUID(), Action.CREATE, orgNode)
        kafkaTemplate.send(OrgNodeChangeEvent.TOPIC_NAME, event.orgNode.id, event)
        return "staff"
    }

}