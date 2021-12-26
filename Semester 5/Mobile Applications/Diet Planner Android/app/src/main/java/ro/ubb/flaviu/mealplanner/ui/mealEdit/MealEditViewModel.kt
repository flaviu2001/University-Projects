package ro.ubb.flaviu.mealplanner.ui.mealEdit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import ro.ubb.flaviu.mealplanner.data.MealApi
import ro.ubb.flaviu.mealplanner.data.MealDatabaseDao
import ro.ubb.flaviu.mealplanner.data.models.Meal
import ro.ubb.flaviu.mealplanner.data.models.Operation

class MealEditViewModel(private val mealId: String?, private val database: MealDatabaseDao) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun update(meal: Meal) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                if (!MealApi.update(meal)) {
                    database.insertOperation(Operation(
                        opType = 1,
                        name = meal.name,
                        calories = meal.calories,
                        dateAdded = meal.dateAdded.time,
                        vegetarian = meal.vegetarian,
                        _id = meal._id
                    ))
                }
            }
        }
    }

    fun save(meal: Meal) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                if (!MealApi.save(meal)) {
                    database.insertOperation(
                        Operation(
                            opType = 0,
                            name = meal.name,
                            calories = meal.calories,
                            dateAdded = meal.dateAdded.time,
                            vegetarian = meal.vegetarian,
                            _id = meal._id
                        )
                    )
                }
            }
        }
    }

    private val mutableMeal = MutableLiveData<Meal?>()
    val meal: LiveData<Meal?> = mutableMeal

    init {
        if (mealId != null) {
            viewModelScope.launch {
                val result = MealApi.getMeal(mealId)
                if (result != null)
                    mutableMeal.value = result
            }
        }
    }
}
