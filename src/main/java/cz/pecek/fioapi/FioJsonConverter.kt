package cz.pecek.fioapi

import java.io.InputStream

/**
 * Universal JSON converter interface.<br />
 *
 * It's possible to use any library to parse JSON result of the call. Using that interface
 * any of library for JSON parsing can be used (Jackson, GSON...). It gives the freedom to connect
 * this library to any project.
 */
interface FioJsonConverter {

    /**
     * Deserialize stream from the [FioWebConnector] to the specified class.
     *
     * @param stream Input stream from connector
     * @param valueType Type of the value (class) to be converted to
     * @return Ready-to-use object
     */
    fun <T> deserialize(stream: InputStream, valueType: Class<T>): T

}