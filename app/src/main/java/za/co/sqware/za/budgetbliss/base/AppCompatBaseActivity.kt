package za.co.sqware.za.budgetbliss.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import za.co.sqware.za.budgetbliss.BudgetBeeApplication

abstract class AppCompatBaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BudgetBeeApplication.instance.feather.injectFields(this)
    }
}