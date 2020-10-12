package za.co.app.budgetbee.ui.add_transaction

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_add_transaction.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.base.AppCompatBaseActivity
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.data.model.domain.TransactionCategory
import za.co.app.budgetbee.data.model.domain.TransactionCategoryType
import za.co.app.budgetbee.ui.landing.LandingActivity
import za.co.app.budgetbee.utils.getDateStringByFormat
import java.util.*
import javax.inject.Inject

class AddTransactionActivity : AppCompatBaseActivity(), IAddTransactionMvp.View {

    private lateinit var inputDate: TextInputEditText
    private lateinit var inputAmount: TextInputEditText

    @Inject
    lateinit var presenter: IAddTransactionMvp.Presenter

    companion object {
        const val EXTRA_TRANSACTION_CATEGORY = "EXTRA_TRANSACTION_CATEGORY"
        const val EXTRA_TRANSACTION = "EXTRA_TRANSACTION"

        fun getStartIntent(context: Context?, transactionCategory: TransactionCategory): Intent {
            val intent = Intent(context, AddTransactionActivity::class.java)
            intent.putExtra(EXTRA_TRANSACTION_CATEGORY, transactionCategory)
            return intent
        }

        fun getStartIntent(context: Context?, transactionCategory: TransactionCategory, transaction: Transaction): Intent {
            val intent = Intent(context, AddTransactionActivity::class.java)
            intent.putExtra(EXTRA_TRANSACTION_CATEGORY, transactionCategory)
            intent.putExtra(EXTRA_TRANSACTION, transaction)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)
        presenter.attachView(this)
        inputAmount = input_amount
        inputDate = input_date
        displayScreen()
    }

    private fun getTransactionDescription(transactionCategoryType: Int): String {
        return if (!input_description.text.toString().isEmpty()) {
            input_description.text.toString()
        } else {
            getTransactionCategoryType(transactionCategoryType)
        }
    }

    private fun getTransactionCategoryType(transactionCategoryType: Int): String {
        return when (transactionCategoryType) {
            TransactionCategoryType.INCOME.value -> "Income"
            TransactionCategoryType.EXPENSE.value -> "Expense"
            else -> "Other"
        }
    }

    private fun setupDateDialogue(calendar: Calendar) {
        val year = calendar.get(Calendar.YEAR)
        val monthOfYear = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(
            this,
            ({ view, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                input_date.setText(calendar.getDateStringByFormat())
            }), year, monthOfYear, dayOfMonth
        ).show()
    }

    override fun addTransaction(calendar: Calendar, transactionCategory: TransactionCategory) {
        val date = calendar.timeInMillis
        val description = getTransactionDescription(transactionCategory.transactionCategoryType)
        val amount = inputAmount.text.toString().toDouble()

        presenter.submitTransaction(
                date,
                description,
                amount,
                transactionCategory.transactionCategoryName,
                transactionCategory.transactionCategoryId,
                transactionCategory.transactionCategoryType
        )
    }

    override fun navigateToLanding(transactionDate: Long) {
        startActivity(
                LandingActivity.getStartIntent(
                        this, transactionDate
                )
        )
    }

    override fun displayScreen() {
        val transactionCategory =
            intent.getParcelableExtra<TransactionCategory>(EXTRA_TRANSACTION_CATEGORY)
        if(intent.hasExtra(EXTRA_TRANSACTION)){
            val transaction = intent.getParcelableExtra<Transaction>(EXTRA_TRANSACTION)
            inputAmount.setText(transaction.transactionAmount.toString())
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = transaction.transactionDate
            inputDate.setText(calendar.getDateStringByFormat("dd MMMM yyyy"))

        }
        val calendar = Calendar.getInstance()
        inputDate.setText(calendar.getDateStringByFormat())
        inputDate.setOnClickListener {
            setupDateDialogue(calendar)
        }

        button_add_transaction.setOnClickListener {
            addTransaction(calendar, transactionCategory)
        }
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }
}