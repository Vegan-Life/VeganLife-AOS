package com.project.veganlife.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateTime(input: String): String {
    val dateTimePart = input.substring(0, 16)
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
    val dateTime = LocalDateTime.parse(dateTimePart, inputFormatter)
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd a hh:mm", Locale.getDefault())
    return dateTime.format(outputFormatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun getCurrentTimestamp(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return LocalDateTime.now().format(formatter)
}
