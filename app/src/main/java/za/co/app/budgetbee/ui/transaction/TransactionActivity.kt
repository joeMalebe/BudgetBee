package za.co.app.budgetbee.ui.transaction

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.widget.AppCompatImageView
import za.co.app.budgetbee.R
import za.co.app.budgetbee.base.AppCompatBaseActivity
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.data.model.domain.TransactionCategoryType
import za.co.app.budgetbee.databinding.ActivityTransactionBinding
import za.co.app.budgetbee.ui.edit_transaction.EditTransactionActivity
import za.co.app.budgetbee.ui.landing.LandingActivity
import za.co.app.budgetbee.utils.getDateStringByFormat
import java.util.Calendar
import javax.inject.Inject

class TransactionActivity : AppCompatBaseActivity(), ITransactionMvp.View {
    val TAG = TransactionActivity::class.simpleName

    private lateinit var deleteButton: AppCompatImageView
    private lateinit var editButton: Button
    private lateinit var backButton: AppCompatImageView
    private lateinit var transaction: Transaction
    private lateinit var binding: ActivityTransactionBinding

    @Inject
    private lateinit var presenter: ITransactionMvp.Presenter

    companion object {
        private val EXTRA_TRANSACTION = "EXTRA_TRANSACTION"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra(EXTRA_TRANSACTION)) {
            val parsedTransaction = intent.getParcelableExtra<Transaction>(EXTRA_TRANSACTION)
            if (parsedTransaction == null) {
                return
            } else {
                presenter.attachView(this)
                transaction = parsedTransaction
                backButton = binding.toolbar.backButton
                backButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
                editButton = binding.editButton
                deleteButton = binding.toolbar.deleteButton
            }
        }
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
        binding.toolbar.screenTitle.text = getString(R.string.transaction)
        binding.categoryNameText.text = transaction.transactionCategoryName
        binding.categoryTypeText.text = getCategoryTypeString()
        binding.amountText.text = transaction.transactionAmount.toString()
        binding.descriptionText.text = transaction.transactionDescription
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = transaction.transactionDate
        binding.dateText.text = calendar.getDateStringByFormat("dd MMMM yyyy")
        deleteButton.setOnClickListener { presenter.deleteTransaction(transaction) }
        editButton.setOnClickListener { startActivityForResult(EditTransactionActivity.getStartIntent(this, transaction), 2) }
    }

    private fun getCategoryTypeString(): CharSequence {
        return if (transaction.transactionCategoryType == TransactionCategoryType.INCOME.value)
            this.getString(R.string.income_heading)
        else
            this.getString(R.string.expense)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val SUCCESS = 200
        if (resultCode == SUCCESS && data != null) {
            val parsedTransaction = data.getParcelableExtra<Transaction>(EXTRA_TRANSACTION)
            if (parsedTransaction != null) {
                transaction = parsedTransaction
            }
            displayScreen()
        } else {
            showError()
        }
    }

    private fun showError() {
        Log.e(TAG, "showError: ")
    }
}