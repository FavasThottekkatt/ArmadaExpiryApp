package com.armada.expiryapp.data.repository

import com.armada.expiryapp.data.db.dao.OutletItemLinkDao
import com.armada.expiryapp.data.db.entity.OutletItemLink
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OutletItemLinkRepository @Inject constructor(private val dao: OutletItemLinkDao) {

    suspend fun insert(link: OutletItemLink): Long = dao.insert(link)

    suspend fun insertAll(links: List<OutletItemLink>) = dao.insertAll(links)

    suspend fun deleteByOutletAndBarcode(outletCode: String, barcode: String) =
        dao.deleteByOutletAndBarcode(outletCode, barcode)

    suspend fun deleteAllForOutlet(outletCode: String) = dao.deleteAllForOutlet(outletCode)

    suspend fun getAllForOutlet(outletCode: String): List<OutletItemLink> =
        dao.getAllForOutlet(outletCode)

    suspend fun getCountForOutlet(outletCode: String): Int = dao.getCountForOutlet(outletCode)

    fun getCountForOutletFlow(outletCode: String): Flow<Int> = dao.getCountForOutletFlow(outletCode)

    suspend fun searchForDropdown(outletCode: String, query: String): List<OutletItemLink> =
        dao.searchForDropdown(outletCode, query)

    suspend fun isLinked(outletCode: String, barcode: String): Boolean =
        dao.isLinked(outletCode, barcode)
}
