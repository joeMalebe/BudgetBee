package za.co.sqware.za.budgetbliss.ui.transactions_category

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import za.co.sqware.za.budgetbliss.base.BaseCompletableObserver
import za.co.sqware.za.budgetbliss.base.IBaseView
import za.co.sqware.za.budgetbliss.data.model.domain.TransactionCategory
import za.co.sqware.za.budgetbliss.data.model.domain.TransactionCategoryType
import za.co.sqware.za.budgetbliss.data.repository.IDatabaseRepository
import java.util.logging.Logger

class AddTransactionCategoryPresenter(val transactionsRepository: IDatabaseRepository) :
    ITransactionCategoryMvp.Presenter {
    private lateinit var view: ITransactionCategoryMvp.View

    override fun submitTransactionCategory(
        categoryName: String,
        categoryType: TransactionCategoryType
    ) {
        transactionsRepository
            .insertTransactionCategory(createTransactionCategory(categoryName, categoryType))
            .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
            .subscribe(
                TransactionCategoryObserver(view)
            )
    }

    fun createTransactionCategory(
        categoryName: String,
        categoryType: TransactionCategoryType
    ): TransactionCategory {
        return TransactionCategory(
            0,
            categoryName,
            categoryType.value
        )
    }

    override fun attachView(view: IBaseView) {
        this.view = view as ITransactionCategoryMvp.View
    }

    override fun detachView() {
        Logger.getAnonymousLogger().info("LandingPresenter Stopped")
    }

    class TransactionCategoryObserver(val view: ITransactionCategoryMvp.View) :
        BaseCompletableObserver() {
        override fun onComplete() {
            view.navigateToTransactionCategorySelectScreen()
        }
    }

}