package za.co.app.budgetbee

import android.app.Application
import android.content.Context
import org.codejargon.feather.Feather

class BudgetBeeApplication : Application() {

    lateinit var feather: Feather

    companion object {

        lateinit var instance: BudgetBeeApplication
        fun getContext(): Context {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        feather = Feather.with(BudgetBeeAppModule(this))
        feather.injectFields(this)
    }
}