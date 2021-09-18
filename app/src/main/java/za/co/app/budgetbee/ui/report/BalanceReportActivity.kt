package za.co.app.budgetbee.ui.report

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.EntryXComparator
import kotlinx.android.synthetic.main.activity_balance_report.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.base.AppCompatBaseActivity
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.data.model.domain.TransactionCategoryType
import java.util.*
import javax.inject.Inject

class BalanceReportActivity : AppCompatBaseActivity(), IBalanceReportMvp.View {

    @Inject
    private lateinit var presenter: IBalanceReportMvp.Presenter
    companion object {

        fun getStartIntent(context: Context): Intent {
            return Intent(context, BalanceReportActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_balance_report)
        presenter.attachView(this)
        displayScreen()
    }

    override fun displayTransactions(transactionsByCategory: Map<Int, List<Transaction>>) {
        if(transactionsByCategory.isEmpty()) {
            line_chart.clear()
        }
        else {
            val incomeEntries = getEntriesByCategoryType(transactionsByCategory.get(TransactionCategoryType.INCOME.value), TransactionCategoryType.INCOME)
            val incomeLineDataSet = LineDataSet(incomeEntries, "Income")
            incomeLineDataSet.color = ContextCompat.getColor(this, R.color.green)
            incomeLineDataSet.axisDependency = YAxis.AxisDependency.LEFT
            Collections.sort(incomeEntries, EntryXComparator())

            val expenseEntries = getEntriesByCategoryType(transactionsByCategory.get(TransactionCategoryType.EXPENSE.value), TransactionCategoryType.EXPENSE)
            val expenseLineDataSet = LineDataSet(expenseEntries, "Expense")
            expenseLineDataSet.color = ContextCompat.getColor(this, R.color.red)
            expenseLineDataSet.axisDependency = YAxis.AxisDependency.LEFT
            Collections.sort(expenseEntries, EntryXComparator())

            val transactionsDataSet = arrayListOf<ILineDataSet>()
            transactionsDataSet.add(incomeLineDataSet)
            transactionsDataSet.add(expenseLineDataSet)

            val lineData = LineData(transactionsDataSet)
            line_chart.data = lineData
            line_chart.invalidate()
        }

    }

    private fun getEntriesByCategoryType(transactions: List<Transaction>?, transactionCategoryType: TransactionCategoryType): ArrayList<Entry> {
        val transactionEntries = arrayListOf<Entry>()
        transactions?.forEach {
                    transactionEntries.add(Entry(it.transactionDate.toFloat(), it.transactionAmount.toFloat()))
                }
        return transactionEntries
    }

    override fun displayScreen() {
        presenter.getTransactionsGroupedByCategoryType()
    }

    override fun showError(error: Throwable) {

    }

    override fun showLoading() {

    }

    override fun dismissLoading() {

    }
}