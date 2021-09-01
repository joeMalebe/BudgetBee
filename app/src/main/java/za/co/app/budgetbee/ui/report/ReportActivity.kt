package za.co.app.budgetbee.ui.report

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.activity_report.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.base.AppCompatBaseActivity
import za.co.app.budgetbee.data.model.domain.Transaction
import java.util.*


class ReportActivity : AppCompatBaseActivity() {

    companion object {
        private val EXTRA_TRANSACTIONS = "EXTRA_TRANSACTIONS"

        fun getStartIntent(context: Context, transactions: List<Transaction>): Intent {
            val intent = Intent(context, ReportActivity::class.java)
            intent.putParcelableArrayListExtra(EXTRA_TRANSACTIONS, transactions as ArrayList<out Parcelable>)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        setSupportActionBar(findViewById(R.id.toolbar))

        val pieChart = findViewById<View>(R.id.pie_chart) as PieChart
        val transactions = intent.getParcelableArrayListExtra<Transaction>(EXTRA_TRANSACTIONS)
        if (transactions != null) {
            val data = PieData(getDataSet(transactions))

            data.setValueFormatter(PercentFormatter())
            data.setValueTextSize(11f)
            data.setValueTextColor(Color.BLACK)
            data.setValueTypeface(Typeface.SANS_SERIF)
            pieChart.data = data

            val description = Description()
            description.textSize = 30f
            description.text = "Transactions"
            pieChart.description.isEnabled = false

            pieChart.legend.isEnabled = false
            /*l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            l.orientation = Legend.LegendOrientation.VERTICAL
            l.setDrawInside(false)
            l.xEntrySpace = 7f
            l.yEntrySpace = 0f
            l.yOffset = 0f
*/
            pieChart.animateXY(2000, 2000)

            pieChart.setUsePercentValues(true)

            pieChart.isDrawHoleEnabled = false
            pieChart.setHoleColor(Color.WHITE)

            pieChart.setTransparentCircleColor(Color.WHITE);
            pieChart.setTransparentCircleAlpha(110);

            pieChart.holeRadius = 58f;
            pieChart.transparentCircleRadius = 61f;

            pieChart.setEntryLabelColor(Color.CYAN);
            pieChart.setEntryLabelTypeface(Typeface.SANS_SERIF);
            pieChart.setEntryLabelTextSize(0f);
            pieChart.invalidate()

            val recyclerView = recycler_transactions_list

            val groupedTransactions = arrayListOf<Transaction>()
            transactions.groupBy { it.transactionCategoryName }
            .forEach { transactionsByCategoryName ->
                val transaction = Transaction(0L, transactionsByCategoryName.key, (transactionsByCategoryName.value.sumBy { transaction -> transaction.transactionAmount.toInt() }.toDouble()),0, transactionsByCategoryName.key,0)
                groupedTransactions.add(transaction)
            }

            val adapter = ReportAdapter(groupedTransactions, ColorTemplate.VORDIPLOM_COLORS.toList())
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
        }
    }

    private fun getDataSet(transactions: ArrayList<Transaction>): IPieDataSet {
        val transactionAmounts = arrayListOf<PieEntry>()
        val total = transactions.sumBy { it.transactionAmount.toInt() }
        val groupedTransactions = transactions.groupBy { it.transactionCategoryName }
        groupedTransactions.forEach { transactionsByCategoryName ->
            val pieEntry = PieEntry((transactionsByCategoryName.value.sumBy { transaction -> transaction.transactionAmount.toInt() }.toFloat()), transactionsByCategoryName.key)
            transactionAmounts.add(pieEntry)
        }
        val dataSet = PieDataSet(transactionAmounts, "Amount")
        dataSet.colors = ColorTemplate.VORDIPLOM_COLORS.toList()
        dataSet.valueTextSize = 0f
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f
        return dataSet
    }
}