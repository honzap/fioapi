package cz.pecek.fioapi

import java.io.InputStream

/**
 * Universal web connector interface.<br />
 *
 * It's possible to use any library to connect to API (like Java [java.net.URL] or any library). The
 * connector should only implements this interface and pass it to the [FioClient].
 */
interface FioWebConnector {

    /**
     * Gets response when calling the URL via GET method
     *
     * @param url Requesting URL, with full hostname and path
     * @return Stream from the library
     */
    fun getResponse(url: String): InputStream
}