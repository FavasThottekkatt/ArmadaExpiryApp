package com.armada.expiryapp.data.repository

import androidx.paging.PagingSource
import com.armada.expiryapp.data.db.dao.OutletDao
import com.armada.expiryapp.data.db.entity.Outlet
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OutletRepository @Inject constructor(private val outletDao: OutletDao) {

    suspend fun insertAll(outlets: List<Outlet>) = outletDao.insertAll(outlets)

    suspend fun replaceAll(outlets: List<Outlet>) = outletDao.replaceAll(outlets)

    fun searchByNameOrCode(query: String): PagingSource<Int, Outlet> =
        outletDao.searchByNameOrCode(query)

    suspend fun getAll(): List<Outlet> = outletDao.getAll()

    suspend fun findByCode(code: String): Outlet? = outletDao.findByCode(code)

    suspend fun getCount(): Int = outletDao.getCount()

    suspend fun searchForDropdown(query: String): List<Outlet> = outletDao.searchForDropdown(query)
}
