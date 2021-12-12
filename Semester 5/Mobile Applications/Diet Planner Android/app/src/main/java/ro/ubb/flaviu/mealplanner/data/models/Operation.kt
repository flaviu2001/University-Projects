package ro.ubb.flaviu.mealplanner.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "operations")
data class Operation(
    @PrimaryKey(autoGenerate = true)
    var opId: Long = 0L,
    var opType: Int, // 0 - add, 1 - update
    val name: String,
    val calories: Int,
    val dateAdded: Long,
    val vegetarian: Boolean,
    val _id: String,
)
