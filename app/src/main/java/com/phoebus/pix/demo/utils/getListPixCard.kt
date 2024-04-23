package com.phoebus.pix.demo.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.ui.graphics.vector.ImageVector
import com.phoebus.pix.demo.R
import java.util.LinkedList

data class CardItem(
    val id: Int,
    val text: Int,
    val icon: ImageVector,
    var isSelected: Boolean
)

fun getListPixCards(): List<CardItem> {
    val items = LinkedList<CardItem>()

    items.add(
        CardItem(1, R.string.current_day, Icons.Default.CalendarToday, true)
    )
    items.add(
        CardItem(2, R.string.current_month, Icons.Default.CalendarMonth, true)
    )
    items.add(
        CardItem(3, R.string.another_period, Icons.Default.EditCalendar, true)
    )

    return items
}
