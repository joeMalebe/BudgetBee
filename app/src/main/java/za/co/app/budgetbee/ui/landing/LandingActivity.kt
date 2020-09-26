package za.co.app.budgetbee.ui.landing

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_landing.*
import kotlinx.android.synthetic.main.layout_month_selector_dialog.view.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.base.AppCompatBaseActivity
import za.co.app.budgetbee.data.model.domain.Month
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.ui.landing.ILandingMvp.View
import za.co.app.budgetbee.ui.transactions_category.TransactionCategorySelectCategoryActivity
import za.co.app.budgetbee.ui.views.MonthDialogAdapter
import za.co.app.budgetbee.ui.views.MonthSwitcher
import java.text.DateFormatSymbols
import java.text.DecimalFormat
import java.util.*
import javax.inject.Inject

class LandingActivity : AppCompatBaseActivity(), View {


    private val compositeDisposable = CompositeDisposable()
    private val DECIMAL_FORMAT_PATTERN = "0.00"
    val TAG = LandingActivity::class.simpleName

    @Inject
    lateinit var presenter: ILandingMvp.Presenter

    private lateinit var transactionsRecyclerView: RecyclerView
    private lateinit var dialog: Dialog
    private lateinit var monthSwitcher: MonthSwitcher
    private lateinit var incomeValueText: TextView
    private lateinit var expenseValueText: TextView
    private lateinit var balanceValueText: TextView

