package za.co.app.budgetbee.data.model

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface TransactionCategoryDao {

    @Query("SELECT * FROM TransactionCategoryDataModel")
    fun getAllTransactionCategories(): Observable<List<TransactionCategoryDataModel>>

    @Query("SELECT * FROM TransactionCategoryDataModel WHERE transactionCategoryName LIKE :transactionCategoryName")
    fun getTransactionsCategoryByName(transactionCategoryName: String): Single<TransactionCategoryDataModel>

    @Insert
    fun addTransactionCategory(transactionCategoryDataModel: TransactionCategoryDataModel): Completable

    @Insert
    fun addAllTransactionCategory(transactionCategoryDataModel: Array<TransactionCategoryDataModel>): Completable

    @Delete
    fun removeTransactionCategory(transactionCategoryDataModel: TransactionCategoryDataModel): Completable

    @Update
    fun updateTransactionCategory(transactionCategoryDataModel: TransactionCategoryDataModel): Completable
}