package ro.ubb.flaviu.mealplanner.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ro.ubb.flaviu.mealplanner.data.AuthApi
import ro.ubb.flaviu.mealplanner.data.models.TokenHolder

class LoginViewModel: ViewModel() {
    private val mutableLoginResult = MutableLiveData<String?>()
    val loginResult: LiveData<String?> = mutableLoginResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            mutableLoginResult.value = AuthApi.login(username, password) ?: ""
        }
    }

    fun useToken(token: String?) {
        mutableLoginResult.value = token
    }

    fun onLoginEnded() {
        mutableLoginResult.value = null
    }
}
