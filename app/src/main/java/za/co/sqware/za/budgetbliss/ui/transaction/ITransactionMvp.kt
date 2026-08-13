package za.co.sqware.za.budgetbliss.ui.transaction

import za.co.sqware.za.budgetbliss.base.IBasePresenter
import za.co.sqware.za.budgetbliss.base.IBaseView
import za.co.sqware.za.budgetbliss.data.model.domain.Transaction

interface ITransactionMvp {
    interface View : IBaseView {
        fun navigateToLanding(transactionDate: Long)
        fun showLoading()
        fun dismissLoading()

    }

    interface Presenter : IBasePresenter {
        fun deleteTransaction(transaction: Transaction)

    }
}