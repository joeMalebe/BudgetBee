package za.co.app.budgetbee.ui.landing

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.CompositeDisposable
import za.co.app.budgetbee.R
import za.co.app.budgetbee.base.AppCompatBaseActivity
import za.co.app.budgetbee.data.model.domain.Month
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.data.model.domain.TransactionCategory
import za.co.app.budgetbee.data.model.domain.TransactionCategoryType
import za.co.app.budgetbee.databinding.ActivityLandingBinding
import za.co.app.budgetbee.databinding.LayoutMonthSelectorDialogBinding
import za.co.app.budgetbee.ui.add_transaction.AddTransactionActivity
import za.co.app.budgetbee.ui.custom_views.MonthDialogAdapter
import za.co.app.budgetbee.ui.custom_views.MonthSwitcher
import za.co.app.budgetbee.ui.custom_views.YearSwitcherDialog
import za.co.app.budgetbee.ui.edit_transaction.EditTransactionActivity
import za.co.app.budgetbee.ui.report.BalanceReportActivity
import za.co.app.budgetbee.ui.report.ReportActivity
import za.co.app.budgetbee.ui.select_transaction_category.SelectTransactionCategoryActivity
import java.text.DateFormatSymbols
import java.text.DecimalFormat
import java.util.Calendar
import javax.inject.Inject

class LandingActivity : AppCompatBaseActivity(), ILandingMvp.View {

    private val currentDate = Calendar.getInstance()
    private val compositeDisposable = CompositeDisposable()
    private val DECIMAL_FORMAT_PATTERN = "0.00"
    val TAG = LandingActivity::class.simpleName

    @Inject
    lateinit var presenter: ILandingMvp.Presenter
    private lateinit var binding: ActivityLandingBinding
    private lateinit var transactionsRecyclerView: RecyclerView
    private lateinit var dialog: YearSwitcherDialog
    private lateinit var monthSwitcher: MonthSwitcher
    private lateinit var incomeValueText: TextView
    private lateinit var expenseValueText: TextView
    private lateinit var balanceValueText: TextView
    private lateinit var noTransactionsLayout: ConstraintLayout
    private lateinit var transactions: ArrayList<Transaction>

