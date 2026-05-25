package org.tirasweel.drivelogger.ui.compose.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun FastScroller(
    listState: LazyListState,
    modifier: Modifier = Modifier,
    labelProvider: (Int) -> String = { "" }
) {
    val coroutineScope = rememberCoroutineScope()
    var isDragging by remember { mutableStateOf(false) }
    var containerHeight by remember { mutableFloatStateOf(0f) }

    // 現在のスクロール位置を計算（derivedStateOfで効率化）
    val scrollPercent by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            if (totalItems == 0) 0f
            else {
                listState.firstVisibleItemIndex.toFloat() / totalItems.toFloat()
            }
        }
    }

    // ドラッグ中の位置を保持する内部状態
    var dragPosition by remember { mutableFloatStateOf(0f) }

    // 指定した割合（0.0-1.0）の位置までスクロールする
    fun scrollTo(position: Float) {
        val totalItems = listState.layoutInfo.totalItemsCount
        if (totalItems > 0) {
            val targetIndex = (position * totalItems).toInt().coerceIn(0, totalItems - 1)
            coroutineScope.launch {
                listState.scrollToItem(targetIndex)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(48.dp)
            .onGloballyPositioned { containerHeight = it.size.height.toFloat() }
            .pointerInput(Unit) {
                // タップした位置にジャンプ
                detectTapGestures(
                    onPress = { offset ->
                        isDragging = true
                        val pos = (offset.y / containerHeight).coerceIn(0f, 1f)
                        dragPosition = pos
                        scrollTo(pos)
                        tryAwaitRelease()
                        isDragging = false
                    }
                )
            }
            .pointerInput(Unit) {
                // ドラッグ操作
                detectDragGestures(
                    onDragStart = { offset ->
                        isDragging = true
                        dragPosition = (offset.y / containerHeight).coerceIn(0f, 1f)
                    },
                    onDragEnd = { isDragging = false },
                    onDragCancel = { isDragging = false },
                    onDrag = { change, _ ->
                        val pos = (change.position.y / containerHeight).coerceIn(0f, 1f)
                        dragPosition = pos
                        scrollTo(pos)
                    }
                )
            }
    ) {
        // ドラッグ中のポップアップ（年月表示）
        AnimatedVisibility(
            visible = isDragging,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset {
                    IntOffset(
                        x = (-60).dp.roundToPx(),
                        y = (dragPosition * containerHeight).roundToInt() - 25.dp.roundToPx()
                    )
                }
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                tonalElevation = 4.dp
            ) {
                val labelIndex = (dragPosition * listState.layoutInfo.totalItemsCount).roundToInt()
                    .coerceIn(0, listState.layoutInfo.totalItemsCount - 1)
                Text(
                    text = labelProvider(labelIndex),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // スクロールバー（つまみ）
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset {
                    val pos = if (isDragging) dragPosition else scrollPercent
                    // つまみの高さ(40dp)を考慮して位置を調整
                    val thumbHeight = 40.dp.roundToPx()
                    val y = (pos * (containerHeight - thumbHeight)).roundToInt()
                    IntOffset(x = (-8).dp.roundToPx(), y = y)
                }
                .size(width = 12.dp, height = 40.dp)
                .clip(CircleShape)
                .background(
                    if (isDragging) MaterialTheme.colorScheme.primary 
                    else MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                )
        )
    }
}
