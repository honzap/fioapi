package cz.pecek.fioapi.parser

import cz.pecek.fioapi.FioJsonConverter
import cz.pecek.fioapi.model.FioAccountStatement
import cz.pecek.fioapi.model.FioTransaction
import cz.pecek.fioapi.model.FioTransactionType
import java.io.InputStream
import java.lang.IllegalArgumentException
import java.math.BigDecimal
import java.time.OffsetDateTime

private const val COLUMN_ID = "22"
private const val COLUMN_DATE = "0"
private const val COLUMN_AMOUNT = "1"
private const val COLUMN_CURRENCY = "14"
private const val COLUMN_ACCOUNT_NUMBER = "2"
private const val COLUMN_ACCOUNT_HOLDER = "10"
private const val COLUMN_BANK_CODE = "3"
private const val COLUMN_BANK_NAME = "12"
private const val COLUMN_CONSTANT_SYMBOL = "4"
private const val COLUMN_VARIABLE_SYMBOL = "5"
private const val COLUMN_SPECIFIC_SYMBOL = "6"
private const val COLUMN_USER_IDENTIFICATION = "7"
private const val COLUMN_MESSAGE = "16"
private const val COLUMN_TYPE = "8"
private const val COLUMN_INVOKED_BY = "9"
private const val COLUMN_DETAILS = "18"
private const val COLUMN_COMMENT = "25"
private const val COLUMN_BIC = "26"
private const val COLUMN_PAYMENT_ORDER_ID = "17"

private val transactionTypes = mapOf(
    "Příjem převodem uvnitř banky" to FioTransactionType.IN_BANK_IN,
    "Platba převodem uvnitř banky" to FioTransactionType.IN_BANK_OUT,
    "Vklad pokladnou" to FioTransactionType.CASHIER_IN,
    "Výběr pokladnou" to FioTransactionType.CASHIER_OUT,
    "Vklad v hotovosti" to FioTransactionType.CASH_IN,
    "Výběr v hotovosti" to FioTransactionType.CASH_OUT,
    "Platba" to FioTransactionType.PAYMENT,
    "Příjem" to FioTransactionType.INCOME,
    "Bezhotovostní platba" to FioTransactionType.CASHLESS_PAYMENT,
    "Bezhotovostní příjem" to FioTransactionType.CASHLESS_INCOME,
    "Platba kartou" to FioTransactionType.CARD_PAYMENT,
    "Úrok z úvěru" to FioTransactionType.LOAN_INTEREST,
    "Sankční poplatek" to FioTransactionType.PENALTY_FEE,
    "Posel - předání" to FioTransactionType.DELIVERY_OUT,
    "Posel - příjem" to FioTransactionType.DELIVERY_INCOME,
    "Převod uvnitř konta" to FioTransactionType.IN_ACCOUNT,
    "Připsaný úrok" to FioTransactionType.ACCOUNTED_INTEREST,
    "Vyplacený úrok" to FioTransactionType.ACCOUNTED_PAYED_INTEREST,
    "Odvod daně z úroků" to FioTransactionType.INTEREST_TAX,
    "Evidovaný úrok" to FioTransactionType.LISTED_INTEREST,
    "Poplatek" to FioTransactionType.FEE,
    "Evidovaný poplatek" to FioTransactionType.LISTED_FEE,
    "Převod mezi bankovními konty (platba)" to FioTransactionType.OWN_ACCOUNTS_OUT,
    "Převod mezi bankovními konty (příjem)" to FioTransactionType.OWN_ACCOUNTS_IN,
    "Neidentifikovaná platba z bankovního konta" to FioTransactionType.UNKNOWN_OUT,
    "Neidentifikovaný příjem na bankovní konto" to FioTransactionType.UNKNOWN_IN,
    "Vlastní platba z bankovního konta" to FioTransactionType.OWN_TRANSFER_OUT,
    "Vlastní příjem na bankovní konto" to FioTransactionType.OWN_TRANSFER_IN,
    "Vlastní platba pokladnou" to FioTransactionType.OWN_CASHIER_OUT,
    "Vlastní příjem pokladnou" to FioTransactionType.OWN_CASHIER_IN,
    "Opravný pohyb" to FioTransactionType.PAYMENT_FIX,
    "Přijatý poplatek" to FioTransactionType.INCOME_FEE,
    "Platba v jiné měně" to FioTransactionType.FX_PAYMENT,
    "Poplatek - platební karta" to FioTransactionType.CARD_FEE,
    "Inkaso" to FioTransactionType.DIRECT_DEBIT,
    "Inkaso ve prospěch účtu" to FioTransactionType.DIRECT_DEBIT_ACCOUNT,
    "Inkaso z účtu" to FioTransactionType.DIRECT_DEBIT_OUT,
    "Příjem inkasa z cizí banky" to FioTransactionType.DIRECT_DEBIT_IN

)

