package za.co.app.budgetbee.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import za.co.app.budgetbee.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AddExpenseFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AddExpenseFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AddExpenseFragment : Fragment() {

    companion object {
        fun newInstance(): AddExpenseFragment {
            return AddExpenseFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_expense, container, false)
    }


}
