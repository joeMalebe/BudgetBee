package za.co.app.budgetbee.ui.select_transaction_category

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_transaction_category.*
import kotlinx.android.synthetic.main.transactions_activity_toolbar.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.base.AppCompatBaseActivity
import za.co.app.budgetbee.base.IBaseView
import za.co.app.budgetbee.ui.add_transaction.AddTransactionPagerAdapter
import za.co.app.budgetbee.ui.transactions_category.AddTransactionCategoryActivity

class SelectTransactionCategoryActivity : AppCompatBaseActivity(), IBaseView {

    lateinit var addIncomeButton: FloatingActionButton

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, SelectTransactionCategoryActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_category)
        screen_title.text = getString(R.string.category)
        back_button.setOnClickListener { onBackPressed() }
        displayScreen()
    }

    override fun displayScreen() {
        val transactionViewPager = transaction_view_pager
        val transactionTabs = transaction_type_tab_layout
        val addIncomeButton = add_income_fab

        addIncomeButton.setOnClickListener { navigateToAddTransactionCategory() }
        transactionViewPager.adapter =
            AddTransactionPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        transactionTabs.setupWithViewPager(transactionViewPager)
    }

    fun navigateToAddTransactionCategory() {
        startActivityForResult(AddTransactionCategoryActivity.getStartIntent(this), 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }
}