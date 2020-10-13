package za.co.app.budgetbee.ui.edit_transaction

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
import za.co.app.budgetbee.utils.getDateStringByFormat
import java.util.*
import javax.inject.Inject

class EditTransactionActivity : AppCompatBaseActivity(), IEditTransactionMvp.View {

    private lateinit var inputDate: TextInputEditText
    private lateinit var inputAmount: TextInputEditText

    @Inject
    lateinit var presenter: IEditTransactionMvp.Presenter

    companion object {
        private val EXTRA_TRANSACTION = "EXTRA_TRANSACTION"

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
        displayScreen()
    }

    override fun displayScreen() {
        val transactionCategory =
                intent.getParcelableExtra<TransactionCategory>(AddTransactionActivity.EXTRA_TRANSACTION_CATEGORY)
        if(intent.hasExtra(AddTransactionActivity.EXTRA_TRANSACTION)){
            val transaction = intent.getParcelableExtra<Transaction>(AddTransactionActivity.EXTRA_TRANSACTION)
            inputAmount.setText(transaction.transactionAmount.toString())
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = transaction.transactionDate
            inputDate.setText(calendar.getDateStringByFormat("dd MMMM yyyy"))

        }
        val calendar = Calendar.getInstance()
        inputDate.setText(calendar.getDateStringByFormat())
        inputDate.setOnClickListener {
            //setupDateDialogue(calendar)
        }

        button_edit_transaction.setOnClickListener {
            //editTransaction(calendar, transactionCategory)
        }
    }
}