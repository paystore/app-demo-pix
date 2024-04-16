package com.phoebus.pix.demo.ui.theme.components

import androidx.annotation.StringRes
import com.phoebus.pix.demo.R

enum class Destinations(@StringRes val title: Int) {
    HOME(title = R.string.app_name),
    COBCREATE(title = R.string.cob_gen),
    CONSULTPIX(title = R.string.pix_find),
    FILTERPIX(title = R.string.pix_list),
    LISTPIX(title = R.string.pix_list),
    PIXDREFUND(title = R.string.pix_refund),
    CHEACKAPPPIX(title = R.string.app_pix_is_installed),
    CLIENTID(title = R.string.client_id),
    TXID(title = R.string.tx_id)
}