package co.dh.wings.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.dh.wings.network.response.ProductResponse
import co.dh.wings.network.Repository
import co.dh.wings.network.response.Transactions
import co.id.tesmandiritmdb.network.Resource
import kotlinx.coroutines.launch

class MainViewModel(
    val repository: Repository
) : ViewModel() {

    val product: MutableLiveData<Resource<ProductResponse>> = MutableLiveData()

    fun fetchProduct() = viewModelScope.launch {
        product.value = Resource.Loading()
        try {
            val response = repository.fetchProduct()
            product.value = Resource.Success(response.body()!!)
        } catch (e: Exception) {
            product.value = Resource.Error(e.message.toString())
        }
    }

}