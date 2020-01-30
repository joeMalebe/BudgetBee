package za.co.app.budgetbee.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_transaction_category.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.data.model.BudgetBeeDatabase
import za.co.app.budgetbee.data.model.TransactionCategory
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

            BudgetBeeDatabase.getInstance(view.context).getTransactionCategoryDao()
                .addTransationCategory(
                    TransactionCategory(categoryName, categoryType.value)
                ).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(TransactionCategoryObserver(this))

        }
    }

    class TransactionCategoryObserver(activity: AddTransactionCategoryActivity) :
        CompletableObserver {

        val activity = WeakReference(activity).get()
        override fun onComplete() {
            if (activity != null) {
                Log.i(activity.localClassName, "OnComplete called")
            }
        }

        override fun onSubscribe(d: Disposable) {
            if (activity != null) {
                Log.d(activity.localClassName, d.toString())
            }

        }

        override fun onError(e: Throwable) {
            if (activity != null) {
                Toast.makeText(activity, "$e", Toast.LENGTH_SHORT).show()
                Log.e(activity.localClassName, e.message, e)
            }

        }

    }

}
