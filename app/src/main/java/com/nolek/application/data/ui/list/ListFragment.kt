package com.nolek.application.data.ui.list

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.search.SearchBar
import com.nolek.application.R
import com.nolek.application.data.ui.AuthVM
import com.nolek.application.databinding.FragmentListBinding
import com.nolek.application.paneVM
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list) {

    private val vm: paneVM by activityViewModels()
    private val vm1: plcVM by activityViewModels()
    private val vm2: AuthVM by viewModels()

    private lateinit var adapter: Adapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentListBinding.bind(view)

        adapter = Adapter(emptyList(), layoutInflater) {
            vm.sel(it)
        }

        binding.list.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        vm1.get()
        vm2.getInfo()

        setMenu(searchBar = binding.searchBar)

        vm1.data.observe(viewLifecycleOwner)
        {
            Log.d("ListFragment", "UI recieved PLC data ${it}")
            adapter.items = it
            adapter.notifyDataSetChanged()
        }


        binding.list.adapter = adapter

    }

    fun setMenu(searchBar: SearchBar) {
        val av = searchBar.menu.findItem(R.id.profile).actionView
        if (av != null) {
            // as this is a custom menu item not icon, it doesnt get pickeup by menuitemclicklistener
            av.findViewById<TextView>(R.id.tv_profile)?.setText(vm2.name[0].toString())
            av.setOnClickListener {
                val dialog = AccountDialogFragment()
                dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_DialogTheme)
                dialog.show(childFragmentManager, "")
            }
        }

        searchBar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.profile -> {

                    true
                }
                else -> false
            }
        }
    }

}