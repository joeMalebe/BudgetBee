package za.co.app.budgetbee.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class TransactionWithCategory(
    @Embedded
    val transactionDataModel: TransactionDataModel,

    @Relation(
        parentColumn = "transactionId",
        entityColumn = "transactionCategoryId"
    )
    val transactionCategoryDataModel: TransactionCategoryDataModel
)