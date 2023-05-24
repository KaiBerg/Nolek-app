package com.nolek.application.data.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.google.android.material.tabs.TabLayout
import com.nolek.application.R
import com.nolek.application.databinding.FragmentDetailBinding
import com.nolek.application.paneVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    private lateinit var model: AAChartModel
    private val vm: paneVM by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (vm.selected.value == null) {
            view.visibility = View.INVISIBLE
            return
        }


        val args = arguments
        val binding = FragmentDetailBinding.bind(view)
//        binding.label.text = vm.selected.value?.index

        model = AAChartModel()
            .chartType(AAChartType.Area)
            .title("B21")
            .dataLabelsEnabled(false)
            .series(arrayOf(
                AASeriesElement()
                    .name("Data")
                    .data(vm.getArray("B21")))
            )

        binding.aaChartView.aa_drawChartWithChartModel(model)

        binding.toolbar.inflateMenu(R.menu.menu_main)

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val property = when(tab?.text.toString()) {
                    getString(R.string.detail_tab_log_time) -> "LogTime"
                    getString(R.string.detail_tab_step_no) -> "StepNo"
                    getString(R.string.detail_tab_tmp_1) -> "TMP1"
                    getString(R.string.detail_tab_tmp_2) -> "TMP2"
                    getString(R.string.detail_tab_b21) -> "B21"
                    getString(R.string.detail_tab_b22) -> "B22"
                    getString(R.string.detail_tab_b32) -> "B32"
                    getString(R.string.detail_tab_b31) -> "B31"
                    getString(R.string.detail_tab_regulator_fb) -> "RegulatorSP"
                    getString(R.string.detail_tab_regulator_sp) -> "RegulatorFB"
                    else -> { "B22" }
                }

                model.title = tab?.text.toString()
                model.series(
                    arrayOf(
                        AASeriesElement()
                            .name(tab?.text.toString())
                            .data(vm.getArray(property))
                ))
                binding.aaChartView.aa_refreshChartWithChartModel(model)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

       val i = args?.getInt(Index)
        if (i != null && vm.selected.value != null) {
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(2))
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(i))
        }

    }

    companion object {
        const val Index = "PropertyIndex"
    }
}