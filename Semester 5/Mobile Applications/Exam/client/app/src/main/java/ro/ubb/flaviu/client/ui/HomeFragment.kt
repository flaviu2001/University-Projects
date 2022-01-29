package ro.ubb.flaviu.client.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.*
import ro.ubb.flaviu.client.data.Api
import ro.ubb.flaviu.client.data.AppDatabase
import ro.ubb.flaviu.client.data.DataApi
import ro.ubb.flaviu.client.data.models.DisplayItem
import ro.ubb.flaviu.client.data.models.Master
import ro.ubb.flaviu.client.data.models.ToSend
import ro.ubb.flaviu.client.databinding.HomeFragmentBinding


class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: HomeFragmentBinding
    private var displayItems = MutableLiveData<List<DisplayItem>>()
    private var masters = MutableLiveData<List<Master>>()
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    private var textToShow = MutableLiveData<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        val factory = HomeViewModelFactory(AppDatabase.getInstance(requireContext()).databaseDao)
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        val adapter = ItemsAdapter(ClickItemListener {
            viewModel.onItemClicked(it)
        }, viewModel)
        binding.itemList.adapter = adapter
        viewModel.items.observe(viewLifecycleOwner){ items ->
            binding.progressLoader.visibility = ProgressBar.GONE
            if (items == null) {
                Toast.makeText(context, "Error connecting to the server", Toast.LENGTH_LONG).show()
                return@observe
            }
            adapter.items = items
        }
        binding.filter.addTextChangedListener {
            viewModel.setItems(displayItems.value!!, it.toString())
        }
        Api.registerWS{
            Thread.sleep(1000)
            displayItems.postValue(Gson().fromJson(it, Array<Master>::class.java).toList().map { it2 -> DisplayItem(it2.name, "") })
            masters.postValue(Gson().fromJson(it, Array<Master>::class.java).toList())
        }
        binding.submit.setOnClickListener {
            val items = viewModel.items.value!!
            for (item in items) {
                if (item.quantity != "")
                uiScope.launch {
                    withContext(Dispatchers.IO) {
                        val code = masters.value!!.find { it.name == item.name }
                        val text = DataApi.update(ToSend(code!!.code, item.quantity.toInt()))
                        if (text != "OK")
                            textToShow.postValue("$text for {${code.code} ${item.name}}")
                    }
                }
            }
        }
        textToShow.observe(viewLifecycleOwner) { text ->
            Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
        }
        displayItems.observe(viewLifecycleOwner) {
            viewModel.setItems(it)
            binding.progressLoader.visibility = ProgressBar.GONE
        }
        return binding.root
    }

}