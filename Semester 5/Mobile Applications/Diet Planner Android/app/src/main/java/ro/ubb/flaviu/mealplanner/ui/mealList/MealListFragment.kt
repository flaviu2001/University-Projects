package ro.ubb.flaviu.mealplanner.ui.mealList

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.*
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import ro.ubb.flaviu.mealplanner.PendingOperationsWorker
import ro.ubb.flaviu.mealplanner.R
import ro.ubb.flaviu.mealplanner.data.ConnectivityLiveData
import ro.ubb.flaviu.mealplanner.databinding.MealListFragmentBinding
import ro.ubb.flaviu.mealplanner.removeToken

class MealListFragment : Fragment() {
    private lateinit var binding: MealListFragmentBinding
    private lateinit var viewModel: MealListViewModel
    private lateinit var sharedPref: SharedPreferences
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var connectivityLiveData: ConnectivityLiveData
    private var length = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.meal_list_fragment, container, false)
        viewModel = ViewModelProvider(this)[MealListViewModel::class.java]
        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        viewModel.refresh()
        val adapter = MealsAdapter(ClickMealListener {
            viewModel.onCardClicked(it)
        })
        binding.mealList.adapter = adapter
        binding.fab.setOnClickListener {
            findNavController().navigate(MealListFragmentDirections.actionMealListFragmentToMealEditFragment(null))
        }
        viewModel.meals.observe(viewLifecycleOwner){
            it?.let{
                adapter.meals = it
            }
        }
        viewModel.navigateToEditMeal.observe(viewLifecycleOwner) {
            it?.let {
                this.findNavController().navigate(MealListFragmentDirections.actionMealListFragmentToMealEditFragment(it))
                viewModel.onEditCardNavigated()
            }
        }
        connectivityManager = ContextCompat.getSystemService(
            requireContext(),
            ConnectivityManager::class.java
        )!!
        connectivityLiveData = ConnectivityLiveData(connectivityManager)
        connectivityLiveData.observe(viewLifecycleOwner, {
            if (it == true) {
                binding.connectivityText.text = "Connected"
                viewModel.refresh()
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
                val myWork = OneTimeWorkRequest.Builder(PendingOperationsWorker::class.java)
                    .setConstraints(constraints)
                    .build()
                WorkManager.getInstance(requireContext()).apply {
                    enqueue(myWork)
                }
            } else
                binding.connectivityText.text = "Not Connected"
        })
        length = Resources.getSystem().displayMetrics.widthPixels - 228 - binding.connectivityText.marginStart*2
        leftToRight()
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun leftToRight() {
        binding.connectivityText.apply {
            animate()
                .translationX(length.toFloat())
                .setDuration(3000)
                .setInterpolator(LinearInterpolator())
                .withEndAction{
                    rightToLeft()
                }
        }
    }

    private fun rightToLeft() {
        binding.connectivityText.apply {
            animate()
                .translationX(0f)
                .setDuration(3000)
                .setInterpolator(LinearInterpolator())
                .withEndAction{
                    leftToRight()
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.meal_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.logout -> {
                removeToken(sharedPref)
                viewModel.logout()
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
