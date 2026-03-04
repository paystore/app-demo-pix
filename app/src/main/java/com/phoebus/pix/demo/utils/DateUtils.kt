package com.phoebus.pix.demo.utils

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class DateUtils{

    private val date: String = "dd/MM/yyyy"
    private val hourMinute: String = "HH:mm"
    private val dateTime: String = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    private val dateTimeLayout = "dd/MM/yyyy HH:mm"

    fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat(hourMinute, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat(date, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun getFirstDayOfMonth(): String {
        val calendar = Calendar.getInstance()
        calendar[Calendar.DAY_OF_MONTH] = 1
        val dateFormat = SimpleDateFormat(date, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun getDate30DaysAgo(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -30)
        val dateFormat = SimpleDateFormat(date, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun stringDateToDate(date: String): Date? {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return try {
            dateFormat.parse(date)
        } catch (ex: Exception) {
            Log.d(ConstantsUtils().TAG, "Erro convertendo data string para Date")
            null
        }
    }

    fun stringDateTimeToDate(date: String): Date? {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
        return try {
            dateFormat.parse(date)
        } catch (ex: Exception) {
            Log.d(ConstantsUtils().TAG, "Erro convertendo data e hora string para Date")
            null
        }
    }
    fun validTime(horaInicial: String, horaFinal: String): Boolean {
        val formato = SimpleDateFormat("HH:mm", Locale.getDefault())

        val horaInicialDate = formato.parse(horaInicial)
        val horaFinalDate = formato.parse(horaFinal)

        return horaInicialDate!!.before(horaFinalDate)
    }
    fun formatDateTime(dateTimeString: String): String {
        val inputFormat = SimpleDateFormat(dateTime, Locale.getDefault())

        val outputFormat = SimpleDateFormat(dateTimeLayout, Locale.getDefault())
        outputFormat.timeZone = TimeZone.getDefault()

        val date = inputFormat.parse(dateTimeString)
        return outputFormat.format(date!!)
    }

    fun convertToUtcIso8601(inputDateTime: String?): String? {
        try {
            val inputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getDefault()

            val date = inputFormat.parse(inputDateTime) ?: return null

            val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            outputFormat.timeZone = TimeZone.getTimeZone("UTC")

            return outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            return null
        }
    }

}