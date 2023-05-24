package com.nolek.application.data.ui.register.nameDetails

import android.os.Bundle
import android.util.Log.d
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nolek.application.R
import com.nolek.application.data.ui.register.RegisterVM
import com.nolek.application.databinding.FragmentRegisterNameDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NameDetailsFragment : Fragment(R.layout.fragment_register_name_details) {

    private val vm: RegisterVM by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentRegisterNameDetailsBinding.bind(view)
        binding.viewModel = vm

        binding.btnNext.setOnClickListener {
            d(
                "NameDetailsFragment",
                "btnNext clicked, with details: firstName:${vm.firstName}, lastName:${vm.lastName}"
            )
            if (vm.firstName.isNotEmpty() && vm.lastName.isNotEmpty()) {
                val action =
                    NameDetailsFragmentDirections.actionNameDetailsFragmentToLoginDetailsFragment(
                        vm.firstName,
                        vm.lastName
                    )
                findNavController().navigate(action)
            } else
                Toast.makeText(requireContext(), "Details incorrect", Toast.LENGTH_LONG).show()
        }

    }

}