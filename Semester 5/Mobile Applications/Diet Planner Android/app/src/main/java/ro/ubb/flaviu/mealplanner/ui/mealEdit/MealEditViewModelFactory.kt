package ro.ubb.flaviu.mealplanner.ui.mealEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class MealEditViewModelFactory (
    private val mealId: String,
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MealEditViewModel::class.java)) {
            return MealEditViewModel(mealId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
