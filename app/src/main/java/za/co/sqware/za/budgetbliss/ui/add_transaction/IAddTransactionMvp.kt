package za.co.sqware.za.budgetbliss.ui.add_transaction

import za.co.sqware.za.budgetbliss.base.IBasePresenter
import za.co.sqware.za.budgetbliss.base.IBaseView
import za.co.sqware.za.budgetbliss.data.model.domain.TransactionCategory
import java.util.*

interface IAddTransactionMvp {
    interface View : IBaseView {
        fun addTransaction(calendar: Calendar, transactionCategory: TransactionCategory)
        fun navigateToLanding(transactionDate: Long)
    }

    interface Presenter : IBasePresenter {
        fun submitTransaction(
                transactionDate: Long,
                transactionDescription: String,
                transactionAmount: Double,
                transactionCategoryName: String,
                transactionCategoryId: Int,
                transactionCategoryType: Int
        )
    }
}