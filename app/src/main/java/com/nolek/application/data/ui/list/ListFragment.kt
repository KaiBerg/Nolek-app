package com.nolek.application.data.ui.list

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
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

    private val paneVM: paneVM by activityViewModels()
    private val plcVM: plcVM by activityViewModels()
    private val authVM: AuthVM by viewModels()

    private lateinit var adapter: Adapter
    private lateinit var suggestionAdapter: SuggestionAdapter

    private lateinit var binding: FragmentListBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListBinding.bind(view)

        adapter = Adapter(emptyList(), layoutInflater) {
            paneVM.sel(it)
        }

        suggestionAdapter = SuggestionAdapter("", emptyList())
        suggestionAdapter.onItemClick = {
            plcVM.search(it)
        }

        plcVM.search.observe(viewLifecycleOwner) {
            binding.searchBar.text = it
            binding.searchView.hide()
            configureSearchBar(it)
        }

        binding.list.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        plcVM.get()
        authVM.getInfo()

        setMenu(searchBar = binding.searchBar)

        plcVM.data.observe(viewLifecycleOwner)
        {
            Log.d("ListFragment", "UI recieved PLC data ${it}")
            adapter.items = it
            adapter.notifyDataSetChanged()
        }


        binding.list.adapter = adapter

        binding.searchView.editText.setOnEditorActionListener { tv, _, _ ->
            plcVM.search(tv.text.toString())
            binding.searchView.hide()
            false
        }

        plcVM.suggestions.observe(viewLifecycleOwner) {
            suggestionAdapter.suggestions = it
            suggestionAdapter.notifyDataSetChanged()
        }

        binding.svSuggestions.adapter = suggestionAdapter
        binding.svSuggestions.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.searchView.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                suggestionAdapter.currentSearch = p0.toString()
                suggestionAdapter.filter.filter(p0.toString())
            }
        })

    }

    private fun configureSearchBar(searchText: String) {
        if (searchText.isEmpty()) {
            binding.searchBar.navigationIcon =
                ResourcesCompat.getDrawable(resources, R.drawable.baseline_search_24, null)
            binding.searchBar.setNavigationOnClickListener {}
        } else {
            binding.searchBar.navigationIcon =
                ResourcesCompat.getDrawable(resources, R.drawable.baseline_arrow_back_24, null)
            binding.searchBar.setNavigationOnClickListener {
                plcVM.search("")
            }
        }

    }

    fun setMenu(searchBar: SearchBar) {
        val av = searchBar.menu.findItem(R.id.profile).actionView
        if (av != null) {
            // as this is a custom menu item not icon, it doesnt get pickeup by menuitemclicklistener
            av.findViewById<TextView>(R.id.tv_profile)?.setText(authVM.name[0].toString())
            av.setOnClickListener {
                val dialog = AccountDialogFragment()
                dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_DialogTheme)
                dialog.show(childFragmentManager, "")
            }
        }
    }

}