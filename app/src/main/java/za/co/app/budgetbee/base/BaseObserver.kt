package za.co.app.budgetbee.base

import android.util.Log
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

abstract class BaseObserver<T> : Observer<T> {
    val TAG = BaseObserver::class.simpleName
    override fun onComplete() {
        Log.d(TAG, "onComplete called")
    }

    override fun onSubscribe(d: Disposable) {
        Log.d(TAG, "onSubscribe called with disposable $d")
    }

    abstract override fun onNext(t: T)


    override fun onError(e: Throwable) {
        Log.d(TAG, "onError called with ${e.message}")
    }

}