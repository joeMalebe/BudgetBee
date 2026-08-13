package za.co.sqware.za.budgetbliss.ui.add_transaction

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import za.co.sqware.za.budgetbliss.base.BaseCompletableObserver
import za.co.sqware.za.budgetbliss.base.IBaseView
import za.co.sqware.za.budgetbliss.data.model.domain.Transaction
import za.co.sqware.za.budgetbliss.data.repository.IDatabaseRepository
import java.util.logging.Logger

class AddTransactionPresenter(val transactionsRepository: IDatabaseRepository) :
        IAddTransactionMvp.Presenter {

    private lateinit var view: IAddTransactionMvp.View

    override fun submitTransaction(
            transactionDate: Long,
            transactionDescription: String,
            transactionAmount: Double,
            transactionCategoryName: String,
            transactionCategoryId: Int,
            transactionCategoryType: Int
    ) {
        transactionsRepository.insertTransaction(
                Transaction(
                        transactionDate,
                        transactionDescription,
                        transactionAmount,
                        transactionCategoryId,
                        transactionCategoryName,
                        transactionCategoryType
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