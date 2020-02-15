package za.co.app.budgetbee.data.repository

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import za.co.app.budgetbee.data.model.database.BudgetBeeDoa
import za.co.app.budgetbee.data.model.database.TransactionCategoryDataModel
import za.co.app.budgetbee.data.model.database.TransactionDataModel
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.data.model.domain.TransactionCategory


class TransactionsRepository(private val budgetBeeDoa: BudgetBeeDoa) : IDatabaseRepository {
    private val TAG = TransactionsRepository::class.simpleName

    override fun getAllTransactionCategories(): Observable<ArrayList<TransactionCategory>> {
        return this.budgetBeeDoa
            .getAllTransactionCategories().map { modelList ->
                Log.d(TAG, "mapping Model To TransactionCategory")
                mapModelToTransactionCategory(modelList)
            }
    }

    override fun getAllTransactionCategoriesByType(transactionCategoryType: Int): Observable<ArrayList<TransactionCategory>> {
        return this.budgetBeeDoa
            .getAllTransactionCategoriesByType(transactionCategoryType).map { modelList ->
                Log.d(TAG, "mapping Model To TransactionCategory")
                mapModelToTransactionCategory(modelList)
            }
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

    override fun getTransactions(): Observable<ArrayList<Transaction>> {
        return budgetBeeDoa.getTransactions().map { modelIst ->
            mapModelToTransactions(modelIst)
        }
    }

    private fun mapModelToTransactions(modelIst: List<TransactionDataModel>): ArrayList<Transaction> {
        val transactionsList: ArrayList<Transaction> = arrayListOf()
        modelIst.forEach { model ->
            transactionsList.add(
                Transaction(
                    model.transactionDate,
                    model.transactionDescription,
                    model.transactionAmount,
                    model.transactionCategoryId,
                    model.transactionCategoryName
                )
            )
        }
        return transactionsList
    }

    override fun getTransactionsCategoryByName(transactionCategoryName: String): Single<TransactionCategory> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insertTransactionCategory(transactionCategory: TransactionCategory): Completable {
        return budgetBeeDoa
            .insertTransactionCategory(
                TransactionCategoryDataModel(
                    transactionCategory.transactionCategoryName,
                    transactionCategory.transactionCategoryType
                )
            )
    }

    override fun insertTransaction(transaction: Transaction): Completable {
        return budgetBeeDoa.insertTransaction(
            TransactionDataModel(
                transaction.transactionDate,
                transaction.transactionDescription,
                transaction.transactionAmount,
                transaction.transactionCategoryId,
                transaction.transactionCategoryName
            )
        )
    }
}