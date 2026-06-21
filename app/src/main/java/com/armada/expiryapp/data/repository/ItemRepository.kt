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
        val bc = barcode.trim()
        if (bc.isBlank()) return null

        // 1. Check LRU cache first (exact match)
        if (barcodeCache.containsKey(bc)) return barcodeCache[bc]

        // 2. Try exact match in Room
        var item = itemDao.findByBarcode(bc)
        if (item != null) { barcodeCache[bc] = item; return item }

        // 3. If 12 digits (UPC-A / EAN-13 with leading 0 stripped by ML Kit): try with leading 0
        if (bc.length == 12) {
            val withLeadingZero = "0$bc"
            item = itemDao.findByBarcode(withLeadingZero)
            if (item != null) { barcodeCache[bc] = item; return item }
        }

        // 4. If 13 digits starting with 0: try without leading 0 (UPC-A format)
        if (bc.length == 13 && bc.startsWith("0")) {
            val withoutLeadingZero = bc.substring(1)
            item = itemDao.findByBarcode(withoutLeadingZero)
            if (item != null) { barcodeCache[bc] = item; return item }
        }

        // 5. If shorter than 8 digits: try padded to 8 with leading zeros (EAN-8)
        if (bc.length < 8) {
            val padded = bc.padStart(8, '0')
            item = itemDao.findByBarcode(padded)
            if (item != null) { barcodeCache[bc] = item; return item }
        }

        // 6. If shorter than 13 and not already handled as 12: try padded to 13
        if (bc.length < 13 && bc.length != 12) {
            val padded13 = bc.padStart(13, '0')
            item = itemDao.findByBarcode(padded13)
            if (item != null) { barcodeCache[bc] = item; return item }
        }

        // 7. Strip all leading zeros and try the remaining digits
        val stripped = bc.trimStart('0')
        if (stripped.isNotBlank() && stripped != bc) {
            item = itemDao.findByBarcode(stripped)
            if (item != null) { barcodeCache[bc] = item; return item }
        }

        // Not found after all attempts
        barcodeCache[bc] = null
        return null
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
