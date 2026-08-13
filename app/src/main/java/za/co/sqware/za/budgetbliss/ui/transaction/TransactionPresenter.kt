package za.co.sqware.za.budgetbliss.ui.transaction

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import za.co.sqware.za.budgetbliss.base.IBaseView
import za.co.sqware.za.budgetbliss.data.model.domain.Transaction
import za.co.sqware.za.budgetbliss.data.repository.IDatabaseRepository

class TransactionPresenter(val transactionsRepository: IDatabaseRepository) : ITransactionMvp.Presenter {
    lateinit var view: ITransactionMvp.View
    val compositeDisposable = CompositeDisposable()

    override fun attachView(view: IBaseView) {
        this.view = view as ITransactionMvp.View
    }

    override fun deleteTransaction(transaction: Transaction) {
        view.showLoading()
        transactionsRepository.deleteTransaction(transaction)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.dismissLoading()
                    view.navigateToLanding(transaction.transactionDate)
                }, { error -> Log.e("deleteTransaction", error.message ?: "error") }).let { compositeDisposable.add(it) }
    }

    override fun detachView() {
        this.view
        compositeDisposable.dispose()
    }
}