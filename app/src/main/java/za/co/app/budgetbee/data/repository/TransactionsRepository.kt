package za.co.app.budgetbee.data.repository

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import za.co.app.budgetbee.data.mapper.TransactionsMapper.Companion.mapModelListToTransactionCategoryList
import za.co.app.budgetbee.data.mapper.TransactionsMapper.Companion.mapModelListToTransactionsList
import za.co.app.budgetbee.data.mapper.TransactionsMapper.Companion.mapTransactionToModel
import za.co.app.budgetbee.data.model.database.BudgetBeeDoa
import za.co.app.budgetbee.data.model.database.TransactionCategoryDataModel
import za.co.app.budgetbee.data.model.database.TransactionDataModel
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.data.model.domain.TransactionCategory
import za.co.app.budgetbee.data.model.domain.TransactionCategoryType


class TransactionsRepository(private val budgetBeeDoa: BudgetBeeDoa) : IDatabaseRepository {
    private val TAG = TransactionsRepository::class.simpleName

    override fun getAllTransactionCategories(): Observable<ArrayList<TransactionCategory>> {
        return this.budgetBeeDoa
                .getAllTransactionCategories().map { modelList ->
                    Log.d(TAG, "mapping Model To TransactionCategory")
                    mapModelListToTransactionCategoryList(modelList)
                }
    }

    override fun getAllTransactionCategoriesByType(transactionCategoryType: Int): Observable<ArrayList<TransactionCategory>> {
        return this.budgetBeeDoa
                .getAllTransactionCategoriesByType(transactionCategoryType).map { modelList ->
                    Log.d(TAG, "mapping Model To TransactionCategory")
                    mapModelListToTransactionCategoryList(modelList)
                }
    }

    override fun getTransactions(): Observable<ArrayList<Transaction>> {
        return budgetBeeDoa.getTransactions().map { modelIst ->
            mapModelListToTransactionsList(modelIst)
        }
    }

    override fun getTransactionsByDateRange(dateRange: Pair<Long, Long>): Observable<ArrayList<Transaction>> {
        return budgetBeeDoa.getTransactionsByDate(dateRange.first, dateRange.second).map { modelIst ->
            mapModelListToTransactionsList(modelIst)
        }
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
                        transaction.transactionCategoryName,
                        transaction.transactionCategoryType
                )
        )
    }

    fun getTransactionsByCategoryType(
            transactionCategory: ArrayList<TransactionCategory>,
            transactions: ArrayList<Transaction>, transactionCategoryType: TransactionCategoryType
    ): List<Transaction> {
        return transactions.filter { transaction ->
            transactionCategory.filter { transactionCategory -> transactionCategory.transactionCategoryType == transactionCategoryType.value }
                    .map { transactionCategory -> transactionCategory.transactionCategoryId }
                    .contains(transaction.transactionCategoryId)
        }
    }

    override fun deleteTransaction(transaction: Transaction): Completable {
        return budgetBeeDoa.deleteTransaction(transaction.transactionId)
    }

    override fun updateTransaction(transaction: Transaction): Completable {
        return budgetBeeDoa.updateTransaction(mapTransactionToModel(transaction))
    }
}