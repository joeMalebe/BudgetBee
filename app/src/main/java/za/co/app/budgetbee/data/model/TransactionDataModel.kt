package za.co.app.budgetbee.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TransactionDataModel(
    val transactionDate: Long,
    val transactionDescription: String,
    val transactionAmount: Double,
    val transactionType: Int
) {
    @PrimaryKey(autoGenerate = true)
    val transactionId: Int = 0
}