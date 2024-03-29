package za.co.app.budgetbee.ui.edit_transaction

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.textfield.TextInputEditText
import za.co.app.budgetbee.R
import za.co.app.budgetbee.base.AppCompatBaseActivity
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.data.model.domain.TransactionCategory
import za.co.app.budgetbee.databinding.ActivityEditTransactionBinding
import za.co.app.budgetbee.ui.add_transaction.AddTransactionActivity
import za.co.app.budgetbee.ui.landing.LandingActivity
import za.co.app.budgetbee.ui.select_transaction_category.SelectTransactionCategoryActivity
import za.co.app.budgetbee.utils.TextValidator
import za.co.app.budgetbee.utils.displayLongDouble
import za.co.app.budgetbee.utils.getDateStringByFormat
import za.co.app.budgetbee.utils.showDatePickerDialogAndDisplaySelectedDateTextToView
import java.util.Calendar
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
    private lateinit var binding: ActivityEditTransactionBinding

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
        binding = ActivityEditTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter.attachView(this)
        val parsedTransaction = intent.getParcelableExtra<Transaction>(EXTRA_TRANSACTION)
        if (parsedTransaction == null) {
            return
        } else {
            binding.toolbar.screenTitle.text = getString(R.string.transaction)
            inputAmount = binding.inputAmount
            val editButton = binding.buttonEditTransaction
            inputAmount.addTextChangedListener(object : TextValidator(inputAmount) {
                override fun validate(textView: TextView, text: String) {
                    if (text.isEmpty()) {
                        textView.error = (getString(R.string.amount_error))
                        editButton.isEnabled = false
                    } else
                        editButton.isEnabled = true
                }
            })
            inputDate = binding.inputDate
            inputDescription = binding.inputDescription
            inputTransactionCategory = binding.inputTransactionCategory
            backButton = binding.toolbar.backButton
            backButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
            deleteButton = binding.toolbar.deleteButton
            transaction = parsedTransaction
            transactionCategory = TransactionCategory(
                transaction.transactionCategoryId,
                transaction.transactionCategoryName,
                transaction.transactionCategoryType
            )

            displayScreen()
        }
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

    private fun navigateToLanding(transactionDate: Long) {
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
            startActivityForResult(SelectTransactionCategoryActivity.getStartIntent(it.context), 2)
        }

        inputDate.setOnClickListener {
            calendar.showDatePickerDialogAndDisplaySelectedDateTextToView(this, inputDate)
        }

        deleteButton.setOnClickListener {
            val alert = gerDisclaimerDialog()
            alert.show()
        }

        binding.buttonEditTransaction.setOnClickListener {
            bindTransactionValues(transaction, calendar)
            presenter.updateTransaction(transaction)

        }
    }

    private fun gerDisclaimerDialog(): AlertDialog {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.are_you_sure_you_want_to_delete_this_transaction)
            .setTitle(R.string.delete_transaction)
            .setCancelable(false)
            .setPositiveButton(R.string.yes) { _, _ -> presenter.deleteTransaction(transaction) }
            .setNegativeButton(R.string.no) { dialog, _ -> dialog.cancel() }
        return builder.create()
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
            val parsedTransactionCategory = data.getParcelableExtra<TransactionCategory>(AddTransactionActivity.EXTRA_TRANSACTION_CATEGORY)
            if (parsedTransactionCategory == null) {
                return
            } else {
                transactionCategory = parsedTransactionCategory
                inputTransactionCategory.setText(transactionCategory.transactionCategoryName)
            }
        }
    }
}