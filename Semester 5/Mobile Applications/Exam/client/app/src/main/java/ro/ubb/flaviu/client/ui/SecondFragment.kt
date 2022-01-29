package ro.ubb.flaviu.client.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ro.ubb.flaviu.client.R
import ro.ubb.flaviu.client.data.AppDatabase
import ro.ubb.flaviu.client.databinding.SecondFragmentBinding

class SecondFragment : Fragment() {
    private lateinit var binding: SecondFragmentBinding
    private lateinit var viewModel: SecondViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SecondFragmentBinding.inflate(inflater, container, false)
        val factory = SecondViewModelFactory(AppDatabase.getInstance(requireContext()).databaseDao)
        viewModel = ViewModelProvider(this, factory)[SecondViewModel::class.java]
        return binding.root
    }
}