package za.co.app.budgetbee.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import za.co.app.budgetbee.R
import za.co.app.budgetbee.data.model.TransactionCategory

class AddTransactionActivity : AppCompatActivity() {

    companion object {
        val EXTRA_TRANSACTION_ID = "EXTRA_TRANSACTION_ID"
        val EXTRA_TRANSACTION_CATEGORY = "EXTRA_TRANSACTION_CATEGORY"

        fun getStartIntent(context: Context?, transactionCategory: TransactionCategory): Intent {
            val intent = Intent(context, AddTransactionActivity::class.java)
            intent.putExtra(EXTRA_TRANSACTION_CATEGORY, transactionCategory)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        val transactionCategory =
            intent.getParcelableExtra<TransactionCategory>(EXTRA_TRANSACTION_CATEGORY)
        Toast.makeText(this, transactionCategory.transactionCategoryName, Toast.LENGTH_SHORT).show()
    }
}
