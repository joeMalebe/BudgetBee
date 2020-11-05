package za.co.app.budgetbee.ui.edit_transaction

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_add_transaction.input_amount
import kotlinx.android.synthetic.main.activity_add_transaction.input_date
import kotlinx.android.synthetic.main.activity_edit_transaction.*
import kotlinx.android.synthetic.main.transactions_activity_toolbar.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.base.AppCompatBaseActivity
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.data.model.domain.TransactionCategory
import za.co.app.budgetbee.ui.add_transaction.AddTransactionActivity
import za.co.app.budgetbee.ui.landing.LandingActivity
import za.co.app.budgetbee.ui.select_transaction_category.SelectTransactionCategoryActivity
import za.co.app.budgetbee.utils.displayLongDouble
import za.co.app.budgetbee.utils.getDateStringByFormat
import za.co.app.budgetbee.utils.showDatePickerDialogAndDisplaySelectedDateTextToView
import java.util.*
import javax.inject.Inject

class EditTransactionActivity : AppCompatBaseActivity(), IEditTransactionMvp.View {

    private lateinit var transaction: Transaction
    private lateinit var transactionCategory: TransactionCategory
    private lateinit var inputDate: TextInputEditText
    private lateinit var inputAmount: TextInputEditText
    private lateinit var inputDescription: TextInputEditText
    private lateinit var inputTransactionCategory: TextInputEditText
    private lateinit var deleteButton: AppCompatImageView
    private lateinit var backButton: AppCompatImageView

    @Inject
    lateinit var presenter: IEditTransactionMvp.Presenter
    val TAG = this.javaClass.canonicalName

    companion object {
        const val EXTRA_TRANSACTION = "EXTRA_TRANSACTION"

        fun getStartIntent(context: Context, transaction: Transaction): Intent {
            val intent = Intent(context, EditTransactionActivity::class.java)
            intent.putExtra(EXTRA_TRANSACTION, transaction)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_transaction)
        presenter.attachView(this)
        screen_title.text = getString(R.string.transaction)
        inputAmount = input_amount
        inputDate = input_date
        inputDescription = input_description
        inputTransactionCategory = input_transaction_category
        backButton = back_button
        backButton.setOnClickListener { onBackPressed() }
        deleteButton = delete_button
        transaction = intent.getParcelableExtra<Transaction>(EXTRA_TRANSACTION)
        transactionCategory = TransactionCategory(transaction.transactionCategoryId, transaction.transactionCategoryName, transaction.transactionCategoryType)

        displayScreen()
    }

    override fun updateSuccessful() {
        navigateToLanding(transaction.transactionDate)
    }

    override fun updateError(error: Throwable?) {
        Log.e(TAG, "updateError: ${error?.stackTrace}", error)
    }

    override fun deleteSuccessful(transactionDate: Long) {
        navigateToLanding(transactionDate)
    }

    fun navigateToLanding(transactionDate: Long) {
        startActivity(LandingActivity.getStartIntent(this, transactionDate))
        finish()
    }

    override fun showLoading() {
        Log.i(TAG, "showLoading: ")
    }

    override fun dismissLoading() {
        Log.i(TAG, "dismissLoading: ")
    }

    override fun displayScreen() {
        inputAmount.setText(transaction.transactionAmount.toString())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = transaction.transactionDate
        inputDate.setText(calendar.getDateStringByFormat("dd MMMM yyyy"))
        inputAmount.setText(transaction.transactionAmount.displayLongDouble())
        inputDescription.setText(transaction.transactionDescription)
        inputTransactionCategory.setText(transaction.transactionCategoryName)
        inputDate.setText(calendar.getDateStringByFormat())

        inputTransactionCategory.setOnClickListener {
            startActivityForResult(SelectTransactionCategoryActivity.getStartIntent(it.context),2)
        }

        inputDate.setOnClickListener {
            calendar.showDatePickerDialogAndDisplaySelectedDateTextToView(this, inputDate)
        }

        deleteButton.setOnClickListener { presenter.deleteTransaction(transaction) }
        button_edit_transaction.setOnClickListener {
            bindTransactionValues(transaction, calendar)
            presenter.updateTransaction(transaction)

        }
    }

    private fun bindTransactionValues(transaction: Transaction, calendar: Calendar) {
        transaction.transactionDescription = inputDescription.text.toString()
        transaction.transactionDate = calendar.timeInMillis
        transaction.transactionAmount = inputAmount.text.toString().toDouble()
        transaction.transactionCategoryType = transactionCategory.transactionCategoryType
        transaction.transactionCategoryId = transactionCategory.transactionCategoryId
        transaction.transactionCategoryName = transactionCategory.transactionCategoryName
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            transactionCategory = data.getParcelableExtra<TransactionCategory>(AddTransactionActivity.EXTRA_TRANSACTION_CATEGORY)
            inputTransactionCategory.setText(transactionCategory.transactionCategoryName)
        }
    }
}