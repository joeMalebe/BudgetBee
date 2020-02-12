package za.co.app.budgetbee.ui.landing

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import za.co.app.budgetbee.base.BaseObserver
import za.co.app.budgetbee.base.IBaseView
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.data.repository.TransactionsRepository
import java.util.logging.Logger

class LandingPresenter(
    val transactionsRepository: TransactionsRepository
) : ILandingMvp.Presenter {
    private lateinit var view: ILandingMvp.View

    override fun getTransactions() {
        transactionsRepository.getTransactions().observeOn(
            AndroidSchedulers.mainThread()
        ).subscribeOn(Schedulers.io()).subscribe(
            TransactionsObserver(
                view
            )
        )
    }

    override fun attachView(view: IBaseView) {
        this.view = view as ILandingMvp.View
    }

    override fun detachView() {
        Logger.getAnonymousLogger().info("LandingPresenter Stopped")
    }

    class TransactionsObserver(val view: ILandingMvp.View) :
        BaseObserver<ArrayList<Transaction>>() {
        override fun onNext(value: ArrayList<Transaction>) {
            if (value.isNotEmpty()) {
                Logger.getAnonymousLogger().info(
                    "getAnonymousLogger TransactionsObserver size ${value.size}- ${value[(value.size - 1)]}"
                )
            }
            view.displayTransactions(value)
        }

        override fun onError(error: Throwable) {
            super.onError(error)
            Logger.getAnonymousLogger().info(error.stackTrace.toString())
            view.showError(error)
        }
    }
}