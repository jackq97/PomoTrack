package com.jask.pomotrack.data.roomdatabase

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Converters {

    @TypeConverter
    fun fromString(value: String?): Date? {
        return value?.let {
            SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(value)
        }
    }

    @TypeConverter
    fun fromDate(date: Date?): String? {
        return date?.let {
            SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date)
        }
    }

}