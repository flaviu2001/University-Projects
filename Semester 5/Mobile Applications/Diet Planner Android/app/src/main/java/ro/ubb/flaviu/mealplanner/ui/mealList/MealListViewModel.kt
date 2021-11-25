package ro.ubb.flaviu.mealplanner.ui.mealList

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ro.ubb.flaviu.mealplanner.data.AuthApi
import ro.ubb.flaviu.mealplanner.data.MealApi
import ro.ubb.flaviu.mealplanner.data.models.Meal
import ro.ubb.flaviu.mealplanner.data.models.Result

class MealListViewModel : ViewModel() {
    private val mutableMeals = MutableLiveData<List<Meal>?>()
    val meals: LiveData<List<Meal>?> = mutableMeals
    private var mutableNavigateToEditMeal = MutableLiveData<String?>()
    var navigateToEditMeal: LiveData<String?> = mutableNavigateToEditMeal

    fun refresh() {
        viewModelScope.launch {
            val result = MealApi.getMeals()
            if (result is Result.Success<List<Meal>>)
                mutableMeals.value = result.data
            else
                mutableMeals.value = null
        }
    }

    fun onCardClicked(_id: String) {
        mutableNavigateToEditMeal.value = _id
    }

    fun onEditCardNavigated() {
        mutableNavigateToEditMeal.value = null
    }

    fun logout() {
        viewModelScope.launch {
            AuthApi.logout()
        }
    }
}