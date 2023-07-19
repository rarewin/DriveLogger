package org.tirasweel.drivelogger.classes

import io.realm.kotlin.query.Sort
import org.tirasweel.drivelogger.R

/**
 * @brief リストのソート順
 */
enum class SortOrderType {
    AscendingDate,
    DescendingDate;

    val menuTextId: Int
        get() = when (this) {
            AscendingDate -> R.string.sort_date_ascending
            DescendingDate -> R.string.sort_date_descending
        }

    /**
     * @brief ソート順(RealmのSortで)
     */
    val order
        get() = when (this) {
            AscendingDate -> Sort.ASCENDING
            DescendingDate -> Sort.DESCENDING
        }

    /**
     * @brief ソートに使用するプロパティ
     */
    val property
        get() = when (this) {
            AscendingDate, DescendingDate -> "date"
        }
}

