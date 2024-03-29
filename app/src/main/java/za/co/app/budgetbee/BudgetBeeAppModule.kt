package za.co.app.budgetbee

import android.util.Log
import androidx.annotation.Keep
import org.codejargon.feather.Provides
import za.co.app.budgetbee.data.model.database.BudgetBeeDatabase
import za.co.app.budgetbee.data.model.database.BudgetBeeDoa
import za.co.app.budgetbee.data.repository.IDatabaseRepository
import za.co.app.budgetbee.data.repository.TransactionsRepository
import za.co.app.budgetbee.ui.add_transaction.AddTransactionPresenter
import za.co.app.budgetbee.ui.add_transaction.IAddTransactionMvp
import za.co.app.budgetbee.ui.edit_transaction.EditTransactionPresenter
import za.co.app.budgetbee.ui.edit_transaction.IEditTransactionMvp
import za.co.app.budgetbee.ui.landing.ILandingMvp
import za.co.app.budgetbee.ui.landing.LandingPresenter
import za.co.app.budgetbee.ui.report.BalanceReportPresenter
import za.co.app.budgetbee.ui.report.IBalanceReportMvp
import za.co.app.budgetbee.ui.report.ReportPresenter
import za.co.app.budgetbee.ui.select_transaction_category.ISelectTransactionCategoryMvp
import za.co.app.budgetbee.ui.select_transaction_category.SelectTransactionCategoryPresenter
import za.co.app.budgetbee.ui.transaction.ITransactionMvp
import za.co.app.budgetbee.ui.transaction.TransactionPresenter
import za.co.app.budgetbee.ui.transactions_category.ITransactionCategoryMvp
import javax.inject.Named
import javax.inject.Singleton

@Keep
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
        return za.co.app.budgetbee.ui.transactions_category.AddTransactionCategoryPresenter(
            transactionsRepository
        )
    }

    @Provides
    @Singleton
    fun addTransactionPresenter(transactionsRepository: IDatabaseRepository): IAddTransactionMvp.Presenter {
        return AddTransactionPresenter(transactionsRepository)
    }

    @Provides
    @Singleton
    fun transactionPresenter(transactionsRepository: IDatabaseRepository): ITransactionMvp.Presenter {
        return TransactionPresenter(transactionsRepository)
    }

    @Provides
    @Singleton
    fun editTransactionPresenter(transactionsRepository: IDatabaseRepository): IEditTransactionMvp.Presenter {
        return EditTransactionPresenter(transactionsRepository)
    }

    @Provides
    @Named("expense")
    @Singleton
    fun transactionCategoryAddExpenseCategoryActivityPresenter(transactionsRepository: IDatabaseRepository): ISelectTransactionCategoryMvp.Presenter {
        return SelectTransactionCategoryPresenter(transactionsRepository)
    }

    @Provides
    @Named("income")
    @Singleton
    fun transactionCategoryAddIncomeCategoryActivityPresenter(transactionsRepository: IDatabaseRepository): ISelectTransactionCategoryMvp.Presenter {
        return SelectTransactionCategoryPresenter(transactionsRepository)
    }

    @Provides
    @Singleton
    fun reportActivityPresenter(transactionsRepository: IDatabaseRepository): ReportPresenter {
        return ReportPresenter(transactionsRepository)
    }

    @Provides
    @Singleton
    fun balancereportActivityPresenter(transactionsRepository: IDatabaseRepository): IBalanceReportMvp.Presenter {
        return BalanceReportPresenter(transactionsRepository)
    }
}