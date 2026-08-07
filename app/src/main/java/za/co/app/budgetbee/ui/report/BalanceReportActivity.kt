package za.co.app.budgetbee.ui.report

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.EntryXComparator
import io.reactivex.disposables.CompositeDisposable
import za.co.app.budgetbee.R
import za.co.app.budgetbee.base.AppCompatBaseActivity
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.data.model.domain.TransactionCategoryType
import za.co.app.budgetbee.databinding.ActivityBalanceReportBinding
import za.co.app.budgetbee.ui.custom_views.LineChartMarkerView
import java.util.Collections
import javax.inject.Inject

class BalanceReportActivity : AppCompatBaseActivity(), IBalanceReportMvp.View {

    private val compositeDisposable = CompositeDisposable()

    @Inject
    private lateinit var presenter: IBalanceReportMvp.Presenter
    private lateinit var binding: ActivityBalanceReportBinding

    companion object {

        fun getStartIntent(context: Context): Intent {
            return Intent(context, BalanceReportActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBalanceReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lineChart.setNoDataTextTypeface(ResourcesCompat.getFont(this, R.font.poppins_bold))
        binding.lineChart.setNoDataTextColor(R.color.colorPrimaryDark)

        presenter.attachView(this)
        val timePeriodSwitcher = binding.toolbar.timePeriodSwitcher
        timePeriodSwitcher.init(BalanceReportPresenter.PERIOD.values()[0])
        timePeriodSwitcher.getSelectedTimePeriod().subscribe { timePeriod ->
            presenter.updateTimePeriod(timePeriod)
        }.let { compositeDisposable.add(it) }
        displayScreen()
    }

    override fun displayTransactions(transactionsByCategory: Map<Int, List<Transaction>>) {
        if (transactionsByCategory.isEmpty()) {
            binding.lineChart.clear()
        } else {

            val incomeLineDataSet = getFormattedLineDateSet(getEntriesByCategoryType(transactionsByCategory
                    [TransactionCategoryType.INCOME.value]), "Income", R.color.green)

            val expenseLineDataSet = getFormattedLineDateSet(getEntriesByCategoryType(transactionsByCategory
                    [TransactionCategoryType.EXPENSE.value]), "Expense", R.color.red)

            val transactionsDataSet = arrayListOf<ILineDataSet>()
            transactionsDataSet.add(incomeLineDataSet)
            transactionsDataSet.add(expenseLineDataSet)

            val lineData = LineData(transactionsDataSet)
            lineData.setDrawValues(false)

            binding.lineChart.getDescription().setEnabled(false)
            binding.lineChart.setTouchEnabled(true)
            binding.lineChart.setDragEnabled(true)
            binding.lineChart.setScaleEnabled(true)
            binding.lineChart.setPinchZoom(true)
            binding.lineChart.setDrawGridBackground(false)
            binding.lineChart.setMaxHighlightDistance(300f)

            //x axis styling
            val x: XAxis = binding.lineChart.getXAxis()
            x.isEnabled = true
            x.valueFormatter = LineChartXAxisValueFormatter()
            x.setLabelCount(5, false)
            x.typeface = ResourcesCompat.getFont(this, R.font.lora_italic)
            x.textColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
            x.setDrawGridLines(false)
            x.setPosition(XAxis.XAxisPosition.BOTTOM)

            //y axis styling
            val y: YAxis = binding.lineChart.getAxisLeft()
            y.typeface = ResourcesCompat.getFont(this, R.font.lora_italic)
            y.setLabelCount(6, false)
            y.textColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
            y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
            y.setDrawGridLines(false)
            y.axisLineColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)

            binding.lineChart.getAxisRight().setEnabled(false)
            binding.lineChart.data = lineData
            binding.lineChart.getLegend().setEnabled(false)
            binding.lineChart.animateXY(2000, 2000)
            binding.lineChart.extraBottomOffset = 16f
            binding.lineChart.marker = LineChartMarkerView(this, R.layout.line_chart_marker)
            binding.lineChart.invalidate()
        }

    }

    override fun displayNoTransactions() {
        binding.lineChart.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
        compositeDisposable.dispose()
    }

    private fun getFormattedLineDateSet(entriesByCategoryType: ArrayList<Entry>, description: String, color: Int): LineDataSet {
        val lineDataSet = LineDataSet(entriesByCategoryType, description)
        lineDataSet.color = ContextCompat.getColor(this, color)
        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT
        Collections.sort(entriesByCategoryType, EntryXComparator())
        formatLineDataSet(lineDataSet, color)
        return lineDataSet
    }

    private fun formatLineDataSet(lineDataSet: LineDataSet, colorResource: Int) {
        lineDataSet.lineWidth = 1.8f
        lineDataSet.circleRadius = 4f
        lineDataSet.setCircleColor(ContextCompat.getColor(this, colorResource))
        lineDataSet.highLightColor = ContextCompat.getColor(this, colorResource)
        lineDataSet.color = ContextCompat.getColor(this, colorResource)
        lineDataSet.fillColor = ContextCompat.getColor(this, colorResource)
        lineDataSet.fillAlpha = 50
        lineDataSet.setDrawHorizontalHighlightIndicator(true)
        lineDataSet.fillFormatter = IFillFormatter { dataSet, dataProvider -> binding.lineChart.getAxisLeft().getAxisMinimum() }
    }

    private fun getEntriesByCategoryType(transactions: List<Transaction>?): ArrayList<Entry> {
        val transactionEntries = arrayListOf<Entry>()
        transactions?.forEach {
            transactionEntries.add(LineChartEntry(it.transactionDate / 1000f, it.transactionAmount.toFloat(), it.transactionDate))
        }
        return transactionEntries
    }

    override fun displayScreen() {
        presenter.getTransactionsGroupedByCategoryType(BalanceReportPresenter.PERIOD.LAST_WEEK)
    }

    override fun showError(error: Throwable) {

    }

    override fun showLoading() {

    }

    override fun dismissLoading() {

    }
}