package com.armada.expiryapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.armada.expiryapp.data.db.entity.StockEntry

@Dao
interface StockEntryDao {

    // id=0 → INSERT (auto-generate id). id>0 → REPLACE existing row.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: StockEntry): Long

    @Query("DELETE FROM stock_entries WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("""
        SELECT * FROM stock_entries
        WHERE outletCode   = :outletCode
          AND merchandiser = :merchandiser
          AND salesman     = :salesman
          AND archived = 0
        ORDER BY description ASC
    """)
    suspend fun getActiveEntries(
        outletCode: String,
        merchandiser: String,
        salesman: String,
    ): List<StockEntry>

    @Query("""
        DELETE FROM stock_entries
        WHERE outletCode   = :outletCode
          AND merchandiser = :merchandiser
          AND salesman     = :salesman
          AND archived = 0
    """)
    suspend fun deleteAllForSession(
        outletCode: String,
        merchandiser: String,
        salesman: String,
    )

    @Query("""
        UPDATE stock_entries SET archived = 1
        WHERE outletCode = :outletCode AND archived = 0
    """)
    suspend fun archiveForOutlet(outletCode: String)
}
