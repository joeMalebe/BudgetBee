package za.co.app.budgetbee.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_landing.*
import za.co.app.budgetbee.BudgetBeeApplication
import za.co.app.budgetbee.R
import za.co.app.budgetbee.base.BaseObserver
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.data.repository.TransactionsRepository
import za.co.app.budgetbee.ui.transaction.transactions_category.TransactionCategoryActivity
import javax.inject.Inject

class LandingActivity : AppCompatActivity() {

    @Inject
    lateinit var transactionsRepository: TransactionsRepository

    companion object {
        fun getStartIntent(context: Context): Intent {
            val intent = Intent(context, LandingActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        BudgetBeeApplication.instance.feather.injectFields(this)
        buildScreen()
    }

    private fun buildScreen() {
        val addTransactionButton = add_transaction_fab
        //todo add adapter
        addTransactionButton.setOnClickListener {
            startActivity(TransactionCategoryActivity.getStartIntent(this))
        }
        getTransactions()
    }

    fun getTransactions() {
        transactionsRepository.getTransactions().observeOn(
            AndroidSchedulers.mainThread()
        ).subscribeOn(Schedulers.io()).subscribe(TransactionsObserver(this))
    }

    class TransactionsObserver(val activity: LandingActivity) :
        BaseObserver<ArrayList<Transaction>>() {
        override fun onNext(value: ArrayList<Transaction>) {
            if (value.isNotEmpty()) {

                Log.d(
                    "TransactionsObserver",
                    "size ${value.size}- ${value[(value.size - 1)]}"
                )
            }
            activity.buildScreen(value)
        }
    }

    private fun buildScreen(transactions: List<Transaction>) {
        if (transactions.isNotEmpty()) {
            //todo add adapter
        }
    }
}
