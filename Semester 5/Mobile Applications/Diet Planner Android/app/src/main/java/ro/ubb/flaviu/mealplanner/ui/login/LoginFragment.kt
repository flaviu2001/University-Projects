package ro.ubb.flaviu.mealplanner.ui.login

import android.content.Context
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
import ro.ubb.flaviu.mealplanner.databinding.LoginFragmentBinding
import ro.ubb.flaviu.mealplanner.getToken
import ro.ubb.flaviu.mealplanner.putToken

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
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        viewModel.useToken(getToken(sharedPref))
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
            if (it != null) {
                if (it == "") {
                    Toast.makeText(requireContext(), "Wrong username or password", Toast.LENGTH_SHORT).show()
                    return@observe
                }
                putToken(sharedPref, it)
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMealListFragment())
                viewModel.onLoginEnded()
            }
        }
        return binding.root
    }

}