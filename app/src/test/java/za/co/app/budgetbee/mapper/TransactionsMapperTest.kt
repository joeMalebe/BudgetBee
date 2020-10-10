package za.co.app.budgetbee.mapper

import org.junit.Assert.assertEquals
import org.junit.Test
import za.co.app.budgetbee.data.mapper.TransactionsMapper
import za.co.app.budgetbee.data.model.database.TransactionDataModel
import za.co.app.budgetbee.data.model.domain.Transaction

class TransactionsMapperTest {

    @Test
    fun mapTransactionToModelTest() {
        val transaction = Transaction(123, "test", 12.2, 3, "Salary")
        val model = TransactionsMapper.mapTransactionToModel(transaction)
        assertEquals(12.2, model.transactionAmount,0.4)
        assertEquals(3, model.transactionCategoryId)
        assertEquals(123, model.transactionDate)
        assertEquals("test", model.transactionDescription)
        assertEquals("Salary", model.transactionCategoryName)
    }

    @Test
    fun mapModelToTransactionTest() {
        val dataModel = TransactionDataModel(123, "test", 12.2, 3, "Salary")
        val transaction = TransactionsMapper.mapModelToTransaction(dataModel)
        assertEquals(12.2, transaction.transactionAmount,0.4)
        assertEquals(3, transaction.transactionCategoryId)
        assertEquals(123, transaction.transactionDate)
        assertEquals("test", transaction.transactionDescription)
        assertEquals("Salary", transaction.transactionCategoryName)
    }
}