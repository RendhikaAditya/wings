package co.dh.wings.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import co.dh.wings.network.Repository

class LoginViewModelFactory (
        val repository: Repository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(repository) as T
    }
}