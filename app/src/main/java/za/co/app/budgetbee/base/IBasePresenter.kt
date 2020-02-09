package za.co.app.budgetbee.base

interface IBasePresenter {

    fun start(view: IBaseView)

    fun stop()
}