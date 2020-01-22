package za.co.app.budgetbee

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_landing.*
import za.co.app.budgetbee.ui.AddExpenseActivity

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
        buildScreen()
    }

    private fun buildScreen() {
        val addExpenseButton = add_expense_fab
        val addIncomeButton = add_income_fab

        addExpenseButton.setOnClickListener({
            startActivity(Intent(this, AddExpenseActivity::class.java))
        })

    }


}
