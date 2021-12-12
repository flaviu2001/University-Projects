package ro.ubb.flaviu.mealplanner.ui.mealEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ro.ubb.flaviu.mealplanner.data.MealDatabaseDao
import java.lang.IllegalArgumentException

class MealEditViewModelFactory (
    private val mealId: String?,
    private val database: MealDatabaseDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MealEditViewModel::class.java)) {
            return MealEditViewModel(mealId, database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
