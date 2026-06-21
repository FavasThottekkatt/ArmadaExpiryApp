package com.armada.expiryapp.data.repository

import com.armada.expiryapp.data.db.dao.StockEntryDao
import com.armada.expiryapp.data.db.entity.StockEntry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockEntryRepository @Inject constructor(private val dao: StockEntryDao) {

    suspend fun upsert(entry: StockEntry): Long = dao.upsert(entry)

    suspend fun deleteById(id: Long) = dao.deleteById(id)

    suspend fun getActiveEntries(
        outletCode: String,
        merchandiser: String,
        salesman: String,
    ): List<StockEntry> = dao.getActiveEntries(outletCode, merchandiser, salesman)

    suspend fun deleteAllForSession(
        outletCode: String,
        merchandiser: String,
        salesman: String,
    ) = dao.deleteAllForSession(outletCode, merchandiser, salesman)
}
