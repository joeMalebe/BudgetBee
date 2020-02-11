package za.co.app.budgetbee.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import za.co.app.budgetbee.BudgetBeeApplication

abstract class AppCompatBaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BudgetBeeApplication.instance.feather.injectFields(this)
    }
}