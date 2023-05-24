package com.nolek.application.data.ui.list

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nolek.application.R
import com.nolek.application.data.ui.AuthVM
import com.nolek.application.data.ui.login.LoginVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountDialogFragment : DialogFragment(R.layout.dialog_account) {
    private val loginVM : LoginVM by activityViewModels()
    private val authVM : AuthVM by viewModels()

    private lateinit var name : TextView
    private lateinit var email : TextView
    private lateinit var profileLetter : TextView
    private lateinit var signout  : LinearLayout
    private lateinit var exit  : ImageView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val inflater = requireActivity().layoutInflater
            val dialogView = inflater.inflate(R.layout.dialog_account, null)
            authVM.getInfo()
            findViews(dialogView)
            val builder = MaterialAlertDialogBuilder(requireContext(), R.style.Theme_DialogTheme)
            builder.setView(dialogView)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setup()
    }

    private fun findViews(view: View) {
        name = view.findViewById(R.id.tv_profile_name)
        email = view.findViewById(R.id.tv_profile_email)
        profileLetter = view.findViewById(R.id.tv_profile)
        signout = view.findViewById(R.id.layout_sign_out)
        exit = view.findViewById(R.id.image_exit)
    }

    private fun setup() {
        name.text = authVM.name
        email.text = authVM.email
        profileLetter.text = authVM.name

        loginVM.loggedIn.observe(viewLifecycleOwner) {
            if (!it) {
                val controller = requireActivity().findNavController(R.id.nav_host_fragment)
                controller.navigate(R.id.action_global_to_loginFragment)

            }
        }

        signout.setOnClickListener {
            loginVM.logOut()
            dialog?.dismiss()
        }

        exit.setOnClickListener {
            dialog?.cancel()
        }
    }
}