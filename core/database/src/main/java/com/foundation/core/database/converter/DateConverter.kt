package com.foundation.core.database.converter

import androidx.room.TypeConverter
import java.util.Date

/**
 * Room에서 [Date] 타입을 Long으로 변환하기 위한 TypeConverter.
 *
 * 추가적인 TypeConverter가 필요하면 이 패키지에 생성한다.
 */
class DateConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}
