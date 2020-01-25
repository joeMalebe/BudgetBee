package za.co.app.budgetbee.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import za.co.app.budgetbee.R


class AddIncomeFragment : Fragment() {

    companion object {
        fun newInstance(): AddIncomeFragment {
            return AddIncomeFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_income, container, false)

        return view
    }


}// Required empty public constructor
