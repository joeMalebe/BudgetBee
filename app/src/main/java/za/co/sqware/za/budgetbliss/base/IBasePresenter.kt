package za.co.sqware.za.budgetbliss.base

interface IBasePresenter {

    fun attachView(view: IBaseView)

    fun detachView()
}