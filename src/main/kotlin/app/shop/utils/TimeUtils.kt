package app.shop.utils

import java.util.*

object TimeUtils {

    fun addMinutes(date: Date?, minutes: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.MINUTE, minutes)
        return calendar.time
    }
}