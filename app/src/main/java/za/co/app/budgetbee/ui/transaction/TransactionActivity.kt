package za.co.app.budgetbee.ui.transaction

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.widget.AppCompatImageView
import kotlinx.android.synthetic.main.activity_transaction.*
import kotlinx.android.synthetic.main.default_toolbar.back_button
import kotlinx.android.synthetic.main.default_toolbar.screen_title
import kotlinx.android.synthetic.main.transactions_activity_toolbar.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.base.AppCompatBaseActivity
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.data.model.domain.TransactionCategoryType
import za.co.app.budgetbee.ui.edit_transaction.EditTransactionActivity
import za.co.app.budgetbee.ui.landing.LandingActivity
import za.co.app.budgetbee.utils.getDateStringByFormat
import java.util.*
import javax.inject.Inject

class TransactionActivity : AppCompatBaseActivity(), ITransactionMvp.View {
    val TAG = TransactionActivity::class.simpleName

    private lateinit var deleteButton: AppCompatImageView
    private lateinit var editButton: Button
    private lateinit var backButton: AppCompatImageView
    private lateinit var transaction: Transaction

    @Inject
    private lateinit var presenter: ITransactionMvp.Presenter

    companion object {
        private val EXTRA_TRANSACTION = "EXTRA_TRANSACTION"

        fun getStartIntent(context: Context, transaction: Transaction): Intent {
            val intent = Intent(context, TransactionActivity::class.java)
            intent.putExtra(EXTRA_TRANSACTION, transaction)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)
        presenter.attachView(this)
        transaction = intent.getParcelableExtra(EXTRA_TRANSACTION)
        backButton = back_button
        backButton.setOnClickListener { onBackPressed() }
        editButton = edit_button
        deleteButton = delete_button

    }

    override fun onResume() {
        super.onResume()
        displayScreen()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun navigateToLanding(transactionDate: Long) {
        startActivity(LandingActivity.getStartIntent(this, transactionDate))
    }

    override fun showLoading() {
        Log.d(TAG, "showLoading")
    }

    override fun dismissLoading() {
        Log.d(TAG, "dismissLoading")
    }

    override fun displayScreen() {
        screen_title.text = getString(R.string.transaction)
        category_name_text.text = transaction.transactionCategoryName
        category_type_text.text = getCategoryTypeString()
        amount_text.text = transaction.transactionAmount.toString()
        description_text.text = transaction.transactionDescription
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = transaction.transactionDate
        date_text.text = calendar.getDateStringByFormat("dd MMMM yyyy")
        deleteButton.setOnClickListener { presenter.deleteTransaction(transaction) }
        editButton.setOnClickListener { startActivity(EditTransactionActivity.getStartIntent(this, transaction)) }
    }

    private fun getCategoryTypeString(): CharSequence {
        return if (transaction.transactionCategoryType == TransactionCategoryType.INCOME.value)
            this.getString(R.string.income_heading)
        else
            this.getString(R.string.expense)
    }
}