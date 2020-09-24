package za.co.app.budgetbee.ui.landing

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import za.co.app.budgetbee.base.BaseObserver
import za.co.app.budgetbee.base.IBaseView
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.data.model.domain.TransactionCategory
import za.co.app.budgetbee.data.model.domain.TransactionCategoryType
import za.co.app.budgetbee.data.repository.IDatabaseRepository
import za.co.app.budgetbee.data.repository.TransactionsRepository
import java.util.logging.Logger

class LandingPresenter(
        val transactionsRepository: IDatabaseRepository
) : ILandingMvp.Presenter {
    private lateinit var view: ILandingMvp.View
    private val compositeDisposable = CompositeDisposable()
    override fun getTransactions() {
        Observable.zip(
                transactionsRepository.getAllTransactionCategories(),
                transactionsRepository.getTransactions(),
                BiFunction<ArrayList<TransactionCategory>, ArrayList<Transaction>, CombinedTransactionAndCategory> { transactionsCategrory, transactions ->
                    CombinedTransactionAndCategory(
                            transactions,
                            transactionsCategrory
                    )
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                TransactionsObserver(
                        view
                )
        )
    }

    override fun getTransactionsByDate(dateRange: Pair<Long, Long>) {
        Observable.zip(
                transactionsRepository.getAllTransactionCategories(),
                transactionsRepository.getTransactionsByDateRange(dateRange),
                BiFunction<ArrayList<TransactionCategory>, ArrayList<Transaction>, CombinedTransactionAndCategory> { transactionsCategrory, transactions ->
                    CombinedTransactionAndCategory(transactions, transactionsCategrory)
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(TransactionsObserver(view))
    }

    override fun getTotalIncome(transactions: java.util.ArrayList<Transaction>) {
        //
    }

    override fun getTotalExpense(transactions: java.util.ArrayList<Transaction>): Any {
        return ""
    }

    override fun attachView(view: IBaseView) {
        this.view = view as ILandingMvp.View
    }

    override fun detachView() {
        Logger.getAnonymousLogger().info("LandingPresenter Stopped")
    }

    inner class TransactionsObserver(val view: ILandingMvp.View) :
            BaseObserver<CombinedTransactionAndCategory>() {
        override fun onNext(value: CombinedTransactionAndCategory) {
            val transactions = value.transactions
            if (transactions.isNotEmpty()) {

                val totalIncome = calculateTotalIncome(value)
                val totalExpense = calculateExpenseTotal(value)
                val budgetBalance = calculateBalance(totalIncome, totalExpense)

                view.displayTotalIncome(totalIncome)
                view.displayTotalExpense(totalExpense)
                view.displayBalance(budgetBalance)
                view.displayTransactions(value.transactions)
            } else {
                view.displayNoTransactions()
            }
        }

        override fun onError(error: Throwable) {
            super.onError(error)
            Logger.getAnonymousLogger().info(error.stackTrace.toString())
            view.showError(error)
        }
    }

    fun calculateTotalIncome(combinedTransactionAndCategory: CombinedTransactionAndCategory): Double {
        val incomeTransactions =
                (transactionsRepository as TransactionsRepository).getTransactionsByCategoryType(
                        combinedTransactionAndCategory.transactionCategory,
                        combinedTransactionAndCategory.transactions,
                        TransactionCategoryType.INCOME
                )
        return incomeTransactions.sumByDouble { transaction -> transaction.transactionAmount }
    }

    fun calculateExpenseTotal(combinedTransactionAndCategory: CombinedTransactionAndCategory): Double {
        val expenseTransactions =
                (transactionsRepository as TransactionsRepository).getTransactionsByCategoryType(
                        combinedTransactionAndCategory.transactionCategory,
                        combinedTransactionAndCategory.transactions,
                        TransactionCategoryType.EXPENSE
                )
        return expenseTransactions.sumByDouble { transaction -> transaction.transactionAmount }
    }

    fun calculateBalance(income: Double, expense: Double): Double {
        return income - expense
    }

    class CombinedTransactionAndCategory(
            val transactions: ArrayList<Transaction>,
            val transactionCategory: ArrayList<TransactionCategory>
    )
}