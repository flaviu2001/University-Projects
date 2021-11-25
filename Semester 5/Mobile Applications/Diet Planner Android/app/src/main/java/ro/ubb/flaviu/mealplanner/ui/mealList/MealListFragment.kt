package ro.ubb.flaviu.mealplanner.ui.mealList

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ro.ubb.flaviu.mealplanner.R
import ro.ubb.flaviu.mealplanner.databinding.MealListFragmentBinding

class MealListFragment : Fragment() {
    private lateinit var binding: MealListFragmentBinding
    private lateinit var viewModel: MealListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.meal_list_fragment, container, false)
        viewModel = ViewModelProvider(this)[MealListViewModel::class.java]
        viewModel.refresh()
        val adapter = MealsAdapter(ClickMealListener {
            viewModel.onCardClicked(it)
        })
        binding.mealList.adapter = adapter
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
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.meal_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.logout -> {
                viewModel.logout()
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
