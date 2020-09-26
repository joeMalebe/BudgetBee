package za.co.app.budgetbee.ui.landing

import za.co.app.budgetbee.base.IBasePresenter
import za.co.app.budgetbee.base.IBaseView
import za.co.app.budgetbee.data.model.domain.Transaction

interface ILandingMvp {

    interface View : IBaseView {
        fun showLoading()
        fun dismissLoading()
        fun showError(error: Throwable)
        fun displayTransactions(transactions: ArrayList<Transaction>)
        fun openTransactionCategoryActivity()
        fun displayTotalIncome(income: Double)
        fun displayTotalExpense(expense: Double)
        fun displayBalance(balance: Double)
        fun displayNoTransactions()
    }

    interface Presenter : IBasePresenter {
        fun getTransactions()
        fun getTransactionsByDate(dateRange: Pair<Long, Long>)
        fun getTotalIncome(transactions: java.util.ArrayList<Transaction>)
        fun getTotalExpense(transactions: java.util.ArrayList<Transaction>): Any
    }
}