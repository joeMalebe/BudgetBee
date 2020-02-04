package za.co.app.budgetbee.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_transaction_category.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.base.BaseCompletableObserver
import za.co.app.budgetbee.data.model.BudgetBeeDatabase
import za.co.app.budgetbee.data.model.TransactionCategory
import za.co.app.budgetbee.data.model.TransactionCategoryDataModel
import za.co.app.budgetbee.data.model.TransactionCategoryType
import java.lang.ref.WeakReference

class AddTransactionCategoryActivity : AppCompatActivity() {

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, AddTransactionCategoryActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction_category)
        buildScreen()

    }

    private fun buildScreen() {
    }

    fun addTransactionCategory(view: View) {

        val UNCHECKED = -1
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
            BudgetBeeDatabase.getInstance(view.context).getTransactionCategoryDao()
                .insertTransactionCategory(
                    TransactionCategoryDataModel(
                        transactionCategory.transactionCategoryName,
                        transactionCategory.transactionCategoryType
                    )
                ).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(TransactionCategoryObserver(this))
        }
    }

    private fun createTransactionCategory(
        categoryName: String,
        categoryType: TransactionCategoryType
    ): TransactionCategory {
        return TransactionCategory(0, categoryName, categoryType.value)
    }

    class TransactionCategoryObserver(activity: AddTransactionCategoryActivity) :
        BaseCompletableObserver() {

        val activity = WeakReference(activity).get()
        override fun onComplete() {
            activity?.startActivity(TransactionCategoryActivity.getStartIntent(activity))

        }
    }
}
