package za.co.app.budgetbee.data.model

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface TransactionCategoryDao {

    @Query("SELECT * FROM TransactionCategory")
    fun getAllTransactionCategories(): Observable<List<TransactionCategory>>

    @Query("SELECT * FROM TransactionCategory WHERE transactionCategoryName LIKE :transactionCategoryName")
    fun getTransactionsCategoryByName(transactionCategoryName: String): Single<TransactionCategory>

    @Insert
    fun addTransationCategory(transactionCategory: TransactionCategory): Completable

    @Delete
    fun removeTransactionCategory(transactionCategory: TransactionCategory): Completable

    @Update
    fun updateTransactionCategory(transactionCategory: TransactionCategory): Completable
}