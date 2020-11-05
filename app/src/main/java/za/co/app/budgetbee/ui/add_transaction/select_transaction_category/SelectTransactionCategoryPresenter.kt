package za.co.app.budgetbee.ui.add_transaction.select_transaction_category

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import za.co.app.budgetbee.base.BaseObserver
import za.co.app.budgetbee.base.IBaseView
import za.co.app.budgetbee.data.model.domain.TransactionCategory
import za.co.app.budgetbee.data.model.domain.TransactionCategoryType
import za.co.app.budgetbee.data.repository.IDatabaseRepository
import java.util.logging.Logger

class SelectTransactionCategoryPresenter(val transactionsRepository: IDatabaseRepository) : ISelectTransactionCategoryMvp.Presenter {
    private lateinit var view: ISelectTransactionCategoryMvp.View

    override fun getAllTransactionCategoriesByType(transactionCategoryType: TransactionCategoryType) {
        transactionsRepository.getAllTransactionCategoriesByType(transactionCategoryType.value)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(TransactionCategoryObserver(view))
    }

    override fun attachView(view: IBaseView) {
        this.view = view as ISelectTransactionCategoryMvp.View
    }

    override fun detachView() {
        Logger.getAnonymousLogger().info("SelectTransactionCategoryPresenter Stopped")
    }

    class TransactionCategoryObserver(val view: ISelectTransactionCategoryMvp.View) :
            BaseObserver<ArrayList<TransactionCategory>>() {

        override fun onNext(value: ArrayList<TransactionCategory>) {
            view.dismissLoading()
            view.displayCategories(value)
        }

        override fun onError(error: Throwable) {
            view.dismissLoading()
            view.showError()
        }
    }
}