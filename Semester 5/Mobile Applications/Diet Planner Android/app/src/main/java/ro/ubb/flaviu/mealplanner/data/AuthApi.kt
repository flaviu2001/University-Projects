package ro.ubb.flaviu.mealplanner.data

import android.util.Log
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import ro.ubb.flaviu.mealplanner.data.models.TokenHolder
import ro.ubb.flaviu.mealplanner.data.models.User

object AuthApi {
    private interface AuthService {
        @Headers("Content-Type: application/json")
        @POST("/api/auth/login")
        suspend fun login(@Body user: User): TokenHolder
    }

    private val authService: AuthService = Api.retrofit.create(AuthService::class.java)

    private suspend fun retrofitLogin(user: User): String? {
        return try {
            authService.login(user).token
        } catch (e: Exception) {
            Log.i("meals", e.toString())
            null
        }
    }

    fun updateToken(token: String?) {
        Api.token = token
    }

    fun logout() {
        Api.token = null
    }

    suspend fun login(username: String, password: String): String? {
        val user = User(username, password)
        val result = retrofitLogin(user)
        if (result != null)
            Api.token = result
        return result
    }
}
