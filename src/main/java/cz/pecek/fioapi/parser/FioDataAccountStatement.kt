package cz.pecek.fioapi.parser

import cz.pecek.fioapi.model.FioInfo

internal data class FioDataAccountStatementPayload(
    val accountStatement: FioDataAccountStatement
)

internal data class FioDataAccountStatement(
    val info: FioInfo,
    val transactionList: FioDataTransactions
)

internal data class FioDataTransactions(
    val transaction: Iterable<FioDataTransaction>
)