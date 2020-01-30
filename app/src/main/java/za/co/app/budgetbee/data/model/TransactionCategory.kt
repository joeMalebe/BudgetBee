package za.co.app.budgetbee.data.model

import android.os.Parcel
import android.os.Parcelable


data class TransactionCategory(
    var transactionCategoryName: String,
    var transactionCategoryType: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(transactionCategoryName)
        parcel.writeInt(transactionCategoryType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TransactionCategory> {
        override fun createFromParcel(parcel: Parcel): TransactionCategory {
            return TransactionCategory(parcel)
        }

        override fun newArray(size: Int): Array<TransactionCategory?> {
            return arrayOfNulls(size)
        }
    }
}