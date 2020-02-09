package za.co.app.budgetbee.ui.transaction

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_transaction.*
import za.co.app.budgetbee.BudgetBeeApplication
import za.co.app.budgetbee.R
import za.co.app.budgetbee.base.BaseCompletableObserver
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.data.model.domain.TransactionCategory
import za.co.app.budgetbee.data.model.domain.TransactionCategoryType
import za.co.app.budgetbee.data.repository.TransactionsRepository
import za.co.app.budgetbee.ui.landing.LandingActivity
import za.co.app.budgetbee.utils.getDateStringByFormat
import java.lang.ref.WeakReference
import java.util.*
import javax.inject.Inject

class AddTransactionActivity : AppCompatActivity() {

    @Inject
    lateinit var transactionsRepository: TransactionsRepository

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
        BudgetBeeApplication.instance.feather.injectFields(this)

        val transactionCategory =
            intent.getParcelableExtra<TransactionCategory>(EXTRA_TRANSACTION_CATEGORY)
        Toast.makeText(this, transactionCategory.transactionCategoryName, Toast.LENGTH_SHORT).show()

        val calendar = Calendar.getInstance()
        input_date.setText(calendar.getDateStringByFormat())
        input_date.setOnClickListener {
            setupDateDialogue(calendar)
        }

        button_add_transaction.setOnClickListener {
            val date = calendar.timeInMillis
            val description = getTransactionDescription(transactionCategory.transactionCategoryType)
            val amount = input_amount.text.toString().toDouble()

            transactionsRepository.insertTransaction(
                Transaction(
                    date,
                    description,
                    amount,
                    transactionCategory.transactionCategoryId
                )
            ).observeOn(
                AndroidSchedulers.mainThread()
            ).subscribeOn(Schedulers.io()).subscribe(
                TransactionObserver(this)
            )
        }
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
                Toast.makeText(
                    view.context,
                    "$selectedYear/${selectedMonth + 1}/$selectedDay",
                    Toast.LENGTH_LONG
                ).show()
            }), year, monthOfYear, dayOfMonth
        ).show()
    }

    class TransactionObserver(activity: Activity) :
        BaseCompletableObserver() {
        private val activity = WeakReference(activity).get()
        override fun onComplete() {
            if (activity != null) {
                Log.d("TransactionObserver", "OnComplete(Added Transaction")
                activity.startActivity(
                    LandingActivity.getStartIntent(
                        activity
                    )
                )
            }
        }
    }
}
