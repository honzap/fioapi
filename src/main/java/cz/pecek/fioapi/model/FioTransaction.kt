package cz.pecek.fioapi.model

import java.math.BigDecimal
import java.time.OffsetDateTime

/**
 * Transaction object with all mandatory and optional fields
 */
data class FioTransaction(
    /**
     * Transaction ID
     */
    val id: String,
    /**
     * Date of the transaction
     */
    val date: OffsetDateTime,
    /**
     * Transaction amount
     */
    val amount: BigDecimal,
    /**
     * Currency of transaction
     */
    val currency: String,
    /**
     * Transaction type
     */
    val type: FioTransactionType,
    /**
     * Partner (beneficiary) account number
     */
    val accountNumber: String?,
    /**
     * Name of the partner account
     */
    val accountName: String?,
    /**
     * Partner bank code
     */
    val bankCode: String?,
    /**
     * Name of the partner bank
     */
    val bankName: String?,
    /**
     * Constant symbol
     */
    val constantSymbol: String?,
    /**
     * Variable symbol
     */
    val variableSymbol: String?,
    /**
     * Specific symbol
     */
    val specificSymbol: String?,
    /**
     * User identification (detail information about transaction)
     */
    val userIdentification: String?,
    /**
     * Message for recipient
     */
    val message: String?,
    /**
     * Name of the person who created (invoked) the transaction
     */
    val invokedBy: String?,
    /**
     * Detail of the transaction (eg. original amount and currency)
     */
    val details: String?,
    /**
     * Additional comment for the transaction
     */
    val comment: String?,
    /**
     * Partner bank BIC
     */
    val bankBic: String?,
    /**
     * Payment order ID which is transaction connected to
     */
    val paymentOrderId: String?
)

/**
 * Type of the transaction
 */
enum class FioTransactionType {
    /**
     * (1) Incoming transaction within the bank
     */
    IN_BANK_IN,
    /**
     * (2) Outgoing transaction within the bank
     */
    IN_BANK_OUT,
    /**
     * (3) Income by cashier
     */
    CASHIER_IN,
    /**
     * (4) Outgoing by cashier
     */
    CASHIER_OUT,
    /**
     * (5) Income by cash
     */
    CASH_IN,
    /**
     * (6) Cash withdraw
     */
    CASH_OUT,
    /**
     * (7) Payment
     */
    PAYMENT,
    /**
     * (8) Income
     */
    INCOME,
    /**
     * (9, 12) Cashless payment
     */
    CASHLESS_PAYMENT,
    /**
     * (10) Cashless income
     */
    CASHLESS_INCOME,
    /**
     * (11) Card payment
     */
    CARD_PAYMENT,
    /**
     * (13) Interest from the loan
     */
    LOAN_INTEREST,
    /**
     * (14) Penalty fee
     */
    PENALTY_FEE,
    /**
     * (15) Delivery out payment
     */
    DELIVERY_OUT,
    /**
     * (16) Delivery payment - income
     */
    DELIVERY_INCOME,
    /**
     * (17) Payment inside the account
     */
    IN_ACCOUNT,
    /**
     * (18) Accounted interest
     */
    ACCOUNTED_INTEREST,
    /**
     * (19) Accounted interested already payed
     */
    ACCOUNTED_PAYED_INTEREST,
    /**
     * (20) Interest tax
     */
    INTEREST_TAX,
    /**
     * (21, 40) Listed interest
     */
    LISTED_INTEREST,
    /**
     * (22) Fee
     */
    FEE,
    /**
     * (23) Listed fee
     */
    LISTED_FEE,
    /**
     * (24) Own transfer (between own accounts) - outgoing
     */
    OWN_ACCOUNTS_OUT,
    /**
     * (25) Own transfer (between own accounts) - incoming
     */
    OWN_ACCOUNTS_IN,
    /**
     * (26) Unknown outgoing payment
     */
    UNKNOWN_OUT,
    /**
     * (27) Unknown incoming payment
     */
    UNKNOWN_IN,
    /**
     * (28) Own outgoing transfer
     */
    OWN_TRANSFER_OUT,
    /**
     * (29) Own incoming transfer
     */
    OWN_TRANSFER_IN,
    /**
     * (30) Outgoing payment by own cashier
     */
    OWN_CASHIER_OUT,
    /**
     * (31) Incoming payment by own casher
     */
    OWN_CASHIER_IN,
    /**
     * (32) Fix of the payment
     */
    PAYMENT_FIX,
    /**
     * (33) Incoming fee
     */
    INCOME_FEE,
    /**
     * (34) FX payment
     */
    FX_PAYMENT,
    /**
     * (35) Card payment fee
     */
    CARD_FEE,
    /**
     * (36) Direct debit
     */
    DIRECT_DEBIT,
    /**
     * (37) Direct debit with beneficiary account
     */
    DIRECT_DEBIT_ACCOUNT,
    /**
     * (38) Outgoing direct debit
     */
    DIRECT_DEBIT_OUT,
    /**
     * (39) Direct debit from outside of bank
     */
    DIRECT_DEBIT_IN,
}