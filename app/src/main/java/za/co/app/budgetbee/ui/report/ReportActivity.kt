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
import com.github.mikephil.charting.utils.MPPointF
import io.reactivex.disposables.CompositeDisposable
import za.co.app.budgetbee.R
import za.co.app.budgetbee.base.AppCompatBaseActivity
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.databinding.ActivityReportBinding
import za.co.app.budgetbee.ui.custom_views.MonthSwitcher
import za.co.app.budgetbee.ui.landing.ILandingMvp
import za.co.app.budgetbee.ui.landing.LandingActivity
import java.util.Calendar
import javax.inject.Inject


class ReportActivity : AppCompatBaseActivity() , ILandingMvp.View{

    private lateinit var colors: Array<String>
    private lateinit var monthSwitcher: MonthSwitcher
    private val compositeDisposable = CompositeDisposable()
    @Inject
    lateinit var presenter: ReportPresenter
    private lateinit var binding: ActivityReportBinding

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
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.pieChart.setNoDataTextTypeface(ResourcesCompat.getFont(this, R.font.poppins_bold))
        binding.pieChart.setNoDataTextColor(R.color.colorPrimaryDark)

        colors = resources.getStringArray(R.array.pie_chart_colors)
        colors.shuffle(kotlin.random.Random(Calendar.getInstance().timeInMillis))
        presenter.attachView(this)

        val date = Calendar.getInstance()
        monthSwitcher = binding.toolbar.monthSwitcher

        val currentDate = intent.getLongExtra(LandingActivity.CURRENT_DATE_EXTRA, 0L)

        monthSwitcher.init(
                if (currentDate == 0L) {
                    date
                } else {
                    date.timeInMillis = currentDate
                    date
                }
        )

        getTransactionsInSelectedMonth()
        presenter.getTransactionsByDate(date)
    }

    private fun displayTransactionsInRecyclerView(transactions: ArrayList<Transaction>) {
        val recyclerView = binding.recyclerTransactionsList
        recyclerView.visibility = VISIBLE
        binding.transactionsCardView.visibility = VISIBLE

        val adapter = ReportAdapter(getGroupedTransactions(transactions).sortedByDescending { it.transactionAmount }, colors)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun getGroupedTransactions(transactions: ArrayList<Transaction>): ArrayList<Transaction> {
        val groupedTransactions = arrayListOf<Transaction>()
        transactions.groupBy { it.transactionCategoryName }
                .forEach { transactionsByCategoryName ->
                    val transaction = Transaction(0L, transactionsByCategoryName.key, (transactionsByCategoryName.value.sumOf { transaction -> transaction.transactionAmount.toInt() }.toDouble()), 0, transactionsByCategoryName.key, 0)
                    groupedTransactions.add(transaction)
                }
        return groupedTransactions
    }

    private fun displayTransactionsInPieChart(transactions: ArrayList<Transaction>) {
        val pieChart = binding.pieChart
        val data = PieData(getDataSet(transactions))

        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(ContextCompat.getColor(this, R.color.white))
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

        pieChart.setNoDataTextTypeface(ResourcesCompat.getFont(this, R.font.poppins_bold))
        pieChart.setNoDataTextColor(R.color.colorPrimaryDark)
        pieChart.invalidate()
    }

    private fun getDataSet(transactions: ArrayList<Transaction>): IPieDataSet {
        val transactionAmounts = arrayListOf<PieEntry>()
        transactions.sumOf { it.transactionAmount.toInt() }
        val groupedTransactions = transactions.groupBy { it.transactionCategoryName }
        groupedTransactions.forEach { transactionsByCategoryName ->
            val pieEntry = PieEntry((transactionsByCategoryName.value.sumOf { transaction -> transaction.transactionAmount.toInt() }.toFloat()), transactionsByCategoryName.key)
            transactionAmounts.add(pieEntry)
        }
        val dataSet = PieDataSet(transactionAmounts.sortedByDescending { it.y }, "Amount")
        dataSet.colors = colors.map { Color.parseColor(it) }
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
        if (transactionsInSelectedCategory.isEmpty()) {
            displayNoTransactions()
        } else {
            displayTransactionsInPieChart(transactionsInSelectedCategory)
            displayTransactionsInRecyclerView(transactionsInSelectedCategory)
        }
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
        binding.pieChart.clear()

        binding.recyclerTransactionsList.visibility = GONE
        binding.transactionsCardView.visibility = GONE
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