package ro.ubb.flaviu.mealplanner

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.*
import ro.ubb.flaviu.mealplanner.data.MealApi
import ro.ubb.flaviu.mealplanner.data.MealDatabase
import ro.ubb.flaviu.mealplanner.data.models.Meal
import ro.ubb.flaviu.mealplanner.data.models.Operation
import java.util.*

class PendingOperationsWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val database = MealDatabase.getInstance(context).databaseDao
        val job = Job()
        val scope = CoroutineScope(Dispatchers.Main + job)
        scope.launch {
            withContext(Dispatchers.IO) {
                val operations = database.getOperations()
                val toDelete = mutableListOf<Operation>()
                for (operation in operations) {
                    val meal = Meal(
                        operation.name,
                        operation.calories,
                        Date(operation.dateAdded),
                        operation.vegetarian,
                        operation._id)
                    val success = if (operation.opType == 0)
                        MealApi.save(meal)
                    else MealApi.update(meal)
                    if (success)
                        toDelete.add(operation)
                }
                database.deleteOperation(*toDelete.toTypedArray())
            }
        }
        return Result.success()
    }
}