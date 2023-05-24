package com.nolek.application.data.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nolek.application.R
import com.nolek.application.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val vm: LoginVM by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentLoginBinding.bind(view)

        binding.tvCreateAcc.setOnClickListener {
            val action =
                LoginFragmentDirections.actionLoginFragmentToNameDetailsFragment()
            findNavController().navigate(action)
        }

        binding.btnSignIn.setOnClickListener {
            val email = binding.tietEmail.text.toString()
            val password = binding.tietPassword.text.toString()

            vm.logIn(email, password)
        }

        vm.loggedIn.observe(viewLifecycleOwner) {
            if (it) {
                val action =
                    LoginFragmentDirections.actionLoginFragmentToMainFragmentNav()
                findNavController().navigate(action)
            }
//            else {
//                Toast.makeText(requireContext(), "Login Incorrect", Toast.LENGTH_LONG).show()
//            }

        }

    }

}