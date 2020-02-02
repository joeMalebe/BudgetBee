package za.co.app.budgetbee.utils

import android.util.Log

open class SingletonHolder<out T : Any, in A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator
    @Volatile
    private var instance: T? = null

    fun getInstance(arg: A): T {
        val i = instance
        if (i != null) {
            Log.i("SingletonHolder", "Old instance returned")
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                Log.i("SingletonHolder", "New instance created")
                creator = null
                created
            }
        }
    }
}