package za.co.app.budgetbee.ui.edit_transaction

import za.co.app.budgetbee.base.IBasePresenter
import za.co.app.budgetbee.base.ILoadableView
import za.co.app.budgetbee.data.model.domain.Transaction

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