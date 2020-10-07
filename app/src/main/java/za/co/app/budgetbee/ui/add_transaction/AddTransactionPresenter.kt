package za.co.app.budgetbee.ui.add_transaction

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import za.co.app.budgetbee.base.BaseCompletableObserver
import za.co.app.budgetbee.base.IBaseView
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.data.repository.IDatabaseRepository
import java.util.logging.Logger

class AddTransactionPresenter(val transactionsRepository: IDatabaseRepository) :
        IAddTransactionMvp.Presenter {

    private lateinit var view: IAddTransactionMvp.View

    override fun submitTransaction(
            transactionDate: Long,
            transactionDescription: String,
            transactionAmount: Double,
            transactionCategoryName: String,
            transactionCategoryId: Int
    ) {
        transactionsRepository.insertTransaction(
                Transaction(0,
                        transactionDate,
                        transactionDescription,
                        transactionAmount,
                        transactionCategoryId,
                        transactionCategoryName
                )
        ).observeOn(
                AndroidSchedulers.mainThread()
        ).subscribeOn(Schedulers.io()).subscribe(
                TransactionObserver(view, transactionDate)
        )
    }

    override fun attachView(view: IBaseView) {
        this.view = view as IAddTransactionMvp.View
    }

    override fun detachView() {
        Logger.getAnonymousLogger().info("detach View")
    }

    class TransactionObserver(val view: IAddTransactionMvp.View, val transactionDate: Long) :
            BaseCompletableObserver() {
        override fun onComplete() {
            Logger.getAnonymousLogger().info("TransactionObserver, OnComplete Added Transaction")
            view.navigateToLanding(transactionDate)
        }
    }

}