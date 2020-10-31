package za.co.app.budgetbee.ui.transactions_category


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_transaction_category.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.base.AppCompatBaseActivity
import za.co.app.budgetbee.data.model.domain.TransactionCategoryType
import javax.inject.Inject

class AddTransactionCategoryActivity : AppCompatBaseActivity(),
    ITransactionCategoryMvp.View {

    val TAG = AddTransactionCategoryActivity::class.simpleName

    @Inject
    lateinit var presenter: ITransactionCategoryMvp.Presenter

    companion object {

        const val UNCHECKED = -1
        fun getStartIntent(context: Context): Intent {
            return Intent(context, AddTransactionCategoryActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction_category)
        presenter.attachView(this)
        displayScreen()
    }

    fun addTransactionCategory(view: View) {

        if (radio_group_transation_category.checkedRadioButtonId == UNCHECKED) {
            Toast.makeText(view.context, "Please select a category", Toast.LENGTH_SHORT).show()
        } else {

            val categoryType = if (radio_income.isChecked) {
                TransactionCategoryType.INCOME
            } else {
                TransactionCategoryType.EXPENSE
            }
            val categoryName = input_category_name_editText.text.toString()
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
        SelectTransactionCategoryActivity.getStartIntent(this)
        finish()
    }

    override fun displayScreen() {

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}