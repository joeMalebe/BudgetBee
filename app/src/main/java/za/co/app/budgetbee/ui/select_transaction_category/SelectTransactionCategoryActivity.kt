package za.co.app.budgetbee.ui.select_transaction_category

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import kotlinx.android.synthetic.main.activity_transaction_category.*
import kotlinx.android.synthetic.main.transactions_activity_toolbar.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.base.AppCompatBaseActivity
import za.co.app.budgetbee.base.IBaseView
import za.co.app.budgetbee.ui.add_transaction.AddTransactionPagerAdapter
import javax.inject.Inject
import javax.inject.Named

class SelectTransactionCategoryActivity : AppCompatBaseActivity(), IBaseView {

    @Inject
    @field:Named("income")
    lateinit var incomePresenter: ISelectTransactionCategoryMvp.Presenter

    @Inject
    @field:Named("expense")
    lateinit var expensePresenter: ISelectTransactionCategoryMvp.Presenter

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
        transactionViewPager.adapter =
                AddTransactionPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, incomePresenter, expensePresenter)
        transactionTabs.setupWithViewPager(transactionViewPager)
    }
}