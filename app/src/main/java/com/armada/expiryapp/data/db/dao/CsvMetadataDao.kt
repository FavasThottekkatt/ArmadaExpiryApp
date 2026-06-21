package com.armada.expiryapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.armada.expiryapp.data.db.entity.CsvMetadata
import kotlinx.coroutines.flow.Flow

@Dao
interface CsvMetadataDao {

    // REPLACE — CsvMetadata uses fileType as PrimaryKey; upsert overwrites on re-import
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(metadata: CsvMetadata)

    @Query("SELECT * FROM csv_metadata WHERE fileType = :fileType LIMIT 1")
    suspend fun getByType(fileType: String): CsvMetadata?

    @Query("SELECT * FROM csv_metadata")
    suspend fun getAll(): List<CsvMetadata>

    // Reactive — used by Settings screen Master Data panel
    @Query("SELECT * FROM csv_metadata")
    fun getAllFlow(): Flow<List<CsvMetadata>>
}
