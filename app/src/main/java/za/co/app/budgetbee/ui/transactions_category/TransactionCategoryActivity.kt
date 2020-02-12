package za.co.app.budgetbee.ui.transactions_category

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import kotlinx.android.synthetic.main.activity_transaction_category.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.base.AppCompatBaseActivity
import za.co.app.budgetbee.ui.transaction.AddTransactionPagerAdapter

class TransactionCategoryActivity : AppCompatBaseActivity() {

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, TransactionCategoryActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_category)
        buildScreen()
    }

    private fun buildScreen() {
        val transactionViewPager = transaction_view_pager
        val transactionTabs = transaction_type_tab_layout
        val addIncomeButton = add_income_fab
        addIncomeButton.setOnClickListener {
            startActivity(
                AddTransactionCategoryActivity.getStartIntent(
                    it.context
                )
            )
        }
        transactionViewPager.adapter =
            AddTransactionPagerAdapter(
                supportFragmentManager,
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
            )
        transactionTabs.setupWithViewPager(transactionViewPager)
    }
}

