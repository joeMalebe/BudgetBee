package za.co.sqware.za.budgetbliss.base

import android.content.Context
import androidx.fragment.app.Fragment
import za.co.sqware.za.budgetbliss.BudgetBeeApplication

abstract class BaseFragment : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        BudgetBeeApplication.instance.feather.injectFields(this)
    }
}