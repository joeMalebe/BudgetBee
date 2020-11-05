package za.co.app.budgetbee.data.model.database

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface BudgetBeeDoa {

    @Query("SELECT * FROM TransactionCategoryDataModel")
    fun getAllTransactionCategories(): Observable<List<TransactionCategoryDataModel>>

    @Query("SELECT * FROM TransactionCategoryDataModel WHERE TransactionCategoryDataModel.transactionCategoryType = :transactionCategoryType")
    fun getAllTransactionCategoriesByType(transactionCategoryType: Int): Observable<List<TransactionCategoryDataModel>>

    @Query("SELECT * FROM TransactionDataModel,TransactionCategoryDataModel WHERE TransactionDataModel.transactionCategoryId = TransactionCategoryDataModel.transactionCategoryId")
    fun getTransactions(): Observable<List<TransactionDataModel>>

    @Query("SELECT * FROM TransactionDataModel,TransactionCategoryDataModel WHERE TransactionDataModel.transactionDate >= :transactionMinDate AND TransactionDataModel.transactionDate <= :transactionMaxDate AND TransactionDataModel.transactionCategoryId = TransactionCategoryDataModel.transactionCategoryId")
    fun getTransactionsByDate(transactionMinDate: Long, transactionMaxDate: Long): Observable<List<TransactionDataModel>>

    @Query("SELECT * FROM TransactionCategoryDataModel WHERE transactionCategoryName LIKE :transactionCategoryName")
    fun getTransactionsCategoryByName(transactionCategoryName: String): Single<TransactionCategoryDataModel>

    @Insert
    fun insertTransactionCategory(transactionCategoryDataModel: TransactionCategoryDataModel): Completable

    @Insert
    fun insertAllTransactionCategory(transactionCategoryDataModel: Array<TransactionCategoryDataModel>): Completable

    @Insert
    fun insertTransaction(transaction: TransactionDataModel): Completable

    @Delete
    fun deleteTransactionCategory(transactionCategoryDataModel: TransactionCategoryDataModel): Completable

    @Query("DELETE FROM TransactionDataModel WHERE TransactionDataModel.transactionId = :transactionId")
    fun deleteTransaction(transactionId: Int): Completable

    @Update
    fun updateTransactionCategory(transactionCategoryDataModel: TransactionCategoryDataModel): Completable

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTransaction(transactionDataModel: TransactionDataModel): Completable
}