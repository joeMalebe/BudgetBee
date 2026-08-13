package za.co.sqware.za.budgetbliss.base

interface IServiceCallView : ILoadableView {
    fun showError(error: Throwable)
}