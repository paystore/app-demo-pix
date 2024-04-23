package com.phoebus.pix.demo.utils

import java.math.BigDecimal
import java.math.RoundingMode

class CurrencyUtil {

    private fun removeMaskCurrency(value: String): String {
        return value.replace("[^0-9]".toRegex(), "")
    }

    private fun toBigDecimalCurrency(value: String): BigDecimal {
        return BigDecimal.valueOf(value.toLong()).divide(BigDecimal(100), 2, RoundingMode.HALF_UP)
    }

    fun moneyStringForRequest(value: String): String {
        val doubleValue = toBigDecimalCurrency(removeMaskCurrency(value)).toDouble()
        return String.format("%.2f", doubleValue).replace(",", ".")
    }
}