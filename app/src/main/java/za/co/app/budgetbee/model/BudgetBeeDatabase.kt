package za.co.app.budgetbee.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(TransactionCategory::class), version = 1)
abstract class BudgetBeeDatabase : RoomDatabase() {
    abstract fun transactionCategoryDao(): TransactionCategoryDao
}