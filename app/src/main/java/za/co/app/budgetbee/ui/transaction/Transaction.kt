package za.co.app.budgetbee.ui.transaction

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import za.co.app.budgetbee.R
import java.util.logging.Logger

class Transaction : AppCompatActivity(), ITransactionMvp.View {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)
    }

    override fun onResume() {
        super.onResume()
        displayScreen()
    }

    override fun displayScreen() {
        Logger.getAnonymousLogger().info("Transaction Stopped")
    }
}