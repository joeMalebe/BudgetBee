package za.co.app.budgetbee.base

import android.util.Log
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable

abstract class BaseCompletableObserver<T> : CompletableObserver {
    abstract override fun onComplete()

    override fun onSubscribe(d: Disposable) {
        Log.i("BaseCompletableObserver", "onSubscribe called with disposable >$d")
    }

    override fun onError(e: Throwable) {
        Log.i("BaseCompletableObserver", "onError called with error > ${e.message}")
    }

}