    companion object {
        private val CURRENT_DATE_EXTRA = "CURRENT_DATE_EXTRA"

        fun getStartIntent(context: Context, transactionDate: Long): Intent {
            val intent = Intent(context, LandingActivity::class.java)
            intent.putExtra(CURRENT_DATE_EXTRA, transactionDate)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        presenter.attachView(this)
        initialiseViews()
    }

    private fun initialiseViews() {
        monthSwitcher = month_switcher
        incomeValueText = income_value_text
        expenseValueText = expense_value_text
        balanceValueText = balance_value_text
        transactionsRecyclerView = recycler_transactions
    }

    override fun onResume() {
        super.onResume()
        displayScreen()
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
        dismissLoading()
        dialog.hide()
        val adapter = TransactionsAdapter(transactions)
        transactionsRecyclerView.adapter = adapter
        transactionsRecyclerView.layoutManager = LinearLayoutManager(this)
        transactionsRecyclerView.isNestedScrollingEnabled = false
        transactionsRecyclerView.visibility = VISIBLE
    }

    override fun openTransactionCategoryActivity() {
        startActivity(TransactionCategorySelectCategoryActivity.getStartIntent(this))
    }

    override fun displayTotalIncome(income: Double) {
        incomeValueText.text = twoDecimalPointsValue(income)
    }

    override fun displayTotalExpense(expense: Double) {
        expenseValueText.text = twoDecimalPointsValue(expense)
    }

    override fun displayBalance(balance: Double) {
        balanceValueText.text = twoDecimalPointsValue(balance)
    }

    override fun displayNoTransactions() {
        Toast.makeText(this, "No transactins", Toast.LENGTH_SHORT).show()
        dismissLoading()
        dialog.hide()

        incomeValueText.text = twoDecimalPointsValue(0.0)
        expenseValueText.text = twoDecimalPointsValue(0.0)
        balanceValueText.text = twoDecimalPointsValue(0.0)
        transactionsRecyclerView.visibility = GONE
    }

    override fun displayScreen() {
        showLoading()
        val date = Calendar.getInstance()
        val currentDate = intent.getLongExtra(CURRENT_DATE_EXTRA, 0L)

        monthSwitcher.init(
                if (currentDate == 0L) {
                    presenter.getTransactionsByDate(getStartAndEndDate(date))
                    date
                } else {
                    date.timeInMillis = currentDate
                    presenter.getTransactionsByDate(getStartAndEndDate(date))
                    date
                }
        )

        val dialogBuilder = AlertDialog.Builder(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.layout_month_selector_dialog, null)

        val firstSixMonthsRecycler = dialogView.first_six_months_recycler
        val firstSixMonthsAdapter = getfirstSixMonthsRecyclerAdapter(date)

        val lastSixMonthsRecycler = dialogView.last_six_months_recycler
        val lastSixMonthsAdapter = getLastSixMonthsRecyclerAdapter(date)

        val yearSwitcher = dialogView.year_switcher
        yearSwitcher.init(date)
        yearSwitcher.getSelectedYear().subscribe { selectedYear ->
            date.set(Calendar.YEAR, selectedYear)
            firstSixMonthsAdapter.updateYear(selectedYear)
            firstSixMonthsAdapter.notifyDataSetChanged()
            lastSixMonthsAdapter.updateYear(selectedYear)
            lastSixMonthsAdapter.notifyDataSetChanged()
        }.let { compositeDisposable.add(it) }

        firstSixMonthsRecycler.adapter = firstSixMonthsAdapter
        lastSixMonthsRecycler.adapter = lastSixMonthsAdapter
        dialogBuilder.setView(dialogView)

        dialog = dialogBuilder.create()

        monthSwitcher.onMonthClicked().subscribe {
            dialog.show()
        }.let { compositeDisposable.add(it) }

        getTransactionsInSelectedMonth()

        val addTransactionButton = add_transaction_fab
        addTransactionButton.setOnClickListener {
            openTransactionCategoryActivity()
        }
    }

    private fun getfirstSixMonthsRecyclerAdapter(date: Calendar): MonthDialogAdapter {
        val firstSixMonthsAdapter = MonthDialogAdapter(getMonthsInRange(0..5), date[Calendar.YEAR])
        firstSixMonthsAdapter.getSelectedMonth().subscribe { calendar ->
            updateDate(calendar)
            presenter.getTransactionsByDate(
                    getStartAndEndDate(calendar))
        }.let { compositeDisposable.add(it) }

        return firstSixMonthsAdapter
    }

    private fun getLastSixMonthsRecyclerAdapter(date: Calendar): MonthDialogAdapter {
        val lastSixMonthsAdapter = MonthDialogAdapter(getMonthsInRange(6..11), date[Calendar.YEAR])
        lastSixMonthsAdapter.getSelectedMonth().subscribe { calendar ->
            updateDate(calendar)
            presenter.getTransactionsByDate(
                    getStartAndEndDate(calendar))
        }.let { compositeDisposable.add(it) }

        return lastSixMonthsAdapter
    }

    private fun updateDate(calendar: Calendar) {
        monthSwitcher.updateDate(calendar)
    }

    private fun getMonthsInRange(monthIndexRange: IntRange): ArrayList<Month> {
        val monthNames = DateFormatSymbols().months
        val monthList = arrayListOf<Month>()
        for (index in monthIndexRange step 1) {
            monthList.add(Month(monthNames[index].substring(0..2), index))
        }
        return monthList
    }

    private fun getTransactionsInSelectedMonth() {
        return monthSwitcher.getSelectedDate().subscribe { calendar ->
            presenter.getTransactionsByDate(
                    getStartAndEndDate(calendar))
        }.let { compositeDisposable.add(it) }
    }

    private fun getStartAndEndDate(calendar: Calendar): Pair<Long, Long> {
        val startDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()
        startDate.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1)
        endDate.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.getMaximum(Calendar.DAY_OF_MONTH))
        return Pair(startDate.timeInMillis, endDate.timeInMillis)
    }

    override fun onDestroy() {
        presenter.detachView()
        compositeDisposable.dispose()
        super.onDestroy()
    }

    private fun twoDecimalPointsValue(value: Double): String? {
        val decimalFormat = DecimalFormat(DECIMAL_FORMAT_PATTERN)
        return decimalFormat.format(value)
    }
}