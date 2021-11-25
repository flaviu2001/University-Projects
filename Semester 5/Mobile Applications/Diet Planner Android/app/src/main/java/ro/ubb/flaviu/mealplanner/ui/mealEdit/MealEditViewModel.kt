package ro.ubb.flaviu.mealplanner.ui.mealEdit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ro.ubb.flaviu.mealplanner.data.MealApi
import ro.ubb.flaviu.mealplanner.data.models.Meal
import ro.ubb.flaviu.mealplanner.data.models.Result

class MealEditViewModel(private val mealId: String) : ViewModel() {
    fun update(meal: Meal) {
        viewModelScope.launch {
            MealApi.update(meal)
        }
    }

    private val mutableMeal = MutableLiveData<Meal?>()
    val meal: LiveData<Meal?> = mutableMeal

    init {
        viewModelScope.launch {
            val result = MealApi.getMeal(mealId)
            if (result is Result.Success<Meal>)
                mutableMeal.value = result.data
        }
    }
}
