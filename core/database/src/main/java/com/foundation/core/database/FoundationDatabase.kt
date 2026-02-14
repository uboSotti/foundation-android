package com.foundation.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.foundation.core.database.converter.DateConverter

/**
 * 앱의 Room Database 정의.
 *
 * 새로운 Entity와 Dao를 추가할 때 이 클래스에 등록한다.
 * 'PlaceholderEntity'는 빌드 가능 상태를 유지하기 위한 것이며,
 * 실제 Entity 추가 후 제거한다.
 */
@Database(
    entities = [PlaceholderEntity::class],
    version = 1,
    exportSchema = true,
)
@TypeConverters(DateConverter::class)
abstract class FoundationDatabase : RoomDatabase()
