package com.armada.expiryapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.armada.expiryapp.data.db.entity.TeamLink
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamLinkDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(link: TeamLink): Long

    @Query("DELETE FROM team_links WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM team_links WHERE merchandiserName = :name ORDER BY outletName ASC")
    fun getAllForMerchandiserFlow(name: String): Flow<List<TeamLink>>

    @Query("SELECT * FROM team_links WHERE merchandiserName = :name ORDER BY outletName ASC")
    suspend fun getAllForMerchandiser(name: String): List<TeamLink>

    @Query("SELECT COUNT(*) FROM team_links WHERE merchandiserName = :name")
    fun getCountFlow(name: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM team_links WHERE merchandiserName = :name")
    suspend fun getCount(name: String): Int

    @Query("SELECT * FROM team_links WHERE outletCode = :outletCode LIMIT 1")
    suspend fun findByOutletCode(outletCode: String): TeamLink?

    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM team_links
            WHERE outletCode = :outletCode AND merchandiserName = :merchandiserName
        )
    """)
    suspend fun isOutletLinked(outletCode: String, merchandiserName: String): Boolean
}
