package co.dh.wings.view.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import co.dh.wings.database.AppDatabase
import co.dh.wings.database.repository.TransactionRepository
import co.dh.wings.network.response.Transactions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TransactionsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TransactionRepository
    val transactions: LiveData<List<Transactions>>

    init {

        val transactionDao = AppDatabase.getDatabase(application).userDao()
        repository = TransactionRepository(transactionDao)
        transactions = repository.transactions



    }
    fun checkTransactionExists(transactions: Transactions): Boolean {
        return runBlocking {
            repository.checkTransactionExists(transactions)
        }
    }
    fun getQty(productCode: String): Int? {
        return runBlocking {
            repository.getQtyProduct(productCode)
        }
    }

    fun insert(transactions: Transactions) = viewModelScope.launch {
        repository.insert(transactions)
    }


    fun updateTransactionByProductCode(qty:Int, productCode: String) = viewModelScope.launch {
        repository.updateByProduct(qty, productCode)
    }

    fun delete(transactions: Transactions) = viewModelScope.launch {
        repository.delete(transactions)
    }



}