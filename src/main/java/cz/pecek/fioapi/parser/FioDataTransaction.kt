package cz.pecek.fioapi.parser

private const val COLUMN_PREFIX = "column"

internal class FioDataTransaction : HashMap<String, FioDataTransactionItem<*>>() {
    override operator fun get(key: String): FioDataTransactionItem<*>? =
        super.get(COLUMN_PREFIX + key)

}


internal data class FioDataTransactionItem<T>(
    val id: Int,
    val value: T,
    val name: String
)