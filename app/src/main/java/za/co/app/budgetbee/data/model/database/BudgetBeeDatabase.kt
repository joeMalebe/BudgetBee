package za.co.app.budgetbee.data.model.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import za.co.app.budgetbee.base.BaseCompletableObserver
import za.co.app.budgetbee.data.model.domain.TransactionCategoryType
import za.co.app.budgetbee.utils.SingletonHolder
import java.io.IOException
import java.io.InputStream
import java.lang.ref.WeakReference
import java.nio.charset.Charset

const val EXPENSES = "expenses"
const val INCOME = "income"
@Database(
        entities = [TransactionCategoryDataModel::class, TransactionDataModel::class],
        version = 1
)
abstract class BudgetBeeDatabase : RoomDatabase() {
    abstract fun getTransactionCategoryDao(): BudgetBeeDoa

    companion object : SingletonHolder<BudgetBeeDatabase, Context>({
        val databaseName = "BudgetBeeDatabase.db"
        Room.databaseBuilder(it.applicationContext, BudgetBeeDatabase::class.java, databaseName)
                .addCallback(InitialiseDatabase(it.applicationContext))
                //.fallbackToDestructiveMigration()// TODO: 10/12/2020 AD Remove this when going live https://stackoverflow.com/questions/44197309/room-cannot-verify-the-data-integrity
                .build()
    })
}

class InitialiseDatabase(val context: Context) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        val transactionCategories = getTransactionCategories(context)
        BudgetBeeDatabase.getInstance(context)
                .getTransactionCategoryDao()
                .insertAllTransactionCategory(
                        getCategoryDataModel(transactionCategories?.first, TransactionCategoryType.EXPENSE) +
                                getCategoryDataModel(transactionCategories?.second, TransactionCategoryType.INCOME)
                )
                .subscribeOn(Schedulers.io())
                .subscribe(TransactionCategoryObserver(context))
    }

    private fun getCategoryDataModel(category: ArrayList<String>?, transactionCategoryType: TransactionCategoryType): ArrayList<TransactionCategoryDataModel> {
        val list = arrayListOf<TransactionCategoryDataModel>()
        return if (category == null) list else {
            category.forEach { item -> list.add(TransactionCategoryDataModel(item, transactionCategoryType.value)) }
            return list
        }
    }

    private fun loadJSONFromAsset(context: Context): String? {
        val json: String?
        json = try {
            val inputStream: InputStream = context.assets.open("transactionCategories.json")
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charset.defaultCharset())
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    private fun getTransactionCategories(context: Context): Pair<ArrayList<String>, ArrayList<String>>? {
        return try {
            val transactionsCategories = JSONObject(loadJSONFromAsset(context) ?: "{ $EXPENSES: [], $INCOME: [] }")
            val expenses = transactionsCategories.getJSONArray(EXPENSES)
            val income = transactionsCategories.getJSONArray(INCOME)
            Log.d("$EXPENSES --> ", expenses.toString())
            Log.d("$INCOME --> ", income.toString())

            val expenseList = getCategoryItems(expenses)
            val incomeList = getCategoryItems(income)

            Pair(expenseList, incomeList)
        } catch (e: JSONException) {
            e.printStackTrace()
            null
        }
    }

    private fun getCategoryItems(categories: JSONArray): ArrayList<String> {
        val list = arrayListOf<String>()

        for (i in 0 until categories.length()) {
            val expenseCategory = categories.getString(i)
            list.add(expenseCategory)
        }
        return list
    }
}

class TransactionCategoryObserver(context: Context) :
        BaseCompletableObserver() {
    private val activity = WeakReference(context).get()
    override fun onComplete() {
        if (activity != null) {
            Log.i("InitialiseDatabase", "Oncomplete(Added Transaction categories")
        }
    }
}