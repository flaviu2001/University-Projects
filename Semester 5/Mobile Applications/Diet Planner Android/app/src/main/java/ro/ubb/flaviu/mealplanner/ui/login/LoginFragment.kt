package ro.ubb.flaviu.mealplanner.ui.login

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ro.ubb.flaviu.mealplanner.R
import ro.ubb.flaviu.mealplanner.data.models.Result
import ro.ubb.flaviu.mealplanner.databinding.LoginFragmentBinding

class LoginFragment : Fragment() {
    private lateinit var binding: LoginFragmentBinding
    private lateinit var viewModel: LoginViewModel

    private fun login() {
        viewModel.login(binding.usernameEdit.text.toString(), binding.passwordEdit.text.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.loginButton.setOnClickListener{
            login()
        }
        binding.passwordEdit.setOnKeyListener{ _: View, i: Int, keyEvent: KeyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN)
                when(i) {
                    KeyEvent.KEYCODE_ENTER -> login()
                }
            return@setOnKeyListener false
        }
        viewModel.loginResult.observe(viewLifecycleOwner){
            it?.let {
                if (it is Result.Success<*>) {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMealListFragment())
                    viewModel.onLoginEnded()
                } else
                    Toast.makeText(requireContext(), "Wrong username of password", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

}