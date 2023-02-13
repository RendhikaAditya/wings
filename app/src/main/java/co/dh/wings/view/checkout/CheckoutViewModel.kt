package co.dh.wings.view.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.dh.wings.network.response.ProductResponse
import co.dh.wings.network.Repository
import co.dh.wings.network.response.BaseResponse
import co.dh.wings.network.response.Transactions
import co.id.tesmandiritmdb.network.Resource
import kotlinx.coroutines.launch

class CheckoutViewModel(
    val repository: Repository
) : ViewModel() {

    val base: MutableLiveData<Resource<BaseResponse>> = MutableLiveData()
    val baseDetail : MutableLiveData<Resource<BaseResponse>> = MutableLiveData()

    fun fetchTransaction(document_code: String,
                         document_number: String,
                         user: String,
                         total: Int) = viewModelScope.launch {
        base.value = Resource.Loading()
        try {
            val response = repository.fetchTransaction(document_code, document_number, user, total)
            base.value = Resource.Success(response.body()!!)
        } catch (e: Exception) {
            base.value = Resource.Error(e.message.toString())
        }
    }

    fun fetchTransactionDetail(
        document_code: String,
        document_number: String,
        product_code: String,
        price: String,
        quantity: String,
        unit: String,
        sub_total: String,
        currency:String
    ) = viewModelScope.launch {
        baseDetail.value = Resource.Loading()
        try {
            val response = repository.fetchTransactionDetail(document_code, document_number, product_code, price, quantity, unit, sub_total, currency)
            baseDetail.value = Resource.Success(response.body()!!)
        } catch (e: Exception) {
            baseDetail.value = Resource.Error(e.message.toString())
        }
    }



}