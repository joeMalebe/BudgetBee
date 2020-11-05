package za.co.app.budgetbee.ui.select_transaction_category

import za.co.app.budgetbee.base.IBasePresenter
import za.co.app.budgetbee.base.IServiceCallView
import za.co.app.budgetbee.data.model.domain.TransactionCategory
import za.co.app.budgetbee.data.model.domain.TransactionCategoryType
import java.util.*

interface ISelectTransactionCategoryMvp {
    interface View : IServiceCallView{
        fun displayCategories(transactionCategories: ArrayList<TransactionCategory>)
    }

    interface Presenter : IBasePresenter{
        fun getAllTransactionCategoriesByType(transactionCategoryType: TransactionCategoryType)
    }
}