package za.co.app.budgetbee.ui.edit_transaction

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_add_transaction.input_amount
import kotlinx.android.synthetic.main.activity_add_transaction.input_date
import kotlinx.android.synthetic.main.activity_edit_transaction.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.base.AppCompatBaseActivity
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.data.model.domain.TransactionCategory
import za.co.app.budgetbee.ui.add_transaction.AddTransactionActivity
import za.co.app.budgetbee.ui.add_transaction.select_transaction_category.SelectTransactionCategoryActivity
import za.co.app.budgetbee.utils.getDateStringByFormat
import za.co.app.budgetbee.utils.showDatePickerDialogAndDisplaySelectedDateTextToView
import java.util.*
import javax.inject.Inject

class EditTransactionActivity : AppCompatBaseActivity(), IEditTransactionMvp.View {

    private lateinit var inputDate: TextInputEditText
    private lateinit var inputAmount: TextInputEditText
    private lateinit var inputDescription: TextInputEditText
    private lateinit var inputTransactionCategory: TextInputEditText

    @Inject
    lateinit var presenter: IEditTransactionMvp.Presenter

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
        inputAmount = input_amount
        inputDate = input_date
        inputDescription = input_description
        inputTransactionCategory = input_transaction_category
        displayScreen()
    }

    override fun displayScreen() {
        val transaction = intent.getParcelableExtra<Transaction>(EXTRA_TRANSACTION)
        inputAmount.setText(transaction.transactionAmount.toString())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = transaction.transactionDate
        inputDate.setText(calendar.getDateStringByFormat("dd MMMM yyyy"))
        inputAmount.setText(transaction.transactionAmount.toString())
        inputDescription.setText(transaction.transactionDescription)
        inputTransactionCategory.setOnClickListener {
            startActivityForResult(SelectTransactionCategoryActivity.getStartIntent(it.context),2)
        }
        inputTransactionCategory.setText(transaction.transactionCategoryName)

        inputDate.setText(calendar.getDateStringByFormat())
        inputDate.setOnClickListener {
            calendar.showDatePickerDialogAndDisplaySelectedDateTextToView(this, inputDate)
        }

        button_edit_transaction.setOnClickListener {
            //editTransaction(calendar, transactionCategory)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            val transactionCategory = data.getParcelableExtra<TransactionCategory>(AddTransactionActivity.EXTRA_TRANSACTION_CATEGORY)
            inputTransactionCategory.setText(transactionCategory.transactionCategoryName)
        }
    }
}