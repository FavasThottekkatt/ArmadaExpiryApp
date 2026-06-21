package com.armada.expiryapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.armada.expiryapp.data.db.entity.OutletItemLink
import kotlinx.coroutines.flow.Flow

@Dao
interface OutletItemLinkDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(link: OutletItemLink): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(links: List<OutletItemLink>)

    @Query("DELETE FROM outlet_item_links WHERE outletCode = :outletCode AND barcode = :barcode")
    suspend fun deleteByOutletAndBarcode(outletCode: String, barcode: String)

    @Query("DELETE FROM outlet_item_links WHERE outletCode = :outletCode")
    suspend fun deleteAllForOutlet(outletCode: String)

    @Query("SELECT * FROM outlet_item_links WHERE outletCode = :outletCode ORDER BY description ASC")
    suspend fun getAllForOutlet(outletCode: String): List<OutletItemLink>

    @Query("SELECT COUNT(*) FROM outlet_item_links WHERE outletCode = :outletCode")
    suspend fun getCountForOutlet(outletCode: String): Int

    @Query("SELECT COUNT(*) FROM outlet_item_links WHERE outletCode = :outletCode")
    fun getCountForOutletFlow(outletCode: String): Flow<Int>

    @Query("""
        SELECT * FROM outlet_item_links
        WHERE outletCode = :outletCode
          AND (description LIKE '%' || :query || '%'
               OR productCode  LIKE '%' || :query || '%')
        ORDER BY description ASC
        LIMIT 50
    """)
    suspend fun searchForDropdown(outletCode: String, query: String): List<OutletItemLink>

    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM outlet_item_links
            WHERE outletCode = :outletCode AND barcode = :barcode
        )
    """)
    suspend fun isLinked(outletCode: String, barcode: String): Boolean
}
