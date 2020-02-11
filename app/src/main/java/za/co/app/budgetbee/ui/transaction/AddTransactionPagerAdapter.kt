package za.co.app.budgetbee.ui.transaction


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

private const val INCOME = 0
private const val EXPENSE = 1
private const val NUMBER_OF_PAGES = 2

class AddTransactionPagerAdapter(fragmentManager: FragmentManager, behavior: Int) :
    FragmentPagerAdapter(fragmentManager, behavior) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            EXPENSE -> AddExpenseFragment.newInstance()
            INCOME -> AddTransactionFragment.newInstance()
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