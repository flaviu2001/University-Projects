package ro.ubb.flaviu.client.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import ro.ubb.flaviu.client.data.AppDatabaseDao
import ro.ubb.flaviu.client.data.DataApi
import ro.ubb.flaviu.client.data.models.DisplayItem
import ro.ubb.flaviu.client.data.models.Master
import ro.ubb.flaviu.client.data.models.Slave
import ro.ubb.flaviu.client.data.models.Thing

class HomeViewModel(val database: AppDatabaseDao) : ViewModel() {
    val items = MutableLiveData<List<DisplayItem>?>()
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun refresh() {
        viewModelScope.launch {
//            val result = DataApi.getSlaves()
//            items.value = result
        }
    }

    fun setItems(newItems: List<DisplayItem>, filter: String = "") {
        items.value = newItems.filter { it.quantity.contains(filter) }
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.deleteAll()
                database.insertThing(*(newItems.map { Thing(it.name, it.quantity) }.toTypedArray()))
            }
        }
    }

    fun onItemClicked(item: DisplayItem) {

    }
}