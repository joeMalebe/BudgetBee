package za.co.app.budgetbee.ui.add_transaction

import za.co.app.budgetbee.base.IBasePresenter
import za.co.app.budgetbee.base.IBaseView
import za.co.app.budgetbee.data.model.domain.TransactionCategory
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