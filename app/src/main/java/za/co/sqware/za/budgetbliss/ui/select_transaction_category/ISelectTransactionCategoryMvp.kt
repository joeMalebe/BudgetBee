package za.co.sqware.za.budgetbliss.ui.select_transaction_category

import za.co.sqware.za.budgetbliss.base.IBasePresenter
import za.co.sqware.za.budgetbliss.base.IServiceCallView
import za.co.sqware.za.budgetbliss.data.model.domain.TransactionCategory
import za.co.sqware.za.budgetbliss.data.model.domain.TransactionCategoryType
import java.util.*

interface ISelectTransactionCategoryMvp {
    interface View : IServiceCallView{
        fun displayCategories(transactionCategories: ArrayList<TransactionCategory>)
    }

    interface Presenter : IBasePresenter{
        fun getAllTransactionCategoriesByType(transactionCategoryType: TransactionCategoryType)
    }
}