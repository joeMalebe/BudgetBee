package za.co.app.budgetbee.data.mapper

import za.co.app.budgetbee.data.model.database.TransactionCategoryDataModel
import za.co.app.budgetbee.data.model.database.TransactionDataModel
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.data.model.domain.TransactionCategory

class TransactionsMapper {

    companion object {
        fun mapModelToTransactions(modelIst: List<TransactionDataModel>): ArrayList<Transaction> {
            val transactionsList: ArrayList<Transaction> = arrayListOf()
            modelIst.forEach { model ->
                transactionsList.add(
                        Transaction(
                                model.transactionId,
                                model.transactionDate,
                                model.transactionDescription,
                                model.transactionAmount,
                                model.transactionCategoryId,
                                model.transactionCategoryName
                        )
                )
            }
            return transactionsList
        }

        fun mapModelToTransactionCategory(modelList: List<TransactionCategoryDataModel>): ArrayList<TransactionCategory> {
            val transactionCategoryList = arrayListOf<TransactionCategory>()
            modelList.forEach { model ->
                transactionCategoryList.add(
                        TransactionCategory(
                                model.transactionCategoryId,
                                model.transactionCategoryName,
                                model.transactionCategoryType
                        )
                )
            }
            return transactionCategoryList
        }
    }
}