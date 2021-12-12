package ro.ubb.flaviu.mealplanner.data

import androidx.room.*
import ro.ubb.flaviu.mealplanner.data.models.Operation

@Dao
interface MealDatabaseDao {
    @Insert
    suspend fun insertOperation(operation: Operation)

    @Delete
    suspend fun deleteOperation(vararg operation: Operation)

    @Query("SELECT * FROM operations")
    suspend fun getOperations(): List<Operation>
}
