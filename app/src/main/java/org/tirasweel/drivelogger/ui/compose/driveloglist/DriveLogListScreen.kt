package org.tirasweel.drivelogger.ui.compose.driveloglist

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.fake.FakeDriveLogsRepository
import org.tirasweel.drivelogger.interfaces.LogListInteractionListener
import org.tirasweel.drivelogger.ui.compose.DriveLogNavigationBar
import org.tirasweel.drivelogger.ui.compose.common.ConfirmDialog
import org.tirasweel.drivelogger.ui.compose.common.FastScroller
import org.tirasweel.drivelogger.ui.theme.DriveLoggerTheme
import org.tirasweel.drivelogger.utils.DateFormatConverter.Companion.toYearMonthString
import org.tirasweel.drivelogger.viewmodels.DriveLogViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DriveLogListScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    driveLogViewModel: DriveLogViewModel,
    clickListener: LogListInteractionListener? = null,
    appBarClickListener: DriveLogListTopAppBarClickListener? = null,
) {
    val listState = rememberLazyListState()
    val driveLogs = driveLogViewModel.driveLogList.value
    var isChartExpanded by remember { mutableStateOf(true) }
    val grouped = remember(driveLogs) {
        driveLogs.groupBy { it.date.toYearMonthString() }
    }

    // 全アイテム（ヘッダー含む）のリストを作成し、ラベル引き引用にする
    val allItems = remember(grouped) {
        val list = mutableListOf<String>()
        grouped.forEach { (month, logs) ->
            list.add(month) // ヘッダー
            logs.forEach { _ -> list.add(month) } // 各ログアイテム
        }
        list
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            DriveLogListTopAppBar(
                modifier = Modifier,
                driveLogViewModel = driveLogViewModel,
                clickListener = appBarClickListener,
                isChartVisible = isChartExpanded,
                onToggleChart = { isChartExpanded = !isChartExpanded }
            )
        },
        bottomBar = {
            DriveLogNavigationBar(navController = navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(all = 16.dp),
                onClick = {
                    clickListener?.onFabAddClicked()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add",
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            val chartData = driveLogViewModel.chartData
            if (chartData.isNotEmpty()) {
                val chartModelProducer = remember { ChartEntryModelProducer() }
                val datasetForModel = remember(chartData) {
                    chartData.mapIndexed { index, dataPoint -> entryOf(index.toFloat(), dataPoint.value) }
                }

                LaunchedEffect(key1 = datasetForModel) {
                    chartModelProducer.setEntries(datasetForModel)
                }

                AnimatedVisibility(visible = isChartExpanded) {
                    Column {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                        ) {
                            val metric = driveLogViewModel.logListState.chartMetric.value
                            val title = when (metric) {
                                DriveLogViewModel.ChartMetric.FuelEfficiency -> "平均燃費の推移 (km/L)"
                                DriveLogViewModel.ChartMetric.Mileage -> "走行距離の推移 (km)"
                                DriveLogViewModel.ChartMetric.TotalMileage -> "総走行距離の推移 (km)"
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

                        // プルダウンでの設定切り替え (指標 & 期間をコンパクトな1行に配置)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // 左側：表示指標の選択 dropdown
                            val metric = driveLogViewModel.logListState.chartMetric.value
                            val metricLabel = when (metric) {
                                DriveLogViewModel.ChartMetric.FuelEfficiency -> "燃費"
                                DriveLogViewModel.ChartMetric.Mileage -> "走行距離"
                                DriveLogViewModel.ChartMetric.TotalMileage -> "総走行距離"
                            }
                            val metrics = listOf("燃費", "走行距離", "総走行距離")
                            CompactDropdown(
                                selectedLabel = metricLabel,
                                items = metrics,
                                onItemSelected = { index ->
                                    driveLogViewModel.logListState.chartMetric.value = DriveLogViewModel.ChartMetric.entries[index]
                                }
                            )

                            // 右側：集計期間の選択 dropdown
                            val type = driveLogViewModel.logListState.chartType.value
                            val typeLabel = when (type) {
                                DriveLogViewModel.ChartType.Weekly -> "週次"
                                DriveLogViewModel.ChartType.Monthly -> "月次"
                                DriveLogViewModel.ChartType.Yearly -> "年次"
                            }
                            val types = listOf("週次", "月次", "年次")
                            CompactDropdown(
                                selectedLabel = typeLabel,
                                items = types,
                                onItemSelected = { index ->
                                    driveLogViewModel.logListState.chartType.value = DriveLogViewModel.ChartType.entries[index]
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        HorizontalDivider()
                    }
                }
            }

            Box(modifier = Modifier.weight(1f)) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                ) {
                    grouped.forEach { (month, logs) ->
                        stickyHeader {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f))
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = month,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        items(logs) { driveLog ->
                            DriveLogRow(
                                driveLog = driveLog,
                                clickListener = clickListener,
                                modifier = Modifier.fillMaxWidth(),
                            )
                            HorizontalDivider()
                        }
                    }
                }

                FastScroller(
                    listState = listState,
                    modifier = Modifier.align(Alignment.CenterEnd),
                    labelProvider = { index ->
                        if (index in allItems.indices) allItems[index] else ""
                    }
                )
            }
        }
    }

    ConfirmDialog(
        isDisplayed = driveLogViewModel.uiState.isConfirmDialogForOverwriteExport,
        onResponse = { response ->
            clickListener?.onConfirmOverwriteExport(response)
        },
        textId = R.string.message_export_file_already_exists,
    )
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

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
private fun DriveLogListScreenPreview() {
    DriveLoggerTheme {
        DriveLogListScreen(
            modifier = Modifier.fillMaxWidth(),
            navController = rememberNavController(),
            driveLogViewModel = DriveLogViewModel(
                driveLogsRepository = FakeDriveLogsRepository(),
            ),
        )
    }
}
