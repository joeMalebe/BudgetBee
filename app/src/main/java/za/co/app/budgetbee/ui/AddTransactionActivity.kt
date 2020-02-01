package za.co.app.budgetbee.ui

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_transaction.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.data.model.TransactionCategory
import za.co.app.budgetbee.utils.getDateStringByFormat
import java.util.*

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

        val calendar = Calendar.getInstance()
        input_date.setText(calendar.getDateStringByFormat())
        input_date.setOnClickListener {
            setupDateDialogue(calendar)
        }
    }

    fun setupDateDialogue(calendar: Calendar) {
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
}
