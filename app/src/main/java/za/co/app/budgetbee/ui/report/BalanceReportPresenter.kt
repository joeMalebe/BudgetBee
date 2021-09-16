package za.co.app.budgetbee.ui.report

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import za.co.app.budgetbee.base.BasePresenterObserver
import za.co.app.budgetbee.base.IBaseView
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.data.repository.IDatabaseRepository
import java.util.logging.Logger

class BalanceReportPresenter(val transactionsRepository: IDatabaseRepository) : IBalanceReportMvp.Presenter {
    private lateinit var view: IBalanceReportMvp.View

    override fun getTransactions() {
        view.showLoading()
        transactionsRepository.getTransactions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(TransactionsObserver(this.view))
    }

    override fun attachView(view: IBaseView) {
        this.view = view as IBalanceReportMvp.View
    }

    override fun detachView() {
        Logger.getAnonymousLogger().info("detach View")
    }

    internal class TransactionsObserver(override val view: IBalanceReportMvp.View) : BasePresenterObserver<ArrayList<Transaction>>(view) {
        override fun onNext(value: ArrayList<Transaction>) {
            view.displayTransactions(value)
        }
    }
}