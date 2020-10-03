package za.co.app.budgetbee.ui.transaction

import za.co.app.budgetbee.base.IBaseView

class TransactionPresenter : ITransactionMvp.Presenter{
    lateinit var view: ITransactionMvp.View
    override fun attachView(view: IBaseView) {
        this.view = view as ITransactionMvp.View
    }

    override fun detachView() {
        this.view
    }
}