package za.co.app.budgetbee.ui


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

private const val EXPENSE = 0
private const val INCOME = 1
private const val NUMBER_OF_PAGES = 2

class AddTransactionPagerAdapter(fragmentManager: FragmentManager, val behavior: Int) :
    FragmentPagerAdapter(fragmentManager, behavior) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            EXPENSE -> AddExpenseFragment.newInstance()
            INCOME -> AddIncomeFragment.newInstance()
            else -> {
                throw IllegalArgumentException("Position $position is invalid")
            }
        }
    }

    override fun getCount(): Int {
        return NUMBER_OF_PAGES
    }

}