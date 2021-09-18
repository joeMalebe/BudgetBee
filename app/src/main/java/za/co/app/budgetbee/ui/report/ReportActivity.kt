package za.co.app.budgetbee.ui.report

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_report.*
import kotlinx.android.synthetic.main.home_toolbar.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.base.AppCompatBaseActivity
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.ui.custom_views.MonthSwitcher
import za.co.app.budgetbee.ui.landing.ILandingMvp
import za.co.app.budgetbee.ui.landing.LandingActivity
import java.util.*
import javax.inject.Inject


class ReportActivity : AppCompatBaseActivity() , ILandingMvp.View{

    private lateinit var monthSwitcher: MonthSwitcher
    private val compositeDisposable = CompositeDisposable()
    @Inject
    lateinit var presenter: ReportPresenter

    companion object {
        private val TAG = ReportActivity::class.simpleName
        private const val EXTRA_TRANSACTIONS_CATEGORY = "TRANSACTIONS_CATEGORY"
        private const val EXTRA_TRANSACTIONS = "EXTRA_TRANSACTIONS"

        fun getStartIntent(context: Context, transactions: List<Transaction>, transactionDate: Long?, transactionsCategory: Int): Intent {
            val intent = Intent(context, ReportActivity::class.java)

            intent.putParcelableArrayListExtra(EXTRA_TRANSACTIONS, transactions as ArrayList<out Parcelable>)
            intent.putExtra(LandingActivity.CURRENT_DATE_EXTRA, transactionDate ?: 0L)
            intent.putExtra(EXTRA_TRANSACTIONS_CATEGORY, transactionsCategory)

            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        setSupportActionBar(findViewById(R.id.toolbar))
        presenter.attachView(this)

        val date = Calendar.getInstance()
        monthSwitcher = month_switcher

        val currentDate = intent.getLongExtra(LandingActivity.CURRENT_DATE_EXTRA, 0L)

        monthSwitcher.init(
                if (currentDate == 0L) {
                    date
                } else {
                    date.timeInMillis = currentDate
                    date
                }
        )
        monthSwitcher.init(
                date
        )
        getTransactionsInSelectedMonth()
        val transactions = intent.getParcelableArrayListExtra<Transaction>(EXTRA_TRANSACTIONS)
        if (transactions != null) {
            displayTransactionsInPieChart(transactions)
            displayTransactionsInRecyclerView(transactions)
        }
    }

    private fun displayTransactionsInRecyclerView(transactions: ArrayList<Transaction>) {
        val recyclerView = recycler_transactions_list
        recyclerView.visibility = VISIBLE

        val adapter = ReportAdapter(getGroupedTransactions(transactions), ColorTemplate.VORDIPLOM_COLORS.toList())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun getGroupedTransactions(transactions: ArrayList<Transaction>): ArrayList<Transaction> {
        val groupedTransactions = arrayListOf<Transaction>()
        transactions.groupBy { it.transactionCategoryName }
                .forEach { transactionsByCategoryName ->
                    val transaction = Transaction(0L, transactionsByCategoryName.key, (transactionsByCategoryName.value.sumBy { transaction -> transaction.transactionAmount.toInt() }.toDouble()), 0, transactionsByCategoryName.key, 0)
                    groupedTransactions.add(transaction)
                }
        return groupedTransactions
    }

    private fun displayTransactionsInPieChart(transactions: ArrayList<Transaction>) {
        val pieChart = pie_chart
        val data = PieData(getDataSet(transactions))

        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(ContextCompat.getColor(this, R.color.primary_text))
        data.setValueTypeface(ResourcesCompat.getFont(this, R.font.poppins_regular))
        pieChart.data = data

        val description = Description()
        description.textSize = 30f
        description.text = "Transactions"
        pieChart.description.isEnabled = false

        pieChart.legend.isEnabled = false
        pieChart.animateXY(2000, 2000)

        pieChart.setUsePercentValues(true)

        pieChart.isDrawHoleEnabled = false
        pieChart.setHoleColor(Color.WHITE)

        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)

        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f

        pieChart.setEntryLabelColor(Color.CYAN)
        pieChart.setEntryLabelTypeface(Typeface.SANS_SERIF)
        pieChart.setEntryLabelTextSize(0f)
        pieChart.invalidate()
    }

    private fun getDataSet(transactions: ArrayList<Transaction>): IPieDataSet {
        val transactionAmounts = arrayListOf<PieEntry>()
        transactions.sumBy { it.transactionAmount.toInt() }
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

    override fun showLoading() {
        Log.d(TAG, "showLoading")
    }

    override fun dismissLoading() {
        Log.d(TAG, "dismissLoading")
    }

    override fun showError(error: Throwable) {
        Log.e(TAG, "showError")
    }

    override fun displayTransactions(transactions: ArrayList<Transaction>) {
        val transactionsCategory = intent.getIntExtra(EXTRA_TRANSACTIONS_CATEGORY, 0)
        val transactionsInSelectedCategory = transactions.filter { it.transactionCategoryType == transactionsCategory } as ArrayList<Transaction>
        displayTransactionsInPieChart(transactionsInSelectedCategory)
        displayTransactionsInRecyclerView(transactionsInSelectedCategory)
    }

    override fun openTransactionCategoryActivity() {
        //do nothing
    }

    override fun displayTotalIncome(income: Double) {
        //do nothing
    }

    override fun displayTotalExpense(expense: Double) {
        //do nothing
    }

    override fun displayBalance(balance: Double) {
        //do nothing
    }

    override fun displayNoTransactions() {
        pie_chart.clear()
        recycler_transactions_list.visibility = GONE
    }

    override fun displayScreen() {

    }

    private fun getTransactionsInSelectedMonth() {
        return monthSwitcher.getSelectedDate().subscribe { calendar ->
            presenter.getTransactionsByDate(calendar)
        }.let { compositeDisposable.add(it) }
    }

    override fun onDestroy() {
        presenter.detachView()
        compositeDisposable.dispose()
        super.onDestroy()
    }
}