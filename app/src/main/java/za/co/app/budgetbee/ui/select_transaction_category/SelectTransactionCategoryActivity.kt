package za.co.app.budgetbee.ui.select_transaction_category

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import za.co.app.budgetbee.R
import za.co.app.budgetbee.base.AppCompatBaseActivity
import za.co.app.budgetbee.base.IBaseView
import za.co.app.budgetbee.databinding.ActivityTransactionCategoryBinding
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

    private lateinit var binding: ActivityTransactionCategoryBinding
    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, SelectTransactionCategoryActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.screenTitle.text = getString(R.string.category)
        binding.toolbar.backButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        displayScreen()
    }

    override fun displayScreen() {
        val transactionViewPager = binding.transactionViewPager
        val transactionTabs = binding.transactionTypeTabLayout
        transactionViewPager.adapter =
                AddTransactionPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, incomePresenter, expensePresenter)
        transactionTabs.setupWithViewPager(transactionViewPager)
    }
}