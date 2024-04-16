package com.phoebus.pix.demo.utils

import android.util.Log
import com.phoebus.pix.demo.utils.ConstantsUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DateUtils{

    private val DATE: String = "dd/MM/yyyy"
    val DATE_TIME: String = "dd/MM/yyyy HH:mm"
    val DATE_TIME_NAV: String = "dd-MM-yyyy HH:mm"
    private val HOUR_MINUTE: String = "HH:mm"

    fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat(HOUR_MINUTE, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat(DATE, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun getFirstDayOfMonth(): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val dateFormat = SimpleDateFormat(DATE, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun getDate30DaysAgo(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -30)
        val dateFormat = SimpleDateFormat(DATE, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun stringDateToDate(date: String): Date? {
        val dateFormat = SimpleDateFormat(DATE)
        return try {
            dateFormat.parse(date)
        } catch (ex: Exception) {
            Log.d(ConstantsUtils().TAG,"Erro convertendo data string para Date")
            null
        }
    }

    fun stringDateTimeToDate(date: String): Date? {
        val dateFormat = SimpleDateFormat(DATE_TIME_NAV)
        return try {
            dateFormat.parse(date)
        } catch (ex: Exception) {
            Log.d(ConstantsUtils().TAG,"Erro convertendo data string para Date")
            null
        }
    }

    fun validTime(horaInicial: String, horaFinal: String): Boolean {
        val formato = SimpleDateFormat(HOUR_MINUTE)

        val horaInicialDate = formato.parse(horaInicial)
        val horaFinalDate = formato.parse(horaFinal)

        return horaInicialDate.before(horaFinalDate)
    }
}