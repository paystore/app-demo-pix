package com.phoebus.pix.demo.utils

import android.content.Context
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

class CurrencyUtil {

    fun addMaskCurrency(bigDecimalValue: BigDecimal, context: Context): String {
        val locale = context.resources.configuration.locale
        val numberFormat = NumberFormat.getCurrencyInstance(locale)
        val decimalFormat = numberFormat as DecimalFormat
        return decimalFormat.format(bigDecimalValue)
    }

    fun removeMaskCurrency(value: String): String {
        return value.replace("[^0-9]".toRegex(), "")
    }

    fun toBigDecimalCurrency(value: String): BigDecimal {
        return BigDecimal(value).divide(BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP)
    }

    fun formatCurrency(value: String): String {
        val decimal = BigDecimal(value)
        val lang = Locale.getDefault().language
        val country = Locale.getDefault().country
        val locale = Locale(lang, country)
        return NumberFormat.getCurrencyInstance(locale).format(decimal)
    }

    fun moneyStringForRequest(value: String): String {
        val doubleValue = toBigDecimalCurrency(removeMaskCurrency(value)).toDouble()
        return String.format("%.2f", doubleValue).replace(",", ".")
    }
}