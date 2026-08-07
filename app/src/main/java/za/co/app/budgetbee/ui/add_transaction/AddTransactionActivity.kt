package za.co.app.budgetbee.ui.add_transaction

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import za.co.app.budgetbee.R
import za.co.app.budgetbee.base.AppCompatBaseActivity
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.data.model.domain.TransactionCategory
import za.co.app.budgetbee.data.model.domain.TransactionCategoryType
import za.co.app.budgetbee.databinding.ActivityAddTransactionBinding
import za.co.app.budgetbee.ui.landing.LandingActivity
import za.co.app.budgetbee.utils.TextValidator
import za.co.app.budgetbee.utils.getDateStringByFormat
import za.co.app.budgetbee.utils.showDatePickerDialogAndDisplaySelectedDateTextToView
import java.util.Calendar
import javax.inject.Inject

class AddTransactionActivity : AppCompatBaseActivity(), IAddTransactionMvp.View {

    private lateinit var addTransactionButton: Button
    private lateinit var inputDate: TextInputEditText
    private lateinit var inputAmount: TextInputEditText
    private lateinit var binding: ActivityAddTransactionBinding

    @Inject
    lateinit var presenter: IAddTransactionMvp.Presenter

    companion object {
        const val EXTRA_TRANSACTION_CATEGORY = "EXTRA_TRANSACTION_CATEGORY"
        const val EXTRA_TRANSACTION = "EXTRA_TRANSACTION"

        fun getStartIntent(context: Context?, transactionCategory: TransactionCategory?): Intent {
            val intent = Intent(context, AddTransactionActivity::class.java)
            intent.putExtra(EXTRA_TRANSACTION_CATEGORY, transactionCategory)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter.attachView(this)
        addTransactionButton = binding.buttonAddTransaction
        addTransactionButton.isEnabled = false
        inputAmount = binding.inputAmount
        inputAmount.addTextChangedListener(object : TextValidator(inputAmount) {
            override fun validate(textView: TextView, text: String) {
                if (text.isEmpty()) {
                    textView.error = (getString(R.string.amount_error))
                    addTransactionButton.isEnabled = false
                } else
                    addTransactionButton.isEnabled = true
            }
        })

        inputDate = binding.inputDate
        binding.toolbar.screenTitle.text = getString(R.string.add_transaction)
        binding.toolbar.backButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        displayScreen()
    }

    private fun getTransactionDescription(transactionCategoryType: Int): String {
        val inputDescription = binding.inputDescription
        return inputDescription.text.toString().ifEmpty {
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
            ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
    }

    override fun displayScreen() {
        val transactionCategory =
            intent.getParcelableExtra<TransactionCategory>(EXTRA_TRANSACTION_CATEGORY)
        if (intent.hasExtra(EXTRA_TRANSACTION)) {
            val transaction = intent.getParcelableExtra<Transaction>(EXTRA_TRANSACTION)
            inputAmount.setText(transaction?.transactionAmount.toString())
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = transaction?.transactionDate ?: 0L
            inputDate.setText(calendar.getDateStringByFormat("dd MMMM yyyy"))

        }
        val calendar = Calendar.getInstance()
        inputDate.setText(calendar.getDateStringByFormat())
        inputDate.setOnClickListener {
            calendar.showDatePickerDialogAndDisplaySelectedDateTextToView(this, inputDate)
        }

        addTransactionButton.setOnClickListener {
            if (transactionCategory != null) {
                addTransaction(calendar, transactionCategory)
            }
        }
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }
}