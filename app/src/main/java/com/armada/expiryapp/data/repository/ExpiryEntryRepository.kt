package com.armada.expiryapp.data.repository

import androidx.paging.PagingSource
import com.armada.expiryapp.data.db.dao.ExpiryEntryDao
import com.armada.expiryapp.data.db.entity.ExpiryEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpiryEntryRepository @Inject constructor(private val dao: ExpiryEntryDao) {

    // ── Write ─────────────────────────────────────────────────────────────────

    suspend fun insert(entry: ExpiryEntry): Long = dao.insert(entry)

    suspend fun update(entry: ExpiryEntry) = dao.update(entry)

    suspend fun delete(entry: ExpiryEntry) = dao.delete(entry)

    suspend fun deleteById(id: Long) = dao.deleteById(id)

    // ── Active entries ────────────────────────────────────────────────────────

    fun getActiveEntriesPaged(outletCode: String): PagingSource<Int, ExpiryEntry> =
        dao.getActiveEntriesPaged(outletCode)

    fun searchActiveEntriesPaged(outletCode: String, query: String): PagingSource<Int, ExpiryEntry> =
        dao.searchActiveEntriesPaged(outletCode, query)

    // ── Dashboard stat flows ──────────────────────────────────────────────────

    fun getExpiredCountFlow(outletCode: String, today: String): Flow<Int> =
        dao.getExpiredCountFlow(outletCode, today)

    fun getWithin30CountFlow(outletCode: String, today: String, d30: String): Flow<Int> =
        dao.getWithin30CountFlow(outletCode, today, d30)

    fun getWithin60CountFlow(outletCode: String, d30: String, d60: String): Flow<Int> =
        dao.getWithin60CountFlow(outletCode, d30, d60)

    fun getWithin90CountFlow(outletCode: String, d60: String, d90: String): Flow<Int> =
        dao.getWithin90CountFlow(outletCode, d60, d90)

    // ── One-shot stat counts ──────────────────────────────────────────────────

    suspend fun getExpiredCount(outletCode: String, today: String): Int =
        dao.getExpiredCount(outletCode, today)

    suspend fun getWithin30Count(outletCode: String, today: String, d30: String): Int =
        dao.getWithin30Count(outletCode, today, d30)

    // ── Single-row fetch ─────────────────────────────────────────────────────

    suspend fun findById(id: Long): ExpiryEntry? = dao.findById(id)

    // ── Duplicate guard ───────────────────────────────────────────────────────

    suspend fun findDuplicate(
        barcode: String,
        outletCode: String,
        expiryDate: String,
        merchandiser: String
    ): ExpiryEntry? = dao.findDuplicate(barcode, outletCode, expiryDate, merchandiser)

    suspend fun incrementQuantity(id: Long, additionalQty: Int) =
        dao.incrementQuantity(id, additionalQty)

    // ── Archive ───────────────────────────────────────────────────────────────

    suspend fun archiveForOutletMonth(outletCode: String, monthPrefix: String) =
        dao.archiveForOutletMonth(outletCode, monthPrefix)

    suspend fun archiveAllActive() = dao.archiveAllActive()

    fun getArchivedEntriesPaged(outletCode: String): PagingSource<Int, ExpiryEntry> =
        dao.getArchivedEntriesPaged(outletCode)

    suspend fun getArchivedEntries(outletCode: String): List<ExpiryEntry> =
        dao.getArchivedEntries(outletCode)

    fun getArchivedCountFlow(outletCode: String): Flow<Int> =
        dao.getArchivedCountFlow(outletCode)

    // ── Excel export ──────────────────────────────────────────────────────────

    suspend fun getEntriesForExport(
        outletCode: String,
        merchandiser: String,
        salesman: String,
        monthPrefix: String
    ): List<ExpiryEntry> = dao.getEntriesForExport(outletCode, merchandiser, salesman, monthPrefix)

    suspend fun getAllEntriesForMerchandiserMonth(
        merchandiser: String,
        salesman: String,
        monthPrefix: String,
    ): List<ExpiryEntry> = dao.getAllEntriesForMerchandiserMonth(merchandiser, salesman, monthPrefix)

    // ── Session recovery ──────────────────────────────────────────────────────

    suspend fun getLastActiveEntry(): ExpiryEntry? = dao.getLastActiveEntry()

    suspend fun getActiveCountForOutlet(outletCode: String): Int =
        dao.getActiveCountForOutlet(outletCode)

    suspend fun getTotalActiveCount(): Int = dao.getTotalActiveCount()

    // ── LRU cache pre-warm (Phase 8) ──────────────────────────────────────────

    suspend fun getRecentEntries(limit: Int): List<ExpiryEntry> = dao.getRecentEntries(limit)
}
