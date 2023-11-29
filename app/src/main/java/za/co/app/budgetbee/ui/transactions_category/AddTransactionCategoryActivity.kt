package za.co.app.budgetbee.ui.transactions_category


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import za.co.app.budgetbee.R
import za.co.app.budgetbee.base.AppCompatBaseActivity
import za.co.app.budgetbee.data.model.domain.TransactionCategoryType
import za.co.app.budgetbee.databinding.ActivityAddTransactionCategoryBinding
import javax.inject.Inject

class AddTransactionCategoryActivity : AppCompatBaseActivity(),
    ITransactionCategoryMvp.View {

    val TAG = AddTransactionCategoryActivity::class.simpleName

    @Inject
    lateinit var presenter: ITransactionCategoryMvp.Presenter
    private lateinit var binding: ActivityAddTransactionCategoryBinding
    companion object {

        const val UNCHECKED = -1
        fun getStartIntent(context: Context): Intent {
            return Intent(context, AddTransactionCategoryActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter.attachView(this)
        binding.toolbar.screenTitle.text = getString(R.string.add_category)
        binding.toolbar.backButton.setOnClickListener { navigateToTransactionCategorySelectScreen() }
        displayScreen()
    }

    fun addTransactionCategory(view: View) {

        if (binding.radioGroupTransationCategory.checkedRadioButtonId == UNCHECKED) {
            Toast.makeText(view.context, "Please select a category", Toast.LENGTH_SHORT).show()
        } else {

            val categoryType = if (binding.radioIncome.isChecked) {
                TransactionCategoryType.INCOME
            } else {
                TransactionCategoryType.EXPENSE
            }
            val categoryName = binding.inputCategoryNameEditText.text.toString()
            showLoading()
            presenter.submitTransactionCategory(categoryName, categoryType)

        }
    }


    override fun showLoading() {
        Log.d(TAG, "showLoading()")
    }

    override fun dismissLoading() {
        Log.d(TAG, "dismissLoading()")
    }

    override fun navigateToTransactionCategorySelectScreen() {
        dismissLoading()
        setResult(2)
        finish()
    }

    override fun displayScreen() {

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}