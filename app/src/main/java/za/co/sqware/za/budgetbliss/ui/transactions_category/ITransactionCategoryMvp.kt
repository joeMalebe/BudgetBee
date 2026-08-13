package za.co.sqware.za.budgetbliss.ui.transactions_category

import za.co.sqware.za.budgetbliss.base.IBasePresenter
import za.co.sqware.za.budgetbliss.base.IBaseView
import za.co.sqware.za.budgetbliss.data.model.domain.TransactionCategoryType

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