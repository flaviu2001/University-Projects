package ro.ubb.flaviu.mealplanner.data

import retrofit2.http.*
import ro.ubb.flaviu.mealplanner.data.models.Meal
import ro.ubb.flaviu.mealplanner.data.models.Result

object MealApi {
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


    }

    private val mealService: MealService = Api.retrofit.create(MealService::class.java)

    private suspend fun retrofitGetMeals(): Result<List<Meal>> {
        return try {
            Result.Success(mealService.getMeals())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    private suspend fun retrofitGetMeal(mealId: String): Result<Meal> {
        return try {
            Result.Success(mealService.getMeal(mealId))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    private suspend fun retrofitUpdate(meal: Meal): Result<Meal> {
        return try {
            Result.Success(mealService.update(meal._id, meal))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getMeals(): Result<List<Meal>> = retrofitGetMeals()

    suspend fun getMeal(mealId: String): Result<Meal> = retrofitGetMeal(mealId)

    suspend fun update(meal: Meal): Result<Meal> = retrofitUpdate(meal)

}
