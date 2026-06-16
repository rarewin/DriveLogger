package org.tirasweel.drivelogger.ui.compose.refuelloglist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.db.RefuelLog
import org.tirasweel.drivelogger.ui.compose.DriveLogNavigationBar
import org.tirasweel.drivelogger.ui.compose.common.FastScroller
import org.tirasweel.drivelogger.utils.DateFormatConverter.Companion.toLocaleDateString
import org.tirasweel.drivelogger.utils.DateFormatConverter.Companion.toYearMonthString
import org.tirasweel.drivelogger.viewmodels.RefuelLogViewModel
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun RefuelLogListScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    refuelLogViewModel: RefuelLogViewModel,
    onFabAddClicked: () -> Unit,
    onItemClicked: (RefuelLog) -> Unit,
    appBarClickListener: RefuelLogListTopAppBarClickListener? = null,
) {
    val listState = rememberLazyListState()
    val logs = refuelLogViewModel.refuelLogList.value
    val grouped = remember(logs) {
        logs.groupBy { it.date.toYearMonthString() }
    }

    // 全アイテム（ヘッダー含む）のリストを作成し、ラベル引き引用にする
    val allItems = remember(grouped) {
        val list = mutableListOf<String>()
        grouped.forEach { (month, monthLogs) ->
            list.add(month) // ヘッダー
            monthLogs.forEach { _ -> list.add(month) } // 各ログアイテム
        }
        list
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            RefuelLogListTopAppBar(
                refuelLogViewModel = refuelLogViewModel,
                clickListener = appBarClickListener,
            )
        },
        bottomBar = {
            DriveLogNavigationBar(navController = navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(all = 16.dp),
                onClick = onFabAddClicked
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(id = R.string.description_fab_new_drivelog))
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
            ) {
                grouped.forEach { (month, monthLogs) ->
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
                    items(monthLogs) { log ->
                        RefuelLogRow(
                            log = log,
                            modifier = Modifier.clickable { onItemClicked(log) }
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

@Composable
fun RefuelLogRow(
    log: RefuelLog,
    modifier: Modifier = Modifier
) {
    ListItem(
        modifier = modifier,
        headlineContent = {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = Date(log.date).toLocaleDateString(),
                    modifier = Modifier.weight(1f)
                )
                log.fuelEfficiency?.let {
                    Text(text = stringResource(id = R.string.text_km_l, it))
                }
            }
        },
        supportingContent = {
            Column {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = log.totalMilliMileage?.let {
                            stringResource(id = R.string.text_km, it / 1000.0)
                        } ?: "--- km",
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "[ " + stringResource(id = R.string.text_km, log.milliMileage / 1000.0) + " ]",
                        modifier = Modifier.weight(1f)
                    )
                    Text(text = stringResource(id = R.string.text_l, log.fuelAmount))
                }
                if (log.memo.isNotEmpty()) {
                    Text(text = log.memo)
                }
            }
        }
    )
}
