package za.co.app.budgetbee.ui.transactions_category

import za.co.app.budgetbee.base.IBasePresenter
import za.co.app.budgetbee.base.IBaseView
import za.co.app.budgetbee.data.model.domain.TransactionCategoryType

interface ITransactionCategoryMvp {
    interface View : IBaseView {
        fun showLoading()
        fun dismissLoading()
        fun navigateToTransactionCategorySelectScreen()
    }

    interface Presenter : IBasePresenter {
        fun submitTransactionCategory(categoryName: String, categoryType: TransactionCategoryType)
    }
}