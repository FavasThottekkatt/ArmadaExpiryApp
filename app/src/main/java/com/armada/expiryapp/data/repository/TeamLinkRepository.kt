package com.armada.expiryapp.data.repository

import com.armada.expiryapp.data.db.dao.TeamLinkDao
import com.armada.expiryapp.data.db.entity.TeamLink
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeamLinkRepository @Inject constructor(private val dao: TeamLinkDao) {
    suspend fun insert(link: TeamLink): Long                          = dao.insert(link)
    suspend fun deleteById(id: Long)                                  = dao.deleteById(id)
    fun getAllForMerchandiserFlow(name: String): Flow<List<TeamLink>>  = dao.getAllForMerchandiserFlow(name)
    suspend fun getAllForMerchandiser(name: String): List<TeamLink>    = dao.getAllForMerchandiser(name)
    fun getCountFlow(name: String): Flow<Int>                         = dao.getCountFlow(name)
    suspend fun getCount(name: String): Int                           = dao.getCount(name)
    suspend fun findByOutletCode(outletCode: String): TeamLink?       = dao.findByOutletCode(outletCode)
    suspend fun isOutletLinked(outletCode: String, name: String): Boolean =
        dao.isOutletLinked(outletCode, name)
}
