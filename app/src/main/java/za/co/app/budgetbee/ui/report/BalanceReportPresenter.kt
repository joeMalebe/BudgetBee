package za.co.app.budgetbee.ui.report

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import za.co.app.budgetbee.base.BasePresenterObserver
import za.co.app.budgetbee.base.IBaseView
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.data.repository.IDatabaseRepository
import java.util.*
import java.util.logging.Logger

class BalanceReportPresenter(private val transactionsRepository: IDatabaseRepository) : IBalanceReportMvp.Presenter {
    private lateinit var view: IBalanceReportMvp.View
    private lateinit var transactions: ArrayList<Transaction>

    enum class PERIOD(val textValue: String) {
        LAST_WEEK("Last week"), LAST_MONTH("Last month"), LAST_YEAR("Last year"), ALL_TIME("All time")
    }

    override fun getTransactionsGroupedByCategoryType(period: PERIOD) {
        view.showLoading()
        transactionsRepository.getTransactions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(TransactionsObserver(this.view, period))
    }

    override fun updateTimePeriod(period: PERIOD) {
        view.displayTransactions(filteredTransactionsByPeriod(period, transactions))
    }

    override fun attachView(view: IBaseView) {
        this.view = view as IBalanceReportMvp.View
    }

    override fun detachView() {
        Logger.getAnonymousLogger().info("detach View")
    }

    private fun filteredTransactionsByPeriod(period: PERIOD, value: ArrayList<Transaction>): Map<Int, List<Transaction>> {
        return when (period) {
            PERIOD.LAST_MONTH -> {
                getGroupedTransactions(getTransactionsByTimePeriod(30))
            }

            PERIOD.LAST_WEEK -> {
                getGroupedTransactions(getTransactionsByTimePeriod(7))
            }

            PERIOD.LAST_YEAR -> {
                getGroupedTransactions(getTransactionsByTimePeriod(365))

            }

            else -> {
                getGroupedTransactions(value)
            }
        }
    }

    private fun getTransactionsByTimePeriod(daysToMinus: Int): List<Transaction> {
        return transactions.filter { transaction ->
            val date = Calendar.getInstance()
            date.add(Calendar.DAY_OF_MONTH, -daysToMinus)
            transaction.transactionDate >= date.timeInMillis
        }
    }

    private fun getGroupedTransactions(transactions: List<Transaction>): Map<Int, List<Transaction>> {
        return transactions.groupBy { it.transactionCategoryType }
    }

    inner class TransactionsObserver(override val view: IBalanceReportMvp.View, var period: PERIOD = PERIOD.ALL_TIME) : BasePresenterObserver<ArrayList<Transaction>>(view) {

        override fun onNext(value: ArrayList<Transaction>) {
            transactions = value
            view.displayTransactions(filteredTransactionsByPeriod(period, value))
        }
    }
}