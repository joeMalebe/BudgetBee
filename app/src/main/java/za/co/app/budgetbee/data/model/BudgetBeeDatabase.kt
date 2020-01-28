package za.co.app.budgetbee.data.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import za.co.app.budgetbee.utils.SingletonHolder

@Database(entities = arrayOf(TransactionCategory::class), version = 1)
abstract class BudgetBeeDatabase : RoomDatabase() {
    abstract fun getTransactionCategoryDao(): TransactionCategoryDao

    companion object : SingletonHolder<BudgetBeeDatabase, Context>({
        Room.databaseBuilder(
            it.applicationContext,
            BudgetBeeDatabase::class.java, "BudgetBeeDatabase"
        ).build()
    })
}