package com.example.royal_decor.Fragments.Graphs

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.royal_decor.DatabaseFunctionality.DatabaseHelper
import com.example.royal_decor.Interface.PiechartCallback
import com.example.royal_decor.R
import com.github.mikephil.charting.charts.PieChart
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
    lateinit var piechartprogress: ProgressBar


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStart() {
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
        piechartprogress.visibility = View.VISIBLE
        dbHelper.fetchDataforPieChart(piechartprogress, object : PiechartCallback {
            override fun returnPieChartValues(list: ArrayList<HashMap<String, Int>>) {
                newsetuppiechart(list)
            }
        })
        return v
    }

    private fun init() {
        pieChart = v.findViewById(R.id.pieChart)
        pieChart.setUsePercentValues(false)
        piechartprogress = v.findViewById(R.id.piechartprogress)
        dbHelper = DatabaseHelper()
        dbHelper.open()


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

        val dataSet = PieDataSet(NoOfEmp, "")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0F, 20F)
        dataSet.selectionShift = 2f
        dataSet.setColors(*ColorTemplate.JOYFUL_COLORS)

        val data = PieData(dataSet)
        data.setValueTextSize(15f)
        data.setValueTextColor(Color.BLACK)
        data.setValueFormatter(DefaultValueFormatter(0))
        data.setValueTypeface(
            ResourcesCompat.getFont(
                activity!!.applicationContext,
                R.font.ralewaysemibold
            )
        )
        pieChart.data = data
        pieChart.isDrawHoleEnabled = true
        pieChart.transparentCircleRadius = 50f
        pieChart.holeRadius = 40f
        pieChart.description.text = "Product Sales"
        pieChart.description.textSize = 16f


        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.setEntryLabelTextSize(10f)
        pieChart.setEntryLabelTypeface(ResourcesCompat.getFont(context!!, R.font.ralewaysemibold))
        pieChart.description.isEnabled = true
        pieChart.legend.isEnabled = false
        pieChart.highlightValues(null)
        pieChart.invalidate()
        pieChart.animateXY(900, 900)

    }

}
