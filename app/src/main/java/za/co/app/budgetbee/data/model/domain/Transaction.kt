package za.co.app.budgetbee.data.model.domain

import android.os.Parcel
import android.os.Parcelable

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
data class Transaction(val transactionId: Int = 0,
                       var transactionDate: Long,
                       var transactionDescription: String,
                       var transactionAmount: Double,
                       var transactionCategoryId: Int,
                       var transactionCategoryName: String,
                       var transactionCategoryType: Int

) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readDouble(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt()
    )

    constructor(transactionDate: Long,
                transactionDescription: String,
                transactionAmount: Double,
                transactionCategoryId: Int,
                transactionCategoryName: String,
                transactionCategoryType: Int
    ) : this(0, transactionDate, transactionDescription, transactionAmount, transactionCategoryId, transactionCategoryName, transactionCategoryType)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(transactionId)
        parcel.writeLong(transactionDate)
        parcel.writeString(transactionDescription)
        parcel.writeDouble(transactionAmount)
        parcel.writeInt(transactionCategoryId)
        parcel.writeString(transactionCategoryName)
        parcel.writeInt(transactionCategoryType)
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