package za.co.app.budgetbee.ui.transaction

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import za.co.app.budgetbee.base.BaseCompletableObserver
import za.co.app.budgetbee.base.IBaseView
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.data.repository.IDatabaseRepository
import java.util.logging.Logger

class TransactionPresenter(val transactionsRepository: IDatabaseRepository) :
        ITransactionMvp.Presenter {

    private lateinit var view: ITransactionMvp.View

    override fun submitTransaction(
            transactionDate: Long,
            transactionDescription: String,
            transactionAmount: Double,
            transactionCategoryName: String,
            transactionCategoryId: Int
    ) {
        transactionsRepository.insertTransaction(
                Transaction(
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
        this.view = view as ITransactionMvp.View
    }

    override fun detachView() {
        Logger.getAnonymousLogger().info("detach View")
    }

    class TransactionObserver(val view: ITransactionMvp.View, val transactionDate: Long) :
            BaseCompletableObserver() {
        override fun onComplete() {
            Logger.getAnonymousLogger().info("TransactionObserver, OnComplete Added Transaction")
            view.navigateToLanding(transactionDate)
        }
    }

}