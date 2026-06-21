package com.armada.expiryapp.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.armada.expiryapp.data.db.entity.Item

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(items: List<Item>)

    @Query("DELETE FROM items")
    suspend fun deleteAll()

    // Atomic delete-then-insert for re-import
    @Transaction
    suspend fun replaceAll(items: List<Item>) {
        deleteAll()
        insertAll(items)
    }

    @Query("SELECT * FROM items WHERE barcode = :barcode LIMIT 1")
    suspend fun findByBarcode(barcode: String): Item?

    @Query("SELECT * FROM items WHERE productCode = :code LIMIT 1")
    suspend fun findByProductCode(code: String): Item?

    @Query("SELECT * FROM items ORDER BY description ASC")
    fun getAllPaged(): PagingSource<Int, Item>

    @Query("""
        SELECT * FROM items
        WHERE description LIKE '%' || :query || '%'
           OR productCode  LIKE '%' || :query || '%'
        ORDER BY description ASC
    """)
    fun searchByDescriptionOrCode(query: String): PagingSource<Int, Item>

    @Query("""
        SELECT * FROM items
        WHERE description LIKE '%' || :query || '%'
           OR productCode  LIKE '%' || :query || '%'
        ORDER BY description ASC
        LIMIT 50
    """)
    suspend fun searchForDropdown(query: String): List<Item>

    @Query("SELECT * FROM items ORDER BY description ASC")
    suspend fun getAll(): List<Item>

    @Query("""
        SELECT * FROM items
        WHERE description LIKE '%' || :query || '%'
           OR productCode  LIKE '%' || :query || '%'
        ORDER BY description ASC
    """)
    suspend fun searchAll(query: String): List<Item>

    @Query("SELECT COUNT(*) FROM items")
    suspend fun getCount(): Int
}
