package za.co.app.budgetbee

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import za.co.app.budgetbee.R
import kotlinx.android.synthetic.main.activity_registration.*

class Registration : AppCompatActivity() {

    fun getStartIntent (context: Context):Intent{
        return Intent(context,Registration::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

    }

}
