package za.co.sqware.za.budgetbliss.ui.report

import za.co.sqware.za.budgetbliss.base.IBasePresenter
import za.co.sqware.za.budgetbliss.base.IBaseView
import za.co.sqware.za.budgetbliss.base.IServiceCallView
import za.co.sqware.za.budgetbliss.data.model.domain.Transaction

interface IBalanceReportMvp {

    interface View :IBaseView, IServiceCallView {
        fun displayTransactions(transactionsByCategory: Map<Int, List<Transaction>>)
        fun displayNoTransactions()
    }

    interface Presenter: IBasePresenter {
        fun getTransactionsGroupedByCategoryType(period: BalanceReportPresenter.PERIOD)
        fun updateTimePeriod(period: BalanceReportPresenter.PERIOD)
    }
}