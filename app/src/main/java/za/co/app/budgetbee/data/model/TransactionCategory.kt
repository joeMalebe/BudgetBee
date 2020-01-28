package za.co.app.budgetbee.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TransactionCategory(
    val transactionCategoryName: String,
    val transactionCategoryType: Int
) {
    @PrimaryKey(autoGenerate = true)
    var transactionCategoryId = 0
}