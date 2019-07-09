package cz.pecek.fioapi.parser

import java.lang.RuntimeException

/**
 * Exception thrown when something happens during parsing
 *
 * @param message Detailed message
 * @param throwable Caused by
 */
class FioParserException(message: String, throwable: Throwable? = null) :
    RuntimeException(message, throwable)