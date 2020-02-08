package za.co.app.budgetbee.data.model.repository

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import za.co.app.budgetbee.BudgetBeeApplication
import za.co.app.budgetbee.data.model.Transaction
import za.co.app.budgetbee.data.model.TransactionCategory
import za.co.app.budgetbee.data.model.database.BudgetBeeDatabase
import za.co.app.budgetbee.data.model.database.TransactionCategoryDataModel

class DatabaseRepository : IDatabaseRepository {
    override fun getAllTransactionCategories(): Observable<ArrayList<TransactionCategory>> {
        return BudgetBeeDatabase.getInstance(BudgetBeeApplication.getContext())
            .getTransactionCategoryDao()
            .getAllTransactionCategories().map { modelList ->
                mapModelToTransactionCategory(modelList)
            }
            .subscribeOn(
                Schedulers.io()
            ).observeOn(AndroidSchedulers.mainThread())

    }

    private fun mapModelToTransactionCategory(modelList: List<TransactionCategoryDataModel>): ArrayList<TransactionCategory> {
        val transactionCategoryList = arrayListOf<TransactionCategory>()
        modelList.forEach { model ->
            transactionCategoryList.add(
                TransactionCategory(
                    model.transactionCategoryId,
                    model.transactionCategoryName,
                    model.transactionCategoryType
                )
            )
        }
        return transactionCategoryList
    }

    override fun getTransactions(): Observable<List<Transaction>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTransactionsCategoryByName(transactionCategoryName: String): Single<TransactionCategory> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insertTransactionCategory(transactionCategory: TransactionCategory): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insertAllTransactionCategory(transactionCategory: Array<TransactionCategory>): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insertTransaction(transaction: Transaction): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteTransactionCategory(transactionCategory: TransactionCategory): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateTransactionCategory(transactionCategory: TransactionCategory): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}