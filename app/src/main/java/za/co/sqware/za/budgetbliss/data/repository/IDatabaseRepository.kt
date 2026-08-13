package za.co.sqware.za.budgetbliss.data.repository

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import za.co.sqware.za.budgetbliss.data.model.domain.Transaction
import za.co.sqware.za.budgetbliss.data.model.domain.TransactionCategory

interface IDatabaseRepository {
    fun getAllTransactionCategories(): Observable<ArrayList<TransactionCategory>>

    fun getAllTransactionCategoriesByType(transactionCategoryType: Int): Observable<ArrayList<TransactionCategory>>

    fun getTransactions(): Observable<ArrayList<Transaction>>

    fun getTransactionsByDateRange(dateRange: Pair<Long, Long>): Observable<ArrayList<Transaction>>

    fun getTransactionsCategoryByName(transactionCategoryName: String): Single<TransactionCategory>

    fun insertTransactionCategory(transactionCategory: TransactionCategory): Completable

    fun insertTransaction(transaction: Transaction): Completable

    fun deleteTransaction(transaction: Transaction): Completable

    fun updateTransaction(transaction: Transaction): Completable
}