    companion object {
        val CURRENT_DATE_EXTRA = "CURRENT_DATE_EXTRA"

        fun getStartIntent(context: Context, transactionDate: Long): Intent {
            val intent = Intent(context, LandingActivity::class.java)
            intent.putExtra(CURRENT_DATE_EXTRA, transactionDate)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Make sure this is before calling super.onCreate
        setTheme(R.style.BudgetBeeMainTheme)

        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter.attachView(this)
        initialiseViews()
        displayScreen()
    }

    private fun initialiseViews() {

        monthSwitcher =  binding.toolbar.monthSwitcher
        incomeValueText = binding.incomeValueText
        expenseValueText = binding.expenseValueText
        balanceValueText = binding.balanceValueText
        transactionsRecyclerView = binding.recyclerTransactions
        noTransactionsLayout = binding.emptyTransactionsLayout.noTransactionsLayout

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

        this.transactions = transactions
        val adapter = TransactionsListAdapter(this.transactions)
        adapter.getSelectedTransaction().subscribe { transaction ->
            startActivity(EditTransactionActivity.getStartIntent(this, transaction))
        }.let { compositeDisposable.add(it) }
        transactionsRecyclerView.adapter = adapter
        transactionsRecyclerView.layoutManager = LinearLayoutManager(this)
        transactionsRecyclerView.isNestedScrollingEnabled = false
        transactionsRecyclerView.visibility = VISIBLE
        noTransactionsLayout.visibility = GONE
    }

    override fun openTransactionCategoryActivity() {
        startActivityForResult(SelectTransactionCategoryActivity.getStartIntent(this), 2)
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
        dismissLoading()
        dialog.hide()

        incomeValueText.text = twoDecimalPointsValue(0.0)
        expenseValueText.text = twoDecimalPointsValue(0.0)
        balanceValueText.text = twoDecimalPointsValue(0.0)
        transactionsRecyclerView.visibility = GONE
        noTransactionsLayout.visibility = VISIBLE
        transactions = arrayListOf()
    }

    override fun displayScreen() {
        showLoading()

        val newDate = intent.getLongExtra(CURRENT_DATE_EXTRA, 0L)

        monthSwitcher.init(
                if (newDate == 0L) {
                    this.currentDate
                } else {
                    this.currentDate.timeInMillis = newDate
                    this.currentDate
                }
        )

        presenter.getTransactionsByDate(this.currentDate)
        dialog = YearSwitcherDialog(this)
        val dialogView = LayoutMonthSelectorDialogBinding.bind(View.inflate(this, R.layout.layout_month_selector_dialog, null))

        val firstSixMonthsRecycler = dialogView.firstSixMonthsRecycler
        val firstSixMonthsAdapter = getfirstSixMonthsRecyclerAdapter(this.currentDate)

        val lastSixMonthsRecycler = dialogView.lastSixMonthsRecycler
        val lastSixMonthsAdapter = getLastSixMonthsRecyclerAdapter(this.currentDate)

        val yearSwitcher = dialogView.yearSwitcher
        yearSwitcher.init(this.currentDate)
        yearSwitcher.getSelectedYear().subscribe { selectedYear ->
            this.currentDate.set(Calendar.YEAR, selectedYear)
            updateYearSwitcherMonths(firstSixMonthsAdapter, selectedYear, lastSixMonthsAdapter)
        }.let { compositeDisposable.add(it) }

        firstSixMonthsRecycler.adapter = firstSixMonthsAdapter
        lastSixMonthsRecycler.adapter = lastSixMonthsAdapter

        dialog.init(firstSixMonthsAdapter, lastSixMonthsAdapter, dialogView.root, yearSwitcher, monthSwitcher)
        showDialogAtTheTopOfTheScreen()

        monthSwitcher.onMonthClicked().subscribe {
            dialog.show()
        }.let { compositeDisposable.add(it) }

        dialogView.buttonToday.setOnClickListener {
            val today = Calendar.getInstance()
            updateDate(today)
            currentDate.timeInMillis = today.timeInMillis
            presenter.getTransactionsByDate(today)
            yearSwitcher.init(today)
            dialog.hide()
        }
        dialogView.buttonToday.typeface = ResourcesCompat.getFont(this, R.font.lora)

        getTransactionsInSelectedMonth()

        val addTransactionButton = binding.addTransactionFab
        addTransactionButton.setOnClickListener {
            openTransactionCategoryActivity()
        }
    }

    private fun showDialogAtTheTopOfTheScreen() {
        val window = dialog.window
        val windowLayoutParams = window!!.attributes

        windowLayoutParams.gravity = Gravity.TOP
        windowLayoutParams.y = 100
        windowLayoutParams.flags = windowLayoutParams.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        window.attributes = windowLayoutParams
    }

    private fun updateYearSwitcherMonths(firstSixMonthsAdapter: MonthDialogAdapter, selectedYear: Int, lastSixMonthsAdapter: MonthDialogAdapter) {
        firstSixMonthsAdapter.updateYear(selectedYear)
        firstSixMonthsAdapter.notifyDataSetChanged()
        lastSixMonthsAdapter.updateYear(selectedYear)
        lastSixMonthsAdapter.notifyDataSetChanged()
    }

    private fun getfirstSixMonthsRecyclerAdapter(date: Calendar): MonthDialogAdapter {
        val firstSixMonthsAdapter = MonthDialogAdapter(getMonthsInRange(0..5), date[Calendar.YEAR])
        firstSixMonthsAdapter.getSelectedMonth().subscribe { calendar ->
            updateDate(calendar)
            currentDate.timeInMillis = calendar.timeInMillis
            presenter.getTransactionsByDate(calendar)
        }.let { compositeDisposable.add(it) }

        return firstSixMonthsAdapter
    }

    private fun getLastSixMonthsRecyclerAdapter(date: Calendar): MonthDialogAdapter {
        val lastSixMonthsAdapter = MonthDialogAdapter(getMonthsInRange(6..11), date[Calendar.YEAR])
        lastSixMonthsAdapter.getSelectedMonth().subscribe { calendar ->
            updateDate(calendar)
            currentDate.timeInMillis = calendar.timeInMillis
            presenter.getTransactionsByDate(calendar)
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
            currentDate.timeInMillis = calendar.timeInMillis
            presenter.getTransactionsByDate(calendar)
        }.let { compositeDisposable.add(it) }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            val transactionCategory = data.getParcelableExtra<TransactionCategory>(AddTransactionActivity.EXTRA_TRANSACTION_CATEGORY)
            startActivity(AddTransactionActivity.getStartIntent(this, transactionCategory))
        }
    }

    fun navigateToIncomeReports(view: View) {
        startReportActivityByCategory(TransactionCategoryType.INCOME.value)
    }

    fun navigateToExpenseReports(view: View) {
        startReportActivityByCategory(TransactionCategoryType.EXPENSE.value)
    }

    fun navigateToBalanceReports(view: View) {
        startActivity(BalanceReportActivity.getStartIntent(this))
    }

    private fun startReportActivityByCategory(transactionCategory: Int) {
        startActivity(ReportActivity.getStartIntent(this, transactions.filter { it.transactionCategoryType == transactionCategory }, currentDate.timeInMillis, transactionCategory))
    }
}