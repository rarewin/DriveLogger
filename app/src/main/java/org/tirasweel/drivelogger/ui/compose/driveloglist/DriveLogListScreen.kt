package org.tirasweel.drivelogger.ui.compose.driveloglist

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.fake.FakeDriveLogsRepository
import org.tirasweel.drivelogger.interfaces.LogListInteractionListener
import org.tirasweel.drivelogger.ui.compose.DriveLogNavigationBar
import org.tirasweel.drivelogger.ui.compose.common.AnalyticsChart
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
            // 共通化した AnalyticsChart を呼び出すだけ
            AnalyticsChart(
                isChartExpanded = isChartExpanded,
                chartData = driveLogViewModel.chartData,
                currentMetric = driveLogViewModel.logListState.chartMetric.value,
                currentType = driveLogViewModel.logListState.chartType.value,
                onMetricSelected = { driveLogViewModel.logListState.chartMetric.value = it },
                onTypeSelected = { driveLogViewModel.logListState.chartType.value = it }
            )

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
