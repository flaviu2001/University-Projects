package ro.ubb.flaviu.client.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ro.ubb.flaviu.client.data.AppDatabaseDao
import java.lang.IllegalArgumentException

class SecondViewModelFactory (
    private val database: AppDatabaseDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SecondViewModel::class.java)) {
            return SecondViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}