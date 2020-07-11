package za.co.app.budgetbee

import android.util.Log
import org.codejargon.feather.Provides
import za.co.app.budgetbee.data.model.database.BudgetBeeDatabase
import za.co.app.budgetbee.data.model.database.BudgetBeeDoa
import za.co.app.budgetbee.data.repository.IDatabaseRepository
import za.co.app.budgetbee.data.repository.TransactionsRepository
import za.co.app.budgetbee.ui.landing.ILandingMvp
import za.co.app.budgetbee.ui.landing.LandingPresenter
import za.co.app.budgetbee.ui.transaction.ITransactionMvp
import za.co.app.budgetbee.ui.transaction.TransactionPresenter
import za.co.app.budgetbee.ui.transactions_category.ITransactionCategoryMvp
import javax.inject.Singleton

class BudgetBeeAppModule(val application: BudgetBeeApplication) {
    val TAG = BudgetBeeAppModule::class.simpleName

    @Provides
    @Singleton
    fun budgetBeeDoa(): BudgetBeeDoa {
        Log.d(TAG, "get budgetBeeDoa doa instance")
        return BudgetBeeDatabase.getInstance(application).getTransactionCategoryDao()
    }

    @Provides
    @Singleton
    fun transactionsRepository(budgetBeeDoa: BudgetBeeDoa): IDatabaseRepository {
        return TransactionsRepository(budgetBeeDoa)
    }

    @Provides
    @Singleton
    fun landingActivityPresenter(transactionsRepository: IDatabaseRepository): ILandingMvp.Presenter {
        return LandingPresenter(
            transactionsRepository
        )
    }

    @Provides
    @Singleton
    fun transactionCategoryAddCategoryActivityPresenter(transactionsRepository: IDatabaseRepository): ITransactionCategoryMvp.Presenter {
        return za.co.app.budgetbee.ui.transactions_category.TransactionCategoryAddCategoryPresenter(
            transactionsRepository
        )
    }

    @Provides
    @Singleton
    fun transactionPresenter(transactionsRepository: IDatabaseRepository): ITransactionMvp.Presenter {
        return TransactionPresenter(transactionsRepository)
    }
}