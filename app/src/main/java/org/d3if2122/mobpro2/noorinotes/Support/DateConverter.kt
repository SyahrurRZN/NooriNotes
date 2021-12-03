package org.d3if2122.mobpro2.noorinotes.Support

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream
import java.util.*

class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?):Date? = if(null == value) null else Date(value)

    @TypeConverter
    fun dateToTimestamp(date: Date?):Long?{
        return date?.time?.toLong()
    }

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray? {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,10,outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
    }
}