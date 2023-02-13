package co.dh.wings.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import co.dh.wings.network.response.Transactions
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert
    suspend fun insertTransactions(transactions: Transactions)

    @Query("SELECT * FROM transactions")
    fun getAll(): LiveData<List<Transactions>>

    @Delete
    suspend fun delete(transactions: Transactions)

    @Query("UPDATE transactions SET quantity = :qty WHERE product_code = :productCode")
    fun updateTransactionByProduct(qty: Int, productCode: String)

    @Query("SELECT quantity FROM transactions WHERE product_code = :productCode LIMIT 1")
    fun getQtyProduct(productCode: String): Int?

    @Query("SELECT * FROM transactions WHERE product_code = :productCode LIMIT 1")
    fun checkTransasctionExists(productCode: String): Transactions



}