package za.co.app.budgetbee.model

import androidx.room.*

@Dao
interface TransactionCategoryDao {

    @Query("SELECT * FROM TransactionCategory")
    fun getAllTransactions(): List<TransactionCategory>

    @Insert
    fun addTransation(transactionCategory: TransactionCategory)

    @Delete
    fun removeTransaction(transactionCategory: TransactionCategory)

    @Update
    fun updateTransaction(transactionCategory: TransactionCategory)
}