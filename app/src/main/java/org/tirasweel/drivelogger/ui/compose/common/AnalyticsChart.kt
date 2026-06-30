package org.tirasweel.drivelogger.ui.compose.common

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.axis.axisLabelComponent
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.component.shape.shader.verticalGradient
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.chart.DefaultPointConnector
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf
import org.tirasweel.drivelogger.utils.ChartDataPoint
import org.tirasweel.drivelogger.utils.ChartMetric
import org.tirasweel.drivelogger.utils.ChartType

@Composable
fun AnalyticsChart(
    isChartExpanded: Boolean,
    chartData: List<ChartDataPoint>,
    currentMetric: ChartMetric,
    currentType: ChartType,
    onMetricSelected: (ChartMetric) -> Unit,
    onTypeSelected: (ChartType) -> Unit,
    modifier: Modifier = Modifier
) {
    if (chartData.isEmpty()) return

    val chartModelProducer = remember { ChartEntryModelProducer() }
    val datasetForModel = remember(chartData) {
        chartData.mapIndexed { index, dataPoint -> entryOf(index.toFloat(), dataPoint.value) }
    }

    LaunchedEffect(key1 = datasetForModel) {
        chartModelProducer.setEntries(datasetForModel)
    }

    AnimatedVisibility(visible = isChartExpanded, modifier = modifier) {
        Column {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                val title = when (currentMetric) {
                    ChartMetric.FuelEfficiency -> "平均燃費の推移 (km/L)"
                    ChartMetric.Mileage -> "走行距離の推移 (km)"
                    ChartMetric.TotalMileage -> "総走行距離の推移 (km)"
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Chart(
                    chart = lineChart(
                        spacing = 32.dp,
                        lines = listOf(
                            com.patrykandpatrick.vico.core.chart.line.LineChart.LineSpec(
                                lineColor = MaterialTheme.colorScheme.primary.toArgb(),
                                lineBackgroundShader = verticalGradient(
                                    arrayOf(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), MaterialTheme.colorScheme.primary.copy(alpha = 0f)),
                                ),
                                pointConnector = DefaultPointConnector(cubicStrength = 0f)
                            )
                        )
                    ),
                    chartModelProducer = chartModelProducer,
                    startAxis = rememberStartAxis(),
                    bottomAxis = rememberBottomAxis(
                        label = axisLabelComponent(textSize = 9.sp),
                        labelRotationDegrees = 90f,
                        valueFormatter = AxisValueFormatter { value, _ ->
                            chartData.getOrNull(value.toInt())?.label ?: ""
                        }
                    )
                )
            }

            // プルダウンでの設定切り替え (指標 & 期間)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 左側：表示指標の選択 dropdown
                val metricLabel = when (currentMetric) {
                    ChartMetric.FuelEfficiency -> "燃費"
                    ChartMetric.Mileage -> "走行距離"
                    ChartMetric.TotalMileage -> "総走行距離"
                }
                val metrics = listOf("燃費", "走行距離", "総走行距離")
                CompactDropdown(
                    selectedLabel = metricLabel,
                    items = metrics,
                    onItemSelected = { index ->
                        onMetricSelected(ChartMetric.entries[index])
                    }
                )

                // 右側：集計期間の選択 dropdown
                val typeLabel = when (currentType) {
                    ChartType.Weekly -> "週次"
                    ChartType.Monthly -> "月次"
                    ChartType.Yearly -> "年次"
                }
                val types = listOf("週次", "月次", "年次")
                CompactDropdown(
                    selectedLabel = typeLabel,
                    items = types,
                    onItemSelected = { index ->
                        onTypeSelected(ChartType.entries[index])
                    }
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider()
        }
    }
}

@Composable
private fun CompactDropdown(
    selectedLabel: String,
    items: List<String>,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .clickable { expanded = true }
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.small
                )
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedLabel,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onItemSelected(index)
                        expanded = false
                    }
                )
            }
        }
    }
}
