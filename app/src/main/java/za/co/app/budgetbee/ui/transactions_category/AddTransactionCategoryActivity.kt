package za.co.app.budgetbee.ui.transactions_category

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_transaction_category.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.base.AppCompatBaseActivity
import za.co.app.budgetbee.base.BaseCompletableObserver
import za.co.app.budgetbee.data.model.domain.TransactionCategory
import za.co.app.budgetbee.data.model.domain.TransactionCategoryType
import za.co.app.budgetbee.data.repository.TransactionsRepository
import java.lang.ref.WeakReference
import javax.inject.Inject

class AddTransactionCategoryActivity : AppCompatBaseActivity() {

    @Inject
    lateinit var transactionsRepository: TransactionsRepository
    companion object {

        const val UNCHECKED = -1
        fun getStartIntent(context: Context): Intent {
            return Intent(context, AddTransactionCategoryActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction_category)

    }

    fun addTransactionCategory(view: View) {


        if (radio_group_transation_category.checkedRadioButtonId == UNCHECKED) {
            Toast.makeText(view.context, "Please select a category", Toast.LENGTH_SHORT).show()
        } else {

            val categoryType = if (radio_income.isChecked) {
                TransactionCategoryType.INCOME
            } else {
                TransactionCategoryType.EXPENSE
            }
            val categoryName = input_category_name_editText.text.toString()
            val transactionCategory = createTransactionCategory(categoryName, categoryType)
            transactionsRepository
                .insertTransactionCategory(transactionCategory)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(
                    TransactionCategoryObserver(
                        this
                    )
                )
        }
    }

    private fun createTransactionCategory(
        categoryName: String,
        categoryType: TransactionCategoryType
    ): TransactionCategory {
        return TransactionCategory(
            0,
            categoryName,
            categoryType.value
        )
    }

    class TransactionCategoryObserver(activity: AddTransactionCategoryActivity) :
        BaseCompletableObserver() {
        val activity = WeakReference(activity).get()
        override fun onComplete() {
            activity?.startActivity(
                TransactionCategoryActivity.getStartIntent(
                    activity
                )
            )
        }
    }
}
