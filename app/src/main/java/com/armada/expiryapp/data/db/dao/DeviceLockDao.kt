package com.armada.expiryapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.armada.expiryapp.data.db.entity.DeviceLock
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceLockDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(lock: DeviceLock)

    @Query("SELECT * FROM device_lock WHERE id = 1 LIMIT 1")
    suspend fun get(): DeviceLock?

    @Query("SELECT * FROM device_lock WHERE id = 1 LIMIT 1")
    fun getFlow(): Flow<DeviceLock?>
}
