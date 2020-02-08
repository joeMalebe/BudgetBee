package za.co.app.budgetbee

import android.util.Log
import org.codejargon.feather.Provides
import za.co.app.budgetbee.data.model.database.BudgetBeeDatabase
import za.co.app.budgetbee.data.model.database.BudgetBeeDoa
import javax.inject.Singleton

class BudgetBeeAppModule(val application: BudgetBeeApplication) {
    val TAG = BudgetBeeAppModule::class.simpleName

    @Provides
    @Singleton
    fun budgetBeeDoa(): BudgetBeeDoa {
        Log.d(TAG, "get budgetBeeDoa doa instance")
        return BudgetBeeDatabase.getInstance(application).getTransactionCategoryDao()
    }
}