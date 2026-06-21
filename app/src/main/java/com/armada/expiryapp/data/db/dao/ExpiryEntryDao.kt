package com.armada.expiryapp.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.armada.expiryapp.data.db.entity.ExpiryEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpiryEntryDao {

    // ── Write operations ─────────────────────────────────────────────────────

    @Insert
    suspend fun insert(entry: ExpiryEntry): Long

    @Update
    suspend fun update(entry: ExpiryEntry)

    @Delete
    suspend fun delete(entry: ExpiryEntry)

    @Query("DELETE FROM expiry_entries WHERE id = :id")
    suspend fun deleteById(id: Long)

    // ── Active entries (New Entry table + Dashboard) ─────────────────────────

    @Query("""
        SELECT * FROM expiry_entries
        WHERE outletCode = :outletCode AND archived = 0
        ORDER BY expiryDate ASC
    """)
    fun getActiveEntriesPaged(outletCode: String): PagingSource<Int, ExpiryEntry>

    @Query("""
        SELECT * FROM expiry_entries
        WHERE outletCode = :outletCode AND archived = 0
          AND (description LIKE '%' || :query || '%' OR productCode LIKE '%' || :query || '%')
        ORDER BY expiryDate ASC
    """)
    fun searchActiveEntriesPaged(outletCode: String, query: String): PagingSource<Int, ExpiryEntry>

    // ── Dashboard stat cards — reactive Flow ─────────────────────────────────
    // Caller computes date strings: today = yyyy-MM-dd, d30/d60/d90 = today + N days

    @Query("""
        SELECT COUNT(*) FROM expiry_entries
        WHERE outletCode = :outletCode AND archived = 0 AND expiryDate < :today
    """)
    fun getExpiredCountFlow(outletCode: String, today: String): Flow<Int>

    @Query("""
        SELECT COUNT(*) FROM expiry_entries
        WHERE outletCode = :outletCode AND archived = 0
          AND expiryDate >= :today AND expiryDate <= :d30
    """)
    fun getWithin30CountFlow(outletCode: String, today: String, d30: String): Flow<Int>

    @Query("""
        SELECT COUNT(*) FROM expiry_entries
        WHERE outletCode = :outletCode AND archived = 0
          AND expiryDate > :d30 AND expiryDate <= :d60
    """)
    fun getWithin60CountFlow(outletCode: String, d30: String, d60: String): Flow<Int>

    @Query("""
        SELECT COUNT(*) FROM expiry_entries
        WHERE outletCode = :outletCode AND archived = 0
          AND expiryDate > :d60 AND expiryDate <= :d90
    """)
    fun getWithin90CountFlow(outletCode: String, d60: String, d90: String): Flow<Int>

    // ── One-shot stat queries (Reports summary card) ──────────────────────────

    @Query("""
        SELECT COUNT(*) FROM expiry_entries
        WHERE outletCode = :outletCode AND archived = 0 AND expiryDate < :today
    """)
    suspend fun getExpiredCount(outletCode: String, today: String): Int

    @Query("""
        SELECT COUNT(*) FROM expiry_entries
        WHERE outletCode = :outletCode AND archived = 0
          AND expiryDate >= :today AND expiryDate <= :d30
    """)
    suspend fun getWithin30Count(outletCode: String, today: String, d30: String): Int

    // ── Single-row fetch ─────────────────────────────────────────────────────

    @Query("SELECT * FROM expiry_entries WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): ExpiryEntry?

    // ── Duplicate guard ───────────────────────────────────────────────────────

    @Query("""
        SELECT * FROM expiry_entries
        WHERE barcode      = :barcode
          AND outletCode   = :outletCode
          AND expiryDate   = :expiryDate
          AND merchandiser = :merchandiser
          AND archived = 0
        LIMIT 1
    """)
    suspend fun findDuplicate(
        barcode: String,
        outletCode: String,
        expiryDate: String,
        merchandiser: String
    ): ExpiryEntry?

    @Query("UPDATE expiry_entries SET quantity = quantity + :additionalQty WHERE id = :id")
    suspend fun incrementQuantity(id: Long, additionalQty: Int)

    // ── Archive ───────────────────────────────────────────────────────────────

    // monthPrefix = "yyyy-MM" — matches entryTimestamp stored as "yyyy-MM-dd HH:mm:ss"
    @Query("""
        UPDATE expiry_entries SET archived = 1
        WHERE outletCode = :outletCode AND archived = 0
          AND entryTimestamp LIKE :monthPrefix || '%'
    """)
    suspend fun archiveForOutletMonth(outletCode: String, monthPrefix: String)

    @Query("UPDATE expiry_entries SET archived = 1 WHERE archived = 0")
    suspend fun archiveAllActive()

    @Query("""
        SELECT * FROM expiry_entries
        WHERE outletCode = :outletCode AND archived = 1
        ORDER BY expiryDate ASC
    """)
    fun getArchivedEntriesPaged(outletCode: String): PagingSource<Int, ExpiryEntry>

    @Query("""
        SELECT * FROM expiry_entries
        WHERE outletCode = :outletCode AND archived = 1
        ORDER BY expiryDate ASC
    """)
    suspend fun getArchivedEntries(outletCode: String): List<ExpiryEntry>

    @Query("SELECT COUNT(*) FROM expiry_entries WHERE outletCode = :outletCode AND archived = 1")
    fun getArchivedCountFlow(outletCode: String): Flow<Int>

    // ── Excel export ──────────────────────────────────────────────────────────

    // monthPrefix = "yyyy-MM"
    @Query("""
        SELECT * FROM expiry_entries
        WHERE outletCode   = :outletCode
          AND merchandiser = :merchandiser
          AND salesman     = :salesman
          AND archived = 0
          AND entryTimestamp LIKE :monthPrefix || '%'
        ORDER BY expiryDate ASC
    """)
    suspend fun getEntriesForExport(
        outletCode: String,
        merchandiser: String,
        salesman: String,
        monthPrefix: String
    ): List<ExpiryEntry>

    // ── All-outlets text report ───────────────────────────────────────────────

    @Query("""
        SELECT * FROM expiry_entries
        WHERE merchandiser = :merchandiser
          AND salesman     = :salesman
          AND archived = 0
          AND entryTimestamp LIKE :monthPrefix || '%'
        ORDER BY outletName ASC, expiryDate ASC
    """)
    suspend fun getAllEntriesForMerchandiserMonth(
        merchandiser: String,
        salesman: String,
        monthPrefix: String,
    ): List<ExpiryEntry>

    // ── Session recovery ──────────────────────────────────────────────────────

    @Query("SELECT * FROM expiry_entries WHERE archived = 0 ORDER BY entryTimestamp DESC LIMIT 1")
    suspend fun getLastActiveEntry(): ExpiryEntry?

    @Query("SELECT COUNT(*) FROM expiry_entries WHERE archived = 0 AND outletCode = :outletCode")
    suspend fun getActiveCountForOutlet(outletCode: String): Int

    @Query("SELECT COUNT(*) FROM expiry_entries WHERE archived = 0")
    suspend fun getTotalActiveCount(): Int

    // ── LRU cache pre-warm (Phase 8) ──────────────────────────────────────────

    @Query("SELECT * FROM expiry_entries ORDER BY entryTimestamp DESC LIMIT :limit")
    suspend fun getRecentEntries(limit: Int): List<ExpiryEntry>
}
