package za.co.app.budgetbee.ui.edit_transaction

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import za.co.app.budgetbee.R

class EditTransactionActivity : AppCompatActivity(),IEditTransactionMvp.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_transaction)
    }

    override fun displayScreen() {

    }
}