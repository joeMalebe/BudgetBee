package za.co.app.budgetbee

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_registration.*
import za.co.app.budgetbee.model.User

class Registration : AppCompatActivity() {

    val USERS = "Users"
    var database = FirebaseDatabase.getInstance()
    var firestoreDatabase = FirebaseFirestore.getInstance()
    val TAG = Registration::class.java.name

    companion object {

        fun getStartIntent(context: Context): Intent {
            return Intent(context, Registration::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        register_button.setOnClickListener(View.OnClickListener { view ->

            if (!isFieldsValid()) {
                Toast.makeText(view.context, "Fields are required", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            } else {
                val name = nameEditText.text.toString()
                val surname = surnameEditText.text.toString()
                val email = emailEditText.text.toString()
                val contactNumber = contactNumberEditText.text.toString()
                val password = passwordEditText.text.toString()


                val newUser = User(name, surname, email, contactNumber, password)
                firestoreDatabase.collection(USERS).add(newUser).addOnSuccessListener {
                    Log.d(TAG, "Document snapshot added with ID ${it.id}")
                }.addOnFailureListener {
                    Log.e(TAG, "Error adding document ${it.message}", it)
                }
            }

        })

    }

    fun isFieldsValid(): Boolean {
        return (!nameEditText.text.toString().isBlank() && !surnameEditText.text.toString().isBlank() && !emailEditText.text.toString().isBlank() && !contactNumberEditText.text.toString().isBlank() && !passwordEditText.text.toString().isBlank())
    }

    fun writeToDatabase(databaseReference: DatabaseReference, user: User) {

        databaseReference.setValue(user)
    }

}
