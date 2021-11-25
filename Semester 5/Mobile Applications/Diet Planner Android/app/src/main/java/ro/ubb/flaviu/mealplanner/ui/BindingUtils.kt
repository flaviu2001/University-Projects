package ro.ubb.flaviu.mealplanner.ui

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import ro.ubb.flaviu.mealplanner.data.models.Meal

@SuppressLint("SetTextI18n")
@BindingAdapter("mealName")
fun TextView.setMealName(meal: Meal?) {
    meal?.let {
        text = "Name: ${meal.name}"
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("mealCalories")
fun TextView.setMealCalories(meal: Meal?) {
    meal?.let {
        text = "${meal.calories} kcal"
    }
}

@BindingAdapter("mealVegetarian")
fun TextView.setMealVegetarian(meal: Meal?) {
    meal?.let {
        text = if (meal.vegetarian)
            "Vegetarian"
        else
            "Not vegetarian"
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("mealDate")
fun TextView.setMealDate(meal: Meal?) {
    meal?.let {
        text = "Date Added: ${meal.dateAdded}"
    }
}