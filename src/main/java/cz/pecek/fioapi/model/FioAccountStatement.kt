package cz.pecek.fioapi.model

/**
 * Cover of transactions/account statement result
 */
data class FioAccountStatement(
    /**
     * Information header
     */
    val info: FioInfo,
    /**
     * List of transactions as [FioTransaction]
     */
    val transactions: List<FioTransaction>
)