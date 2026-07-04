package com.example.deezermusicdemo.utils

object TimeUtils {
    /**
     * Convert seconds to a formatted time string.
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
    fun secondToDuration(seconds: Int): String {
        if (seconds <= 0) return "0:00"

        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val remainingSeconds = seconds % 60

        return if (hours > 0) {
            String.format(
                "%d:%02d:%02d",
                hours,
                minutes,
                remainingSeconds
            )
        } else {
            String.format(
                "%d:%02d",
                minutes,
                remainingSeconds
            )
        }
    }
}