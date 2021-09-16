package za.co.app.budgetbee.ui.report

import za.co.app.budgetbee.base.IBasePresenter
import za.co.app.budgetbee.base.IBaseView
import za.co.app.budgetbee.base.IServiceCallView
import za.co.app.budgetbee.data.model.domain.Transaction

interface IBalanceReportMvp {

    interface View :IBaseView, IServiceCallView {
        fun displayTransactions(transactions: List<Transaction>)
    }

    interface Presenter: IBasePresenter {
        fun getTransactionsGroupedByCategoryName()
    }
}