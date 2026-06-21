package com.armada.expiryapp.data.repository

import com.armada.expiryapp.data.db.dao.DeviceLockDao
import com.armada.expiryapp.data.db.entity.DeviceLock
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceLockRepository @Inject constructor(private val dao: DeviceLockDao) {
    suspend fun get(): DeviceLock?          = dao.get()
    fun getFlow(): Flow<DeviceLock?>        = dao.getFlow()
    suspend fun upsert(lock: DeviceLock)    = dao.upsert(lock)
}
