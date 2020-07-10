package za.co.app.budgetbee.base

interface IBasePresenter {

    fun attachView(view: IBaseView)

    fun detachView()
}