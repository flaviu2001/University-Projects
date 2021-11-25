package ro.ubb.flaviu.mealplanner.data

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import ro.ubb.flaviu.mealplanner.data.models.Result
import ro.ubb.flaviu.mealplanner.data.models.TokenHolder
import ro.ubb.flaviu.mealplanner.data.models.User

object AuthApi {
    private interface AuthService {
        @Headers("Content-Type: application/json")
        @POST("/api/auth/login")
        suspend fun login(@Body user: User): TokenHolder
    }

    private val authService: AuthService = Api.retrofit.create(AuthService::class.java)

    private suspend fun retrofitLogin(user: User): Result<TokenHolder> {
        return try {
            Result.Success(authService.login(user))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    private var user: User? = null

    fun logout() {
        user = null
        Api.token = null
    }

    suspend fun login(username: String, password: String): Result<TokenHolder> {
        val user = User(username, password)
        val result = retrofitLogin(user)
        if (result is Result.Success<TokenHolder>) {
            AuthApi.user = user
            Api.token = result.data.token
        }
        return result
    }
}
