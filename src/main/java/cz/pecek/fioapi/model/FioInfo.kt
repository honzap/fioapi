package cz.pecek.fioapi.model

import java.math.BigDecimal
import java.time.LocalDate

/**
 * Summary info for list of transactions/account statement result
 */
data class FioInfo(
    /**
     * Account ID
     */
    val accountId: String,
    /**
     * Account currency
     */
    val currency: String,
    /**
     * Account IBAN
     */
    val iban: String,
    /**
     * BIC of this account (bank)
     */
    val bic: String,
    /**
     * Balance at the begin on the account in specified period
     */
    val openingBalance: BigDecimal,
    /**
     * Balance at the end on the account in specified period
     */
    val closingBalance: BigDecimal,
    /**
     * Start date of the period if period is requested
     */
    val dateStart: LocalDate?,
    /**
     * End date of the period if period is requested
     */
    val dateEnd: LocalDate?,
    /**
     * Year of the account statement if statement is requested
     */
    val yearList: Int?,
    /**
     * Number of the account statement if statement is requested
     */
    val idList: Int?,
    /**
     * Starting transaction ID if range of IDs is requested
     */
    val idFrom: String?,
    /**
     * Ending transaction ID if range of IDs is requested
     */
    val idTo: String?,
    /**
     * ID of last downloaded transaction when option of downloading New transactions is used
     */
    val idLastDownload: String?
)