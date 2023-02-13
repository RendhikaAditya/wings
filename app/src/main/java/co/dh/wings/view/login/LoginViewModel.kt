package co.dh.wings.view.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.dh.wings.network.response.LoginResponse
import co.dh.wings.network.Repository
import co.id.tesmandiritmdb.network.Resource
import kotlinx.coroutines.launch

class LoginViewModel(val repository: Repository
) : ViewModel() {

    val login: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()


    fun fetchLogin(User:String, Password:String) = viewModelScope.launch {
        login.value = Resource.Loading()
        try {
            val response = repository.fetchLogin(User,Password)
            login.value = Resource.Success(response.body()!!)
        } catch (e: Exception) {
            login.value = Resource.Error(e.message.toString())
        }
    }

}