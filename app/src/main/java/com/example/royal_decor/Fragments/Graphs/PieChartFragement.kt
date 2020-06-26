package com.example.royal_decor.Fragments.Graphs

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.royal_decor.DatabaseFunctionality.DatabaseHelper
import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF


class PieChartFragement : Fragment(), OnChartValueSelectedListener,
    AdapterView.OnItemSelectedListener {


    lateinit var v: View
    lateinit var pieChart: PieChart
    lateinit var dbHelper: DatabaseHelper


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStart() {
        newsetuppiechart(Constants.PIECHART_PROD_DATA)
        super.onStart()

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_pie_chart_fragement, container, false)

        init()
        newsetuppiechart(Constants.PIECHART_PROD_DATA)
        dbHelper.fetchDataforPieChart(pieChart)
        return v
    }

    private fun init() {
        pieChart = v.findViewById(R.id.pieChart)
        pieChart.setUsePercentValues(false)
        dbHelper = DatabaseHelper()
        dbHelper.open()
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

    @RequiresApi(Build.VERSION_CODES.N)
    private fun <T, V> Map<T, V>.merge(another: Map<T, V>, mergeFunction: (V, V) -> V): Map<T, V> =
        toMutableMap()
            .apply {
                another.forEach { (key, value) ->
                    merge(key, value, mergeFunction)
                }
            }


    @RequiresApi(Build.VERSION_CODES.N)
    fun newsetuppiechart(map: ArrayList<HashMap<String, Int>>) {
        val NoOfEmp = ArrayList<PieEntry>()

        val result: Map<String, Int> = map
            .fold(mapOf()) { accMap, map ->
                accMap.merge(map, Int::plus)
            }

        for (element in result) {
            NoOfEmp.add(PieEntry((element.value).toFloat(), element.key))
        }

        val dataSet = PieDataSet(NoOfEmp, "Product Wise Sale")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 1f
        dataSet.iconsOffset = MPPointF(0F, 20F)
        dataSet.selectionShift = 5f
        dataSet.setColors(*ColorTemplate.PASTEL_COLORS)

        val data = PieData(dataSet)
        data.setValueTextSize(17f)
        data.setValueTextColor(Color.BLACK)
        data.setValueFormatter(DefaultValueFormatter(0))
        data.setValueTypeface(
            ResourcesCompat.getFont(
                activity!!.applicationContext,
                R.font.ralewaysemibold
            )
        )
        pieChart.isDrawHoleEnabled = false

        pieChart.data = data
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.setEntryLabelTextSize(10f)
        pieChart.setEntryLabelTypeface(ResourcesCompat.getFont(context!!, R.font.ralewaysemibold))
        pieChart.description.isEnabled = false
        pieChart.legend.isEnabled = false
        pieChart.highlightValues(null)
        pieChart.invalidate()
        pieChart.animateXY(900, 900)

    }

}
