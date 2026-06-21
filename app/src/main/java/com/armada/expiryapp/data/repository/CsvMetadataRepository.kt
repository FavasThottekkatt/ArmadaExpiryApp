package com.armada.expiryapp.data.repository

import com.armada.expiryapp.data.db.dao.CsvMetadataDao
import com.armada.expiryapp.data.db.entity.CsvMetadata
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CsvMetadataRepository @Inject constructor(private val dao: CsvMetadataDao) {

    suspend fun upsert(metadata: CsvMetadata) = dao.upsert(metadata)

    suspend fun getByType(fileType: String): CsvMetadata? = dao.getByType(fileType)

    suspend fun getAll(): List<CsvMetadata> = dao.getAll()

    fun getAllFlow(): Flow<List<CsvMetadata>> = dao.getAllFlow()

    companion object {
        const val FILE_TYPE_ITEMS = "ITEMS"
        const val FILE_TYPE_OUTLETS = "OUTLETS"
    }
}
