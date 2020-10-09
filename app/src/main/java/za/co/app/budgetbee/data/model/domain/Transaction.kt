package za.co.app.budgetbee.data.model.domain

import android.os.Parcel
import android.os.Parcelable

data class Transaction(val transactionId: Int = 0,
                       val transactionDate: Long,
                       val transactionDescription: String,
                       val transactionAmount: Double,
                       val transactionCategoryId: Int,
                       val transactionCategoryName: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readDouble(),
            parcel.readInt(),
            parcel.readString()
    )

    constructor(transactionDate: Long,
                transactionDescription: String,
                transactionAmount: Double,
                transactionCategoryId: Int,
                transactionCategoryName: String) : this(0, transactionDate, transactionDescription, transactionAmount, transactionCategoryId, transactionCategoryName)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(transactionId)
        parcel.writeLong(transactionDate)
        parcel.writeString(transactionDescription)
        parcel.writeDouble(transactionAmount)
        parcel.writeInt(transactionCategoryId)
        parcel.writeString(transactionCategoryName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Transaction> {
        override fun createFromParcel(parcel: Parcel): Transaction {
            return Transaction(parcel)
        }

        override fun newArray(size: Int): Array<Transaction?> {
            return arrayOfNulls(size)
        }
    }
}