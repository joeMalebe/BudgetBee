package za.co.app.budgetbee.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import za.co.app.budgetbee.R

class AddExpenseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)
        supportActionBar?.setTitle(R.string.expenses_heading)
    }


}
