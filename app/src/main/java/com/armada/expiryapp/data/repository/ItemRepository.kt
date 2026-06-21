package com.armada.expiryapp.data.repository

import androidx.paging.PagingSource
import com.armada.expiryapp.data.db.dao.ItemDao
import com.armada.expiryapp.data.db.entity.ExpiryEntry
import com.armada.expiryapp.data.db.entity.Item
import java.util.Collections
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemRepository @Inject constructor(private val itemDao: ItemDao) {

    // NOTE: Manual barcode/description entries are NEVER written back to CSV source files.
    // They exist only in Room database for the current month's reporting period.

    // LRU cache — 100 items, access-order eviction, synchronised for Dispatchers.IO thread pool
    private val barcodeCache: MutableMap<String, Item?> = Collections.synchronizedMap(
        object : LinkedHashMap<String, Item?>(100, 0.75f, true) {
            override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, Item?>) = size > 100
        }
    )

    fun clearCache() = barcodeCache.clear()

    suspend fun findByBarcode(barcode: String): Item? {
        // containsKey handles null values correctly (item not found → null stored, skips DB next time)
        if (barcodeCache.containsKey(barcode)) return barcodeCache[barcode]
        val item = itemDao.findByBarcode(barcode)
        barcodeCache[barcode] = item
        return item
    }

    suspend fun findByProductCode(code: String): Item? = itemDao.findByProductCode(code)

    fun getAllPaged(): PagingSource<Int, Item> = itemDao.getAllPaged()

    fun searchByDescriptionOrCode(query: String): PagingSource<Int, Item> =
        itemDao.searchByDescriptionOrCode(query)

    suspend fun searchForDropdown(query: String): List<Item> =
        itemDao.searchForDropdown(query)

    suspend fun getAll(): List<Item> = itemDao.getAll()

    suspend fun searchAll(query: String): List<Item> = itemDao.searchAll(query)

    suspend fun getCount(): Int = itemDao.getCount()

    suspend fun preloadRecentBarcodes(recentEntries: List<ExpiryEntry>) {
        recentEntries.forEach { entry ->
            if (!barcodeCache.containsKey(entry.barcode)) {
                runCatching {
                    val item = itemDao.findByBarcode(entry.barcode)
                    barcodeCache[entry.barcode] = item
                }
            }
        }
    }
}
