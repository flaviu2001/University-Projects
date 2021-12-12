package ro.ubb.flaviu.mealplanner.data

import android.util.Log
import retrofit2.http.*
import ro.ubb.flaviu.mealplanner.data.models.Meal
import java.util.*

object MealApi {
    private data class MockMeal (val name: String,
                                 val calories: Int,
                                 val dateAdded: Date,
                                 val vegetarian: Boolean) {
        companion object {
            fun fromMeal(meal: Meal): MockMeal {
                return MockMeal(meal.name, meal.calories, meal.dateAdded, meal.vegetarian)
            }
        }
    }

    private interface MealService {
        @Headers("Content-Type: application/json")
        @GET("/api/meal/filter")
        suspend fun getMeals(): List<Meal>

        @Headers("Content-Type: application/json")
        @GET("/api/meal/{id}")
        suspend fun getMeal(@Path("id") mealId: String): Meal

        @Headers("Content-Type: application/json")
        @PUT("/api/meal/{id}")
        suspend fun update(@Path("id") mealId: String, @Body meal: Meal): Meal

        @Headers("Content-Type: application/json")
        @POST("/api/meal/")
        suspend fun save(@Body meal: MockMeal): Meal
    }

    private val mealService: MealService = Api.retrofit.create(MealService::class.java)

    suspend fun getMeals(): List<Meal>? {
        return try {
            mealService.getMeals()
        } catch (e: Exception) {
            Log.i("meals", e.toString())
            null
        }
    }

    suspend fun getMeal(mealId: String): Meal? {
        return try {
            mealService.getMeal(mealId)
        } catch (e: Exception) {
            Log.i("meals", e.toString())
            null
        }
    }

    suspend fun update(meal: Meal): Boolean {
        return try {
            mealService.update(meal._id, meal)
            true
        } catch (e: Exception) {
            Log.i("meals", e.toString())
            false
        }
    }

    suspend fun save(meal: Meal): Boolean {
        return try {
            mealService.save(MockMeal.fromMeal(meal))
            true
        } catch (e: java.lang.Exception) {
            Log.i("meals", e.toString())
            false
        }
    }
}
