package org.linesofcode.taurus.infrastructure

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class AbstractReadFromBeginningListenerTest {
    private val listener = object : AbstractReadFromBeginningListener() {
        override val logger = LoggerFactory.getLogger("TestListener")


    }
}

