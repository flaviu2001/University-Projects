package ro.ubb.flaviu.mealplanner.ui.mealEdit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ro.ubb.flaviu.mealplanner.R
import ro.ubb.flaviu.mealplanner.data.MealDatabase
import ro.ubb.flaviu.mealplanner.data.models.Meal
import ro.ubb.flaviu.mealplanner.databinding.MealEditFragmentBinding
import java.util.*

class MealEditFragment : Fragment() {
    private lateinit var binding: MealEditFragmentBinding
    private lateinit var viewModel: MealEditViewModel
    private var dateAdded: Date? = null
    private var mealId: String? = null

    private fun saveOrUpdate() {
        val id = if (mealId != null) mealId else ""
        val meal = Meal(binding.nameEdit.text.toString(),
            binding.caloriesEdit.text.toString().toInt(),
            dateAdded!!,
            binding.vegetarian.isChecked,
            id!!
        )
        if (mealId != null)
            viewModel.update(meal)
        else viewModel.save(meal)
        findNavController().navigateUp()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.meal_edit_fragment, container, false)
        val arguments = MealEditFragmentArgs.fromBundle(requireArguments())
        mealId = arguments.mealId
        val database = MealDatabase.getInstance(requireContext()).databaseDao
        val mealEditViewModelFactory = MealEditViewModelFactory(mealId, database)
        viewModel = ViewModelProvider(this, mealEditViewModelFactory)[MealEditViewModel::class.java]
        viewModel.meal.observe(viewLifecycleOwner) {
            it?.let {
                binding.nameEdit.setText(it.name)
                binding.caloriesEdit.setText(it.calories.toString())
                binding.vegetarian.isChecked = it.vegetarian
                dateAdded = it.dateAdded
                binding.dateAddedEdit.setText(it.dateAdded.toString())
            }
        }
        binding.dateAddedEdit.setOnClickListener{
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    val timePickerDialog = TimePickerDialog(requireContext(), { _, hour, minute ->
                        val dateAdded = Calendar.getInstance()
                        dateAdded.set(year, month, day, hour, minute, 0)
                        this.dateAdded = Date(dateAdded.timeInMillis)
                        binding.dateAddedEdit.setText(this.dateAdded.toString())
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
                    timePickerDialog.updateTime(0, 0)
                    timePickerDialog.show()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.meal_edit_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.save -> {
                saveOrUpdate()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}