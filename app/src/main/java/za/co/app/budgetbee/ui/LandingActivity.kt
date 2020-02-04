package za.co.app.budgetbee.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_landing.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.data.model.BudgetBeeDatabase
import za.co.app.budgetbee.data.model.BudgetBeeDatabase.Companion.getInstance
import za.co.app.budgetbee.data.model.TransactionDataModel

class LandingActivity : AppCompatActivity() {

    companion object {
        fun getStartIntent(context: Context): Intent {
            val intent = Intent(context, LandingActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        getInstance(this)
        buildScreen()
    }

    private fun buildScreen() {
        val addExpenseButton = add_expense_fab

        addExpenseButton.setOnClickListener {
            startActivity(TransactionCategoryActivity.getStartIntent(this))
        }
        getTransactions()
    }

    fun getTransactions() {
        BudgetBeeDatabase.getInstance(this).getTransactionCategoryDao()
            .getTransactions().observeOn(
                AndroidSchedulers.mainThread()
            ).subscribeOn(Schedulers.io()).subscribe(TransactionsObserver(this))
    }

    class TransactionsObserver(val activity: LandingActivity) :
        Observer<List<TransactionDataModel>> {
        override fun onSubscribe(d: Disposable) {

        }

        override fun onComplete() {
        }

        override fun onNext(t: List<TransactionDataModel>) {
            if (t != null && t.isNotEmpty()) {

                Log.d("TransactionsObserver", "size ${t.size}- ${t.get(0)}")
            }
            activity.buildScreen(t)

        }

        override fun onError(e: Throwable) {
        }

    }

    private fun buildScreen(transactions: List<TransactionDataModel>?) {
        if (transactions != null && transactions.isNotEmpty()) {

            Toast.makeText(
                this,
                "list size ${transactions.size} - ${transactions.get(0)}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
