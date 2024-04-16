package com.phoebus.pix.demo.utils

import android.content.Context
import com.phoebus.pix.demo.R
import com.phoebus.pix.demo.data.enum.ChargeStatus
import com.phoebus.pix.demo.data.model.ConsultPixByClientIDResponse

class ResponseUtils {

    fun messageConsultPix(context: Context, response: ConsultPixByClientIDResponse): String {

        return "${context.getString(R.string.consult)}: \n" +
               "${context.getString(R.string.value)}: ${response.cobValue} \n" +
               "${context.getString(R.string.status)}: ${chargeStatus(context, response.status)} \n" +
               "${context.getString(R.string.tx_id)}: ${response.txId}"
    }

    fun chargeStatus(context: Context, status: String): String {
        return when (status) {
            ChargeStatus.ACTIVE.getValue() -> context.getString(R.string.active)
            ChargeStatus.CONCLUDED.getValue() -> context.getString(R.string.sale)
            ChargeStatus.REFUNDED.getValue() -> context.getString(R.string.refunded)
            ChargeStatus.REFUND_PROCESSING.getValue() -> context.getString(R.string.refund_processing)
            ChargeStatus.REFUND_NOT_DONE.getValue() -> context.getString(R.string.refund_not_done)
            ChargeStatus.REMOVED_BY_USER.getValue() -> context.getString(R.string.cancelled_charge)
            ChargeStatus.REMOVED_BY_PSP.getValue() -> context.getString(R.string.cancelled_charge)
            ChargeStatus.EXPIRED.getValue() -> context.getString(R.string.expired)
            else -> ""
        }
    }
}