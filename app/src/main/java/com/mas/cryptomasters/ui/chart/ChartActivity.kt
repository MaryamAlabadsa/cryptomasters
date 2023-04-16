package com.mas.cryptomasters.ui.chart
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
//import com.github.mikephil.charting.charts.LineChart
import com.mas.cryptomasters.R

class ChartActivity : AppCompatActivity() {

//    private lateinit var chart: LineChart
//    private lateinit var chartViewModel: ChartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)
//        chart = findViewById(R.id.chart)
//        chartViewModel = ViewModelProvider(this).get(ChartViewModel::class.java)
//        chart.data = chartViewModel.getChartData()
//        chart.invalidate()
    }
}
