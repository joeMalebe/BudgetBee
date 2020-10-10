package za.co.app.budgetbee.data.mapper

import za.co.app.budgetbee.data.model.database.TransactionCategoryDataModel
import za.co.app.budgetbee.data.model.database.TransactionDataModel
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.data.model.domain.TransactionCategory

class TransactionsMapper {

    companion object {
        fun mapModelListToTransactionsList(modelIst: List<TransactionDataModel>): ArrayList<Transaction> {
            val transactionsList: ArrayList<Transaction> = arrayListOf()
            modelIst.forEach { model ->
                transactionsList.add(
                        mapModelToTransaction(model)
                )
            }
            return transactionsList
        }

        fun mapModelToTransaction(model: TransactionDataModel): Transaction {
            return Transaction(
                    model.transactionId,
                    model.transactionDate,
                    model.transactionDescription,
                    model.transactionAmount,
                    model.transactionCategoryId,
                    model.transactionCategoryName
            )
        }

        fun mapTransactionToModel(transaction: Transaction): TransactionDataModel {
            return TransactionDataModel(
                    transaction.transactionDate,
                    transaction.transactionDescription,
                    transaction.transactionAmount,
                    transaction.transactionCategoryId,
                    transaction.transactionCategoryName
            )
        }

        fun mapModelListToTransactionCategoryList(modelList: List<TransactionCategoryDataModel>): ArrayList<TransactionCategory> {
            val transactionCategoryList = arrayListOf<TransactionCategory>()
            modelList.forEach { model ->
                transactionCategoryList.add(
                        mapModelToTransactionCategory(model)
                )
            }
            return transactionCategoryList
        }

        private fun mapModelToTransactionCategory(model: TransactionCategoryDataModel): TransactionCategory {
            return TransactionCategory(
                    model.transactionCategoryId,
                    model.transactionCategoryName,
                    model.transactionCategoryType
            )
        }
    }
}