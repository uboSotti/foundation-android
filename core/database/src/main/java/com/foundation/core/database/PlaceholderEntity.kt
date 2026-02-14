package com.foundation.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 빌드 가능 상태를 유지하기 위한 Placeholder Entity.
 *
 * 실제 Entity를 추가한 후 이 클래스와 Database 등록을 제거한다.
 */
@Entity(tableName = "placeholder")
data class PlaceholderEntity(
    @PrimaryKey
    val id: Long = 0L,
)
