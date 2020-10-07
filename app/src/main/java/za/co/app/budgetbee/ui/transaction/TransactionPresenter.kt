package za.co.app.budgetbee.ui.transaction

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import za.co.app.budgetbee.base.IBaseView
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.data.repository.IDatabaseRepository

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
                }, { error -> Log.e("deleteTransaction", error.message) }).let { compositeDisposable.add(it) }
    }

    override fun detachView() {
        this.view
        compositeDisposable.dispose()
    }
}