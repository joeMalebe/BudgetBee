package za.co.app.budgetbee.ui.transaction

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_add_transaction.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.base.AppCompatBaseActivity
import za.co.app.budgetbee.data.model.domain.TransactionCategory
import za.co.app.budgetbee.data.model.domain.TransactionCategoryType
import za.co.app.budgetbee.ui.landing.LandingActivity
import za.co.app.budgetbee.utils.getDateStringByFormat
import java.util.*
import javax.inject.Inject

class AddTransactionActivity : AppCompatBaseActivity(), ITransactionMvp.View {

    @Inject
    lateinit var presenter: ITransactionMvp.Presenter

    companion object {
        const val EXTRA_TRANSACTION_CATEGORY = "EXTRA_TRANSACTION_CATEGORY"

        fun getStartIntent(context: Context?, transactionCategory: TransactionCategory): Intent {
            val intent = Intent(context, AddTransactionActivity::class.java)
            intent.putExtra(EXTRA_TRANSACTION_CATEGORY, transactionCategory)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)
        presenter.attachView(this)
        displayScreen()
    }

    private fun getTransactionDescription(transactionCategoryType: Int): String {
        return if (!input_description.text.toString().isEmpty()) {
            input_description.text.toString()
        } else {
            when (transactionCategoryType) {
                TransactionCategoryType.INCOME.value -> "Income"
                TransactionCategoryType.EXPENSE.value -> "Expense"
                else -> "Other"
            }
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
        val amount = input_amount.text.toString().toDouble()

        presenter.submitTransaction(
            date,
            description,
            amount,
            transactionCategory.transactionCategoryName,
            transactionCategory.transactionCategoryId
        )
    }

    override fun navigateToLanding() {
        startActivity(
            LandingActivity.getStartIntent(
                this
            )
        )
    }

    override fun displayScreen() {
        val transactionCategory =
            intent.getParcelableExtra<TransactionCategory>(EXTRA_TRANSACTION_CATEGORY)

        val calendar = Calendar.getInstance()
        input_date.setText(calendar.getDateStringByFormat())
        input_date.setOnClickListener {
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