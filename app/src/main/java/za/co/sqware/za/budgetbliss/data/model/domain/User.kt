package za.co.sqware.za.budgetbliss.data.model.domain

data class User(
    val name: String,
    val surname: String,
    val email: String,
    val contactNumber: String,
    val password: String
)