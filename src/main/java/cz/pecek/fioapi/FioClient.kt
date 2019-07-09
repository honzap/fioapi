package cz.pecek.fioapi

import cz.pecek.fioapi.model.FioAccountStatement
import cz.pecek.fioapi.parser.FioParser
import java.io.InputStream
import java.time.LocalDate

private const val DATA_FORMAT = "json"

private const val URL_ARG_FORMAT = "format"
private const val URL_ARG_TOKEN = "token"
private const val URL_ARG_YEAR = "year"
private const val URL_ARG_STATEMENT_NUMBER = "id"
private const val URL_ARG_DATE_FROM = "date-from"
private const val URL_ARG_DATE_TO = "date-to"
private const val URL_ARG_TRANSACTION_ID = "id"
private const val URL_ARG_DATE = "date"

private const val FIO_CZ_BASE_URL = "https://www.fio.cz/ib_api/rest/"
private const val GET_STATEMENTS_PATH =
    "by-id/{$URL_ARG_TOKEN}/{$URL_ARG_YEAR}/{$URL_ARG_STATEMENT_NUMBER}/transactions.{$URL_ARG_FORMAT}"
private const val GET_TRANSACTIONS_PATH =
    "periods/{$URL_ARG_TOKEN}/{$URL_ARG_DATE_FROM}/{$URL_ARG_DATE_TO}/transactions.{$URL_ARG_FORMAT}"
private const val GET_NEW_TRANSACTIONS_PATH =
    "last/{$URL_ARG_TOKEN}/transactions.{$URL_ARG_FORMAT}"
private const val SET_LAST_ID_PATH =
    "set-last-id/{$URL_ARG_TOKEN}/{$URL_ARG_TRANSACTION_ID}/"
private const val SET_LAST_DATE_PATH =
    "set-last-date/{$URL_ARG_TOKEN}/{$URL_ARG_DATE}/"

/**
 * FIO Bank API client.<br />
 *
 * Universal class which can be used in any Java/Kotlin project (also Android). It uses only pure
 * Java API and allows to use any of library for connecting to HTTPS and parsing JSON response.
 *
 * To use this class the instances of [FioWebConnector] and [FioJsonConverter] should be created.
 * First of all the <b>token</b> should be get from Internet Banking.
 *
 * Reponse from the server is parsed and converted to usable data classes. It allows only to GET
 * the data from server, not to send a new payment order. Setting up the pointer of the last
 * transaction or date touched when asking for new transactions is also supported.
 *
 * @see https://www.fio.cz/bankovni-sluzby/api-bankovnictvi
 */
class FioClient(
    /**
     * Instance of web connector (see [FioWebConnector])
     */
    private val webConnector: FioWebConnector,
    /**
     * Instance of JSON converter (see [FioJsonConverter])
     */
    jsonConverter: FioJsonConverter,
    /**
     * Access token for FIO API
     */
    token: String,
    /**
     * Base API URL
     */
    private val baseUrl: String = FIO_CZ_BASE_URL
) {

    private val parser = FioParser(jsonConverter)

    private val globalUrlArguments = mapOf(
        URL_ARG_TOKEN to token,
        URL_ARG_FORMAT to DATA_FORMAT
    )

    /**
     * Get list of transactions between specific dates
     *
     * @param dateFrom Starting date requested
     * @param dateTo Ending date requested
     * @return List of transactions with covering info
     */
    fun getTransactions(dateFrom: LocalDate, dateTo: LocalDate): FioAccountStatement {
        val url = buildUrl(
            GET_TRANSACTIONS_PATH, mapOf(
                URL_ARG_DATE_FROM to dateFrom.toString(),
                URL_ARG_DATE_TO to dateTo.toString()
            )
        )
        return executeDataRequest(url)
    }

    /**
     * Get list of transactions newly appeared after the last request
     *
     * @return List of transactions with covering info
     */
    fun getNewTransactions(): FioAccountStatement {
        val url = buildUrl(GET_NEW_TRANSACTIONS_PATH, mapOf())
        return executeDataRequest(url)
    }

    /**
     * Get one specific account statement
     *
     * @param year Year of the statement
     * @param statementNumber Number of the statement for specific year
     * @return List of transactions with covering info
     */
    fun getStatement(year: Int, statementNumber: Int): FioAccountStatement {
        val url = buildUrl(
            GET_STATEMENTS_PATH, mapOf(
                URL_ARG_YEAR to year.toString(),
                URL_ARG_STATEMENT_NUMBER to statementNumber.toString()
            )
        )
        return executeDataRequest(url)
    }

    /**
     * Set transaction pointer by date when asking for new transactions in [getNewTransactions]
     *
     * @param date Date pointer to be set as a last visited date
     * @return response data form server
     */
    fun setTransactionPointerByDate(date: LocalDate): InputStream {
        val url = buildUrl(SET_LAST_DATE_PATH, mapOf(URL_ARG_DATE to date.toString()))
        return executeRequest(url)
    }

    /**
     * Set transaction pointer by transaction ID when asking for new transactions in [getNewTransactions]
     *
     * @param transactionId Transaction ID to be set as a last visited transaction
     * @return response data form server
     */
    fun setTransactionPointerById(transactionId: Int): InputStream {
        val url = buildUrl(
            SET_LAST_ID_PATH,
            mapOf(URL_ARG_TRANSACTION_ID to transactionId.toString())
        )
        return executeRequest(url)
    }

    private fun buildUrl(path: String, arguments: Map<String, String>): String {
        var url = "$baseUrl$path"
        (arguments + globalUrlArguments).forEach { (name, value) ->
            url = url.replaceFirst("{$name}", value)
        }
        return url
    }

    private fun executeDataRequest(url: String): FioAccountStatement =
        parser.parse(executeRequest(url))

    private fun executeRequest(url: String): InputStream = webConnector.getResponse(url)

}