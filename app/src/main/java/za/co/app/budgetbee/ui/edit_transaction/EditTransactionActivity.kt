package za.co.app.budgetbee.ui.edit_transaction

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import za.co.app.budgetbee.R
import za.co.app.budgetbee.data.model.domain.Transaction

class EditTransactionActivity : AppCompatActivity(), IEditTransactionMvp.View {

    companion object {
        private val EXTRA_TRANSACTION = "EXTRA_TRANSACTION"

        fun getStartIntent(context: Context, transaction: Transaction): Intent {
            val intent = Intent(context, EditTransactionActivity::class.java)
            intent.putExtra(EXTRA_TRANSACTION, transaction)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_transaction)
    }

    override fun displayScreen() {

    }
}