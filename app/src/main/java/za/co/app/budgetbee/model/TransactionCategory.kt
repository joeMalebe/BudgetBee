package za.co.app.budgetbee.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TransactionCategory(
    @PrimaryKey(autoGenerate = true) val transactionId: Int, @ColumnInfo(name = "transaction_name") val transactionName: String,
    @ColumnInfo(name = "transaction_type") val transactionType: Int
)