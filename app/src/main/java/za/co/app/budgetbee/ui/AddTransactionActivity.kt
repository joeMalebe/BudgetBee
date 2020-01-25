package za.co.app.budgetbee.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import kotlinx.android.synthetic.main.activity_add_transaction.*
import za.co.app.budgetbee.R

class AddTransactionActivity : AppCompatActivity() {

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, AddTransactionActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)
        buildScreen()
    }

    private fun buildScreen() {
        val transactionViewPager = transaction_view_pager
        val transactionTabs = transaction_type_tab_layout
        transactionViewPager.adapter =
            AddTransactionPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        transactionTabs.setupWithViewPager(transactionViewPager)
    }
}

