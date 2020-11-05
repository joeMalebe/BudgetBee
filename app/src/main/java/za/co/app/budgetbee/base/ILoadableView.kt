package za.co.app.budgetbee.base

interface ILoadableView : IBaseView {
    fun showLoading()
    fun dismissLoading()
}