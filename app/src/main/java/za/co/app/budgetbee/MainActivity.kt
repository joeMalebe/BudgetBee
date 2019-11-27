package za.co.app.budgetbee

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.BudgetBeeMainTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}
