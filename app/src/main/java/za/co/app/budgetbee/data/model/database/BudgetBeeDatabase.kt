package za.co.app.budgetbee.data.model.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import io.reactivex.schedulers.Schedulers
import za.co.app.budgetbee.base.BaseCompletableObserver
import za.co.app.budgetbee.data.model.domain.TransactionCategoryType
import za.co.app.budgetbee.utils.SingletonHolder
import java.lang.ref.WeakReference

@Database(
    entities = [TransactionCategoryDataModel::class, TransactionDataModel::class],
    version = 1
)
abstract class BudgetBeeDatabase : RoomDatabase() {
    abstract fun getTransactionCategoryDao(): BudgetBeeDoa

    companion object : SingletonHolder<BudgetBeeDatabase, Context>({
        val databaseName = "BudgetBeeDatabsae.db"
        Room.databaseBuilder(
            it.applicationContext,
            BudgetBeeDatabase::class.java, databaseName
        ).addCallback(
            InitialiseDatabase(it.applicationContext)
        )
            .build()
    })
}

class InitialiseDatabase(val context: Context) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {

        BudgetBeeDatabase.getInstance(context).getTransactionCategoryDao()
            .insertAllTransactionCategory(
                arrayOf(
                    TransactionCategoryDataModel(
                        "Salary",
                        TransactionCategoryType.INCOME.value
                    ),
                    TransactionCategoryDataModel(
                        "Loan",
                        TransactionCategoryType.EXPENSE.value
                    )
                )
            ).subscribeOn(Schedulers.io()).subscribe(
                TransactionCategoryObserver(
                    context
                )
            )
    }
}

class TransactionCategoryObserver(context: Context) :
    BaseCompletableObserver() {
    val activity = WeakReference(context).get()
    override fun onComplete() {
        if (activity != null) {
            Log.i("InitialiseDatabase", "Oncomplete(Added Transaction categories")
        }
    }
}