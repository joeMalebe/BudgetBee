package za.co.app.budgetbee.base

import android.util.Log
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

abstract class BasePresenterObserver<T>(open val view: IServiceCallView) : Observer<T> {
    val TAG = BasePresenterObserver::class.simpleName

    override fun onComplete() {
        view.dismissLoading()
        Log.d(TAG, "onComplete called")
    }

    override fun onSubscribe(disposable: Disposable) {
        Log.d(TAG, "onSubscribe called with disposable $disposable")
    }

    abstract override fun onNext(value: T)


    override fun onError(error: Throwable) {
        view.dismissLoading()
        view.showError()
        Log.d(TAG, "onError called with ${error.message}")
    }

}