package org.tirasweel.drivelogger.utils

import android.icu.util.Calendar
import org.tirasweel.drivelogger.interfaces.ChartableLog

enum class ChartMetric {
    FuelEfficiency, Mileage, TotalMileage
}

enum class ChartType {
    Weekly, Monthly, Yearly
}

data class ChartDataPoint(
    val label: String,
    val value: Double
)

object ChartHelper {
    fun generateChartData(
        logs: List<ChartableLog>,
        metric: ChartMetric,
        chartType: ChartType
    ): List<ChartDataPoint> {
        val sortedLogs = logs.sortedBy { it.logDate }
        if (sortedLogs.isEmpty()) return emptyList()

        val calendar = Calendar.getInstance()

        // 選択された指標に応じてログをフィルタリング
        val filteredLogs = when (metric) {
            ChartMetric.FuelEfficiency -> sortedLogs.filter { it.logFuelEfficiency != null }
            ChartMetric.Mileage -> sortedLogs.filter { it.logMilliMileage >= 0 }
            ChartMetric.TotalMileage -> sortedLogs.filter { it.logTotalMilliMileage != null }
        }

        if (filteredLogs.isEmpty()) return emptyList()

        // 期間ごとにグループ化
        val groupedLogs = filteredLogs.groupBy { log ->
            calendar.timeInMillis = log.logDate
            when (chartType) {
                ChartType.Weekly -> {
                    "${calendar.get(Calendar.YEAR)} W${calendar.get(Calendar.WEEK_OF_YEAR)}"
                }
                ChartType.Monthly -> {
                    "${calendar.get(Calendar.YEAR)}/${calendar.get(Calendar.MONTH) + 1}"
                }
                ChartType.Yearly -> {
                    "${calendar.get(Calendar.YEAR)}"
                }
            }
        }

        return groupedLogs.map { (label, logsInGroup) ->
            val value = when (metric) {
                ChartMetric.FuelEfficiency -> {
                    logsInGroup.mapNotNull { it.logFuelEfficiency }.average()
                }
                ChartMetric.Mileage -> {
                    logsInGroup.map { it.logMilliMileage.toDouble() / 1000.0 }.sum()
                }
                ChartMetric.TotalMileage -> {
                    logsInGroup.last().logTotalMilliMileage!!.toDouble() / 1000.0
                }
            }
            ChartDataPoint(label, value)
        }
    }
}
