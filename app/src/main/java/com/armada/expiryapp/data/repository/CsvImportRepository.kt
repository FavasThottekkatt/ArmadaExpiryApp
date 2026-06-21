package com.armada.expiryapp.data.repository

import com.armada.expiryapp.data.db.dao.CsvMetadataDao
import com.armada.expiryapp.data.db.dao.ItemDao
import com.armada.expiryapp.data.db.dao.OutletDao
import com.armada.expiryapp.data.db.entity.CsvMetadata
import com.armada.expiryapp.util.CsvParseResult
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CsvImportRepository @Inject constructor(
    private val itemDao:        ItemDao,
    private val outletDao:      OutletDao,
    private val csvMetadataDao: CsvMetadataDao,
) {

    suspend fun importData(parseResult: CsvParseResult) {
        val ts = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault())
            .format(Instant.now())

        itemDao.replaceAll(parseResult.items.validItems)
        outletDao.replaceAll(parseResult.outlets.validOutlets)

        csvMetadataDao.upsert(
            CsvMetadata(
                fileType    = CsvMetadataRepository.FILE_TYPE_ITEMS,
                importedAt  = ts,
                recordCount = parseResult.items.validItems.size,
                skippedRows = parseResult.items.skippedRows,
                fileHash    = parseResult.itemsMd5,
            )
        )
        csvMetadataDao.upsert(
            CsvMetadata(
                fileType    = CsvMetadataRepository.FILE_TYPE_OUTLETS,
                importedAt  = ts,
                recordCount = parseResult.outlets.validOutlets.size,
                skippedRows = parseResult.outlets.skippedRows,
                fileHash    = parseResult.outletsMd5,
            )
        )
    }
}