/**
 * FIO parser parses the response.<br />
 *
 * It uses [InputStream] and converts that using interface [FioJsonConverter]. Stream already
 * converted to objects is then processed using internal objects. Those are converted (in case of
 * [FioTransaction]) to the public object.
 *
 * @param jsonConverter Instance of JSON converter class.
 */
class FioParser(private val jsonConverter: FioJsonConverter) {


    /**
     * Parsing the [InputStream]
     *
     * @param inputStream Input
     * @return Object with transactions and account info
     */
    fun parse(inputStream: InputStream): FioAccountStatement {
        val payload = jsonConverter.deserialize(
            inputStream,
            FioDataAccountStatementPayload::class.java
        )
        return try {
            FioAccountStatement(
                info = payload.accountStatement.info,
                transactions = payload.accountStatement.transactionList.transaction.map(::parseTransaction)
            )
        } catch (t: Throwable) {
            throw FioParserException("could not parse some data", t)
        }

    }

    private fun parseTransaction(transactionData: FioDataTransaction) = FioTransaction(
        id = transactionData[COLUMN_ID]?.value as String,
        date = transactionData[COLUMN_DATE]?.value as OffsetDateTime,
        amount = transactionData[COLUMN_AMOUNT]?.value as BigDecimal,
        currency = transactionData[COLUMN_CURRENCY]?.value as String,
        type = parseTransactionType(transactionData[COLUMN_TYPE]),
        accountNumber = transactionData[COLUMN_ACCOUNT_NUMBER]?.value as? String?,
        accountName = transactionData[COLUMN_ACCOUNT_HOLDER]?.value as? String?,
        bankCode = transactionData[COLUMN_BANK_CODE]?.value as? String?,
        bankName = transactionData[COLUMN_BANK_NAME]?.value as? String?,
        constantSymbol = transactionData[COLUMN_CONSTANT_SYMBOL]?.value as? String?,
        variableSymbol = transactionData[COLUMN_VARIABLE_SYMBOL]?.value as? String?,
        specificSymbol = transactionData[COLUMN_SPECIFIC_SYMBOL]?.value as? String?,
        userIdentification = transactionData[COLUMN_USER_IDENTIFICATION]?.value as? String?,
        message = transactionData[COLUMN_MESSAGE]?.value as? String?,
        invokedBy = transactionData[COLUMN_INVOKED_BY]?.value as? String?,
        details = transactionData[COLUMN_DETAILS]?.value as? String?,
        comment = transactionData[COLUMN_COMMENT]?.value as? String?,
        bankBic = transactionData[COLUMN_BIC]?.value as? String?,
        paymentOrderId = transactionData[COLUMN_PAYMENT_ORDER_ID]?.value as? String?
    )

    private fun parseTransactionType(item: FioDataTransactionItem<*>?): FioTransactionType =
        (item?.value as? String?)?.let { transactionTypes[it] }
            ?: throw IllegalArgumentException("could not find transaction type")


}