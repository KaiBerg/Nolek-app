package com.nolek.application.data.ui.listDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.nolek.application.R
import com.nolek.application.data.ui.detail.DetailFragment
import com.nolek.application.databinding.FragmentListDetailBinding
import com.nolek.application.paneVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListDetailFragment : Fragment() {

    private val primaryViewModel: paneVM by activityViewModels()

    private var _binding: FragmentListDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var callback: TwoPaneOnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListDetailBinding.inflate(inflater, container, false)

        binding.slidingPaneLayout.lockMode = SlidingPaneLayout.LOCK_MODE_LOCKED

        callback = TwoPaneOnBackPressedCallback(binding.slidingPaneLayout)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        val primaryNavHostFragment =
            childFragmentManager.findFragmentById(R.id.list_container) as NavHostFragment
        var navController = primaryNavHostFragment.navController
        navController.setGraph(
            R.navigation.list_nav
        )

//        val detailNavHostFragment =
//            childFragmentManager.findFragmentById(R.id.detail_container) as NavHostFragment
//        val detnavController = detailNavHostFragment.navController
//        detnavController.setGraph(
//            R.navigation.item_nav
//        )

        val controller =
            (childFragmentManager.findFragmentById(R.id.detail_container) as NavHostFragment).navController

        primaryViewModel.selected.observe(viewLifecycleOwner) {
            controller.navigate(
                R.id.detailFragment, bundleOf(
                    DetailFragment.Index to 0
                )
            )


            binding.root.open()
        }



        return binding.root
    }

    override fun onResume() {
        super.onResume()
        callback.onTabResumed()
    }

    override fun onPause() {
        super.onPause()
        callback.onTabPaused()
    }

}
