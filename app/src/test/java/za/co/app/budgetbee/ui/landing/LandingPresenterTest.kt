package za.co.app.budgetbee.ui.landing

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import za.co.app.budgetbee.data.model.database.BudgetBeeDoa
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.data.model.domain.TransactionCategory
import za.co.app.budgetbee.data.model.domain.TransactionCategoryType
import za.co.app.budgetbee.data.repository.IDatabaseRepository
import za.co.app.budgetbee.data.repository.TransactionsRepository
import java.util.*

class LandingPresenterTest {

    private lateinit var presenter: LandingPresenter

    val MARGIN_FOR_ERROR = 0.2
    val budgetBeeDoa: BudgetBeeDoa = mock(BudgetBeeDoa::class.java)
    lateinit var repository: IDatabaseRepository

    fun getMockTransactions(): ArrayList<Transaction> {
        val date = Calendar.getInstance().timeInMillis
        val transaction1 = Transaction(
                date,
                "This is a test 1",
                100.5,
                TransactionCategoryType.INCOME.value,
                "Salary"
        )
        val transaction5 = Transaction(
                date,
                "This is a test 5",
                50.5,
                TransactionCategoryType.EXPENSE.value,
                "Gift"
        )
        val transaction2 = Transaction(
                date,
                "This is a test 2",
                100.5,
                TransactionCategoryType.INCOME.value,
                "Invest"
        )
        val transaction3 = Transaction(
                date,
                "This is a test 3",
                100.5,
                TransactionCategoryType.INCOME.value,
                "Olx sale"
        )
        val transaction4 = Transaction(
                date,
                "This is a test 4",
                50.0,
                TransactionCategoryType.EXPENSE.value,
                "Uber driver"
        )

        return arrayListOf(transaction1, transaction2, transaction3, transaction4, transaction5)
    }

    fun getMockTransactionCategories(): ArrayList<TransactionCategory> {
        val transactionCategory =
                TransactionCategory(0, "Salary", TransactionCategoryType.INCOME.value)
        val transactionCategory2 =
                TransactionCategory(1, "Gift", TransactionCategoryType.EXPENSE.value)
        val transactionCategory3 =
                TransactionCategory(2, "Uber driver", TransactionCategoryType.EXPENSE.value)
        val transactionCategory4 =
                TransactionCategory(3, "Invest", TransactionCategoryType.INCOME.value)
        val transactionCategory5 =
                TransactionCategory(4, "Olx sale", TransactionCategoryType.INCOME.value)

        return arrayListOf(
                transactionCategory,
                transactionCategory2,
                transactionCategory3,
                transactionCategory4,
                transactionCategory5
        )
    }

    @Before
    fun setUp() {
        repository = TransactionsRepository(budgetBeeDoa)
        presenter = LandingPresenter(repository)
    }

    @Test
    fun givenTransactions_and_transactionCategories_calculateTotalIncome() {
        val combinedTransactionAndCategory = LandingPresenter.CombinedTransactionAndCategory(
                getMockTransactions(),
                getMockTransactionCategories()
        )
        val incomeTotal = presenter.calculateTotalIncome(combinedTransactionAndCategory)
        assertEquals(301.5, incomeTotal, MARGIN_FOR_ERROR)
    }

    @Test
    fun givenTransactions_and_transactionCategories_calculateTotalExpenses() {
        val combinedTransactionAndCategory = LandingPresenter.CombinedTransactionAndCategory(
                getMockTransactions(),
                getMockTransactionCategories()
        )
        val incomeTotal = presenter.calculateExpenseTotal(combinedTransactionAndCategory)
        assertEquals(100.5, incomeTotal, MARGIN_FOR_ERROR)
    }

    @Test
    fun givenIncome_and_expenseTotal_calculateBalance() {
        assertEquals(-20.0, presenter.calculateBalance(30.0, 50.0), MARGIN_FOR_ERROR)
        assertEquals(19.8, presenter.calculateBalance(120.5, 100.7), MARGIN_FOR_ERROR)
    }
}