package za.co.app.budgetbee.data.model.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    foreignKeys = arrayOf(
        ForeignKey(
            entity = TransactionCategoryDataModel::class,
            parentColumns = arrayOf("transactionCategoryId"),
            childColumns = arrayOf("transactionCategoryId"),
            onDelete = ForeignKey.CASCADE
        )
    )
)
data class TransactionDataModel(
    val transactionDate: Long,
    val transactionDescription: String,
    val transactionAmount: Double,
    val transactionCategoryId: Int
) {
    @PrimaryKey(autoGenerate = true)
    var transactionId: Int = 0
}