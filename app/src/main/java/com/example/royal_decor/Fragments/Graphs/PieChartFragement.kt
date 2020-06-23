package com.example.royal_decor.Fragments.Graphs

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.royal_decor.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF


class PieChartFragement : Fragment(), OnChartValueSelectedListener,
    AdapterView.OnItemSelectedListener {

    lateinit var v: View
    lateinit var pieChart: PieChart
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_pie_chart_fragement, container, false)

        init()
        setuppiechart()
        return v
    }

    private fun setuppiechart() {
        val NoOfEmp = ArrayList<PieEntry>()
        val total: Float
        total = (100).toFloat()

        NoOfEmp.add(PieEntry((23).toFloat(), "APEX"))
        NoOfEmp.add(PieEntry((19).toFloat(), "ULTIMA"))
        NoOfEmp.add(PieEntry((35).toFloat(), "ROYAL PRIMER"))
        NoOfEmp.add(PieEntry((10).toFloat(), "DECLATION"))
        NoOfEmp.add(PieEntry((9).toFloat(), "ASIAN SHINE"))
        NoOfEmp.add(PieEntry((10).toFloat(), "DECO PRIMER"))
        val dataSet = PieDataSet(NoOfEmp, "Product Wise Sale")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 10f
        dataSet.iconsOffset = MPPointF(0F, 20F)
        dataSet.selectionShift = 5f
        dataSet.setColors(*ColorTemplate.COLORFUL_COLORS)

        val data = PieData(dataSet)
        data.setValueTextSize(17f)
        data.setValueTextColor(Color.BLACK)
        data.setValueTypeface(ResourcesCompat.getFont(context!!, R.font.ralewaysemibold))
        pieChart.holeRadius = 10f

        pieChart.data = data
        pieChart.description.isEnabled = false
        pieChart.legend.isEnabled = false
        pieChart.highlightValues(null)
        pieChart.invalidate()
        pieChart.animateXY(900, 900)

    }

    private fun init() {
        pieChart = v.findViewById(R.id.pieChart)
        pieChart.setUsePercentValues(true)
    }

    fun chartDetails(mChart: PieChart, tf: Typeface) {
        mChart.description.isEnabled = true
        mChart.centerText = ""
        mChart.setCenterTextSize(10F)
        mChart.setCenterTextTypeface(tf)
        val l = mChart.legend
        mChart.legend.isWordWrapEnabled = true
        mChart.legend.isEnabled = false
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.formSize = 20F
        l.formToTextSpace = 5f
        l.form = Legend.LegendForm.SQUARE
        l.textSize = 12f
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.isWordWrapEnabled = true
        l.setDrawInside(false)
        mChart.setTouchEnabled(false)
        mChart.setDrawEntryLabels(false)
        mChart.legend.isWordWrapEnabled = true
        mChart.setExtraOffsets(20f, 0f, 20f, 0f)
        mChart.setUsePercentValues(true)
        // mChart.rotationAngle = 0f
        mChart.setUsePercentValues(true)
        mChart.setDrawCenterText(false)
        mChart.description.isEnabled = true
        mChart.isRotationEnabled = false
    }

    override fun onNothingSelected() {
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }
}
