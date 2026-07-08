package com.example.deezermusicdemo.utils

object TimeUtils {
    /**
     * Converts seconds to a formatted duration string.
     *
     * Examples:
     * 0 -> "0:00"
     * 5 -> "0:05"
     * 60 -> "1:00"
     * 65 -> "1:05"
     * 3599 -> "59:59"
     * 3600 -> "1:00:00"
     * 3665 -> "1:01:05"
     */
    fun secondToDuration(seconds: Long): String {
        val totalSeconds = seconds.coerceAtLeast(0)

        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val secs = totalSeconds % 60

        return if (hours > 0) {
            "%d:%02d:%02d".format(hours, minutes, secs)
        } else {
            "%d:%02d".format(minutes, secs)
        }
    }

    /**
     * Converts milliseconds to a formatted duration string.
     */
    fun millisecondToDuration(milliseconds: Long): String =
        secondToDuration(milliseconds / 1000)
}