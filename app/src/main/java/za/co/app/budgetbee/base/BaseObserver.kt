package za.co.app.budgetbee.base

import android.util.Log
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

abstract class BaseObserver<T> : Observer<T> {
    val TAG = BaseObserver::class.simpleName

    override fun onComplete() {
        Log.d(TAG, "onComplete called")
    }

    override fun onSubscribe(disposable: Disposable) {
        Log.d(TAG, "onSubscribe called with disposable $disposable")
    }

    abstract override fun onNext(value: T)


    override fun onError(error: Throwable) {
        Log.d(TAG, "onError called with ${error.message}")
    }

}