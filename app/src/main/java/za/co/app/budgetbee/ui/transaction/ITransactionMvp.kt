package za.co.app.budgetbee.ui.transaction

import za.co.app.budgetbee.base.IBasePresenter
import za.co.app.budgetbee.base.IBaseView
import za.co.app.budgetbee.data.model.domain.Transaction

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