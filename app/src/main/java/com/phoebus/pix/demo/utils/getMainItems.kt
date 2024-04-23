package com.phoebus.pix.demo.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.ManageSearch
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Pix
import com.phoebus.pix.demo.R
import com.phoebus.pix.demo.ui.theme.components.Destinations
import com.phoebus.pix.demo.ui.theme.components.MenuItem
import java.util.LinkedList

fun getMainItems(): List<MenuItem> {

    val items = LinkedList<MenuItem>()

    items.add(
        MenuItem(
            1,
            R.string.cob_gen,
            Icons.Default.Pix,
            Destinations.COBCREATE.name)
    )

    items.add(
        MenuItem(
            2,
            R.string.pix_find,
            Icons.AutoMirrored.Filled.ManageSearch,
            Destinations.CONSULTPIX.name)
    )

    items.add(
        MenuItem(
            3,
            R.string.pix_list,
            Icons.AutoMirrored.Filled.List,
            Destinations.FILTERPIX.name)
    )

    items.add(
        MenuItem(
            4,
            R.string.pix_refund,
            Icons.Default.CurrencyExchange,
            Destinations.PIXDREFUND.name
        )
    )

    items.add(
        MenuItem(
            5,
            R.string.app_pix_is_installed,
            Icons.Default.CheckCircle,
            Destinations.CHEACKAPPPIX.name
        )
    )

    return items

}