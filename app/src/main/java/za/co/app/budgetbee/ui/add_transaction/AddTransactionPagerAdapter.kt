package za.co.app.budgetbee.ui.add_transaction


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import za.co.app.budgetbee.data.model.domain.TransactionCategoryType
import za.co.app.budgetbee.ui.select_transaction_category.SelectTransactionCategoryFragment

private const val EXPENSE = 0
private const val INCOME = 1
private const val NUMBER_OF_PAGES = 2

class AddTransactionPagerAdapter(fragmentManager: FragmentManager, behavior: Int) :
    FragmentPagerAdapter(fragmentManager, behavior) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            EXPENSE -> SelectTransactionCategoryFragment.newInstance(TransactionCategoryType.EXPENSE)
            INCOME -> SelectTransactionCategoryFragment.newInstance(TransactionCategoryType.INCOME)
            else -> {
                throw Throwable("Position $position is invalid")
            }
        }
    }

    override fun getCount(): Int {
        return NUMBER_OF_PAGES
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            EXPENSE -> "Expense"
            INCOME -> "Income"
            else -> {
                throw Throwable("Position $position is invalid")
            }
        }
    }
}