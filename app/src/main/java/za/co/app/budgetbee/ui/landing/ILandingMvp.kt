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
    }

    interface Presenter : IBasePresenter {
        fun getTransactions()
    }
}