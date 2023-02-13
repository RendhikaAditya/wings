package co.dh.wings.database.repository

import androidx.lifecycle.LiveData
import co.dh.wings.database.AppDatabase
import co.dh.wings.database.dao.TransactionDao
import co.dh.wings.network.response.Transactions
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val transactionDao: TransactionDao) {

    val transactions: LiveData<List<Transactions>> = transactionDao.getAll()



    fun checkTransactionExists(transactions: Transactions): Boolean {
        val transactionExists = transactionDao.checkTransasctionExists(transactions.product_code)
        return transactionExists != null
    }

    suspend fun insert(transactions: Transactions) {
        transactionDao.insertTransactions(transactions)
    }

    suspend fun delete(transactions: Transactions) {
        transactionDao.delete(transactions)
    }


    fun updateByProduct(qty:Int, productCode: String){
        transactionDao.updateTransactionByProduct(qty, productCode)
    }
    fun getQtyProduct(productCode: String): Int? {
        return transactionDao.getQtyProduct(productCode)
    }





}