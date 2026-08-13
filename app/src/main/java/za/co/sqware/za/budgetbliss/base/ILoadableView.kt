package za.co.sqware.za.budgetbliss.base

interface ILoadableView : IBaseView {
    fun showLoading()
    fun dismissLoading()
}