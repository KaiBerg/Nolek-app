package com.nolek.application.data.ui.register.loginDetails

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nolek.application.R
import com.nolek.application.data.ui.login.LoginVM
import com.nolek.application.data.ui.register.RegisterVM
import com.nolek.application.databinding.FragmentRegisterLoginDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginDetailsFragment : Fragment(R.layout.fragment_register_login_details) {

    private val vm: RegisterVM by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentRegisterLoginDetailsBinding.bind(view)
        binding.viewModel = vm

        val bundle = arguments
        if (bundle != null) {
            val args = LoginDetailsFragmentArgs.fromBundle(bundle)
            vm.firstName = args.firstName
            vm.lastName = args.lastName
        }

        binding.btnNext.setOnClickListener {
            if (vm.email.isNotEmpty() && vm.password.isNotEmpty() && vm.firstName.isNotEmpty() && vm.lastName.isNotEmpty())
                vm.register()
            else
                Toast.makeText(requireContext(), "Details incorrect", Toast.LENGTH_LONG).show()
        }

        vm.registered.observe(viewLifecycleOwner) { isRegistered ->
            if (isRegistered) {
                val login: LoginVM by activityViewModels()
                login.logIn(vm.email, vm.password)

                login.loggedIn.observe(viewLifecycleOwner) {
                    if (it) {
                        val action =
                            LoginDetailsFragmentDirections.actionLoginDetailsFragmentToMainFragmentNav()
                        findNavController().navigate(action)
                    }
                }
            }

        }

    }

}