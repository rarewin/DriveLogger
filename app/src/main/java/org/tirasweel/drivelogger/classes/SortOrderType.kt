package org.tirasweel.drivelogger.classes

import io.realm.kotlin.query.Sort

/**
 * @brief リストのソート順
 */
enum class SortOrderType {
    AscendingDate,
    DescendingDate;

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

