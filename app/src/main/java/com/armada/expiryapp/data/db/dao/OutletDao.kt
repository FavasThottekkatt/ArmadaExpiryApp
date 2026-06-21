package com.armada.expiryapp.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.armada.expiryapp.data.db.entity.Outlet

@Dao
interface OutletDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(outlets: List<Outlet>)

    @Query("DELETE FROM outlets")
    suspend fun deleteAll()

    // Atomic delete-then-insert for re-import
    @Transaction
    suspend fun replaceAll(outlets: List<Outlet>) {
        deleteAll()
        insertAll(outlets)
    }

    @Query("""
        SELECT * FROM outlets
        WHERE outletName LIKE '%' || :query || '%'
           OR outletCode  LIKE '%' || :query || '%'
        ORDER BY outletName ASC
    """)
    fun searchByNameOrCode(query: String): PagingSource<Int, Outlet>

    @Query("""
        SELECT * FROM outlets
        WHERE outletName LIKE '%' || :query || '%'
           OR outletCode  LIKE '%' || :query || '%'
        ORDER BY outletName ASC
        LIMIT 20
    """)
    suspend fun searchForDropdown(query: String): List<Outlet>

    @Query("SELECT * FROM outlets ORDER BY outletName ASC")
    suspend fun getAll(): List<Outlet>

    @Query("SELECT * FROM outlets WHERE outletCode = :code LIMIT 1")
    suspend fun findByCode(code: String): Outlet?

    @Query("SELECT COUNT(*) FROM outlets")
    suspend fun getCount(): Int
}
