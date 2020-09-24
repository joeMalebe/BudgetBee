package za.co.app.budgetbee.ui.landing

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_landing.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.base.AppCompatBaseActivity
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.ui.landing.ILandingMvp.View
import za.co.app.budgetbee.ui.transactions_category.TransactionCategorySelectCategoryActivity
import java.text.DecimalFormat
import java.util.*
import javax.inject.Inject

class LandingActivity : AppCompatBaseActivity(), View {

    private val compositeDisposable = CompositeDisposable()
    private val DECIMAL_FORMAT_PATTERN = "0.00"
    val TAG = LandingActivity::class.simpleName

    @Inject
    lateinit var presenter: ILandingMvp.Presenter

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
        val adapter = TransactionsAdapter(transactions)
        recycler_transactions.adapter = adapter
        recycler_transactions.layoutManager = LinearLayoutManager(this)
        recycler_transactions.isNestedScrollingEnabled = false

    }

    override fun openTransactionCategoryActivity() {
        startActivity(TransactionCategorySelectCategoryActivity.getStartIntent(this))
    }

    override fun displayTotalIncome(income: Double) {
        income_value_text.text = twoDecimalPointsValue(income)
    }

    override fun displayTotalExpense(expense: Double) {
        expense_value_text.text = twoDecimalPointsValue(expense)
    }

    override fun displayBalance(balance: Double) {
        balance_value_text.text = twoDecimalPointsValue(balance)
    }

    override fun displayNoTransactions() {
        Toast.makeText(this, "No transactins", Toast.LENGTH_SHORT).show()
    }

    override fun displayScreen() {
        showLoading()

        val currentDate = intent.getLongExtra(CURRENT_DATE_EXTRA, 0L)
        month_switcher.init(
                if (currentDate == 0L) {
                    val date = Calendar.getInstance()
                    presenter.getTransactionsByDate(getStartAndEndDate(date))
                    date
                } else {
                    val date = Calendar.getInstance()
                    date.timeInMillis = currentDate
                    presenter.getTransactionsByDate(getStartAndEndDate(date))
                    date
                }
        )

        val disposable = getTransactionsInSelectedMonth()
        compositeDisposable.add(disposable)

        val addTransactionButton = add_transaction_fab
        addTransactionButton.setOnClickListener {
            openTransactionCategoryActivity()
        }
    }

    private fun getTransactionsInSelectedMonth(): Disposable {
        return month_switcher.getSelectedDate().subscribe { calendar ->
            presenter.getTransactionsByDate(
                    getStartAndEndDate(calendar))
        }
    }

    fun getStartAndEndDate(calendar: Calendar): Pair<Long, Long> {
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

    fun twoDecimalPointsValue(value: Double): String? {
        val decimalFormat = DecimalFormat(DECIMAL_FORMAT_PATTERN)
        return decimalFormat.format(value)
    }
}