package org.tirasweel.drivelogger.ui.compose.refuelloglist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.tirasweel.drivelogger.ui.theme.DriveLoggerTheme
import org.tirasweel.drivelogger.viewmodels.RefuelLogViewModel

@Composable
fun RefuelLogListScreen(
    modifier: Modifier = Modifier,
    refuelLogViewModel: RefuelLogViewModel,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(text = "hoge")
        Text(text = "fuga")
        Text(text = "moge")
        Text(text = "foo")
    }
//    LazyColumn(
//        modifier = modifier,
//    ) {
//
//    }
}

@Preview
@Composable
fun RefuelLogListScreenPreview() {
    DriveLoggerTheme {
        RefuelLogListScreen(
            refuelLogViewModel = RefuelLogViewModel()
        )
    }
}