package za.co.app.budgetbee.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import za.co.app.budgetbee.R
import za.co.app.budgetbee.data.model.TransactionCategory

class AddTransactionActivity : AppCompatActivity() {

    companion object {
        val EXTRA_TRANSACTION_ID = "EXTRA_TRANSACTION_ID"
        val EXTRA_TRANSACTION_NAME = "EXTRA_TRANSACTION_NAME"

        fun getStartIntent(context: Context?, transactionCategory: TransactionCategory): Intent {
            val intent = Intent(context, AddTransactionActivity::class.java)
            intent.putExtra(EXTRA_TRANSACTION_NAME, transactionCategory.transactionCategoryName)
            intent.putExtra(EXTRA_TRANSACTION_ID, transactionCategory.transactionCategoryId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)
    }
}
