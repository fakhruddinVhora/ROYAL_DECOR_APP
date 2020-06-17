package com.example.royal_decor.Fragments

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.example.royal_decor.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener


class PieChartFragement : Fragment(), OnChartValueSelectedListener,
    AdapterView.OnItemSelectedListener {

    lateinit var v: View
    lateinit var pieChart: PieChart
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_pie_chart_fragement, container, false)

        pieChart = v.findViewById(R.id.pieChart)

        pieChart.setUsePercentValues(true)
        val xvalues = ArrayList<PieEntry>()
        xvalues.add(PieEntry(34.0f, "London"))
        xvalues.add(PieEntry(28.2f, "Coventry"))
        xvalues.add(PieEntry(37.9f, "Manchester"))
        val dataSet = PieDataSet(xvalues, "")
        val data = PieData(dataSet)
/*
        pieChartColor(data, dataSet)
*/
        // In Percentage
        data.setValueFormatter(PercentFormatter())

        pieChart.data = data
        pieChart.description.text = ""
        pieChart.isDrawHoleEnabled = false
        data.setValueTextSize(13f)


        pieChart.setOnChartValueSelectedListener(this)
        chartDetails(pieChart, Typeface.SANS_SERIF)


        // Inflate the layout for this fragment
        return v
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
