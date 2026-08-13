package za.co.sqware.za.budgetbliss.ui.edit_transaction

import za.co.sqware.za.budgetbliss.base.IBasePresenter
import za.co.sqware.za.budgetbliss.base.ILoadableView
import za.co.sqware.za.budgetbliss.data.model.domain.Transaction

interface IEditTransactionMvp {

    interface View : ILoadableView {
        fun updateSuccessful()
        fun updateError(error: Throwable?)
        fun deleteSuccessful(transactionDate: Long)

    }

    interface Presenter : IBasePresenter {
        fun updateTransaction(transaction: Transaction)
        fun deleteTransaction(transaction: Transaction)
    }
}