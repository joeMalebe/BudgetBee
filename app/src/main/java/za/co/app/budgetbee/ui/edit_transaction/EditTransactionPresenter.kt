package za.co.app.budgetbee.ui.edit_transaction

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import za.co.app.budgetbee.base.IBaseView
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.data.repository.IDatabaseRepository

class EditTransactionPresenter(val transactionsRepository: IDatabaseRepository) : IEditTransactionMvp.Presenter {

    private lateinit var view: IEditTransactionMvp.View
    private val compositeDisposable = CompositeDisposable()
    override fun updateTransaction(transaction: Transaction) {
        view.showLoading()
        transactionsRepository.updateTransaction(transaction)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { view.dismissLoading() }
                .subscribe({ view.updateSuccessful() }, { error -> view.updateError(error) })
                .let { compositeDisposable.add(it) }

    }

    override fun attachView(view: IBaseView) {
        this.view = view as IEditTransactionMvp.View
    }

    override fun detachView() {
        compositeDisposable.dispose()
        Log.i("EditPresenter", "detachView: ")
    }

}
