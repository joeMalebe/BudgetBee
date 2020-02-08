package za.co.app.budgetbee.data.model.repository

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import za.co.app.budgetbee.data.model.Transaction
import za.co.app.budgetbee.data.model.TransactionCategory

interface IDatabaseRepository {
    fun getAllTransactionCategories(): Observable<ArrayList<TransactionCategory>>

    fun getTransactions(): Observable<List<Transaction>>

    fun getTransactionsCategoryByName(transactionCategoryName: String): Single<TransactionCategory>

    fun insertTransactionCategory(transactionCategory: TransactionCategory): Completable

    fun insertAllTransactionCategory(transactionCategory: Array<TransactionCategory>): Completable

    fun insertTransaction(transaction: Transaction): Completable

    fun deleteTransactionCategory(transactionCategory: TransactionCategory): Completable

    fun updateTransactionCategory(transactionCategory: TransactionCategory): Completable
}