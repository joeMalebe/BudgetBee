package za.co.app.budgetbee.ui.landing

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_landing.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.base.AppCompatBaseActivity
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.ui.landing.ILandingMvp.View
import za.co.app.budgetbee.ui.transactions_category.TransactionCategorySelectCategoryActivity
import java.text.DecimalFormat
import javax.inject.Inject

class LandingActivity : AppCompatBaseActivity(), View {

    private val DECIMAL_FORMAT_PATTERN = "0.00"
    val TAG = LandingActivity::class.simpleName

    @Inject
    lateinit var presenter: ILandingMvp.Presenter

    companion object {
        fun getStartIntent(context: Context): Intent {
            val intent = Intent(context, LandingActivity::class.java)
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun displayScreen() {
        showLoading()
        presenter.getTransactions()
        val addTransactionButton = add_transaction_fab
        addTransactionButton.setOnClickListener {
            openTransactionCategoryActivity()
        }
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    fun twoDecimalPointsValue(value: Double): String? {
        val decimalFormat = DecimalFormat(DECIMAL_FORMAT_PATTERN)
        return decimalFormat.format(value)
    }
}