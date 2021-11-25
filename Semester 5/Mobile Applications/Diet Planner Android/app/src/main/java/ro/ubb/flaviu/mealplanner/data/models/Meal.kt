package ro.ubb.flaviu.mealplanner.data.models

import java.util.*

data class Meal(
    val name: String,
    val calories: Int,
    val dateAdded: Date,
    val vegetarian: Boolean,
    val _id: String
)
