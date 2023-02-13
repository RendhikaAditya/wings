package co.dh.wings.view.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import co.dh.wings.network.Repository

class CheckoutViewModelFactory (
        val repository: Repository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CheckoutViewModel(repository) as T
    }
}