package za.co.app.budgetbee.base

import android.content.Context
import androidx.fragment.app.Fragment
import za.co.app.budgetbee.BudgetBeeApplication

abstract class BaseFragment : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        BudgetBeeApplication.instance.feather.injectFields(this)
    }
}