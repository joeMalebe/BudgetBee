package za.co.app.budgetbee.data.model

import android.os.Parcel
import android.os.Parcelable

data class Transaction(
    val transactionDate: Long,
    val transactionDescription: String?,
    val transactionAmount: Double,
    val transactionCategoryId: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(transactionDate)
        parcel.writeString(transactionDescription)
        parcel.writeDouble(transactionAmount)
        parcel.writeInt(transactionCategoryId)
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