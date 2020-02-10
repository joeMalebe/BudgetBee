package za.co.app.budgetbee.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import za.co.app.budgetbee.BudgetBeeApplication

abstract class AppCompatBaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        BudgetBeeApplication.instance.feather.injectFields(this)
    }
}