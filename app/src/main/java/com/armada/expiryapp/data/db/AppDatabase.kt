package com.armada.expiryapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.armada.expiryapp.data.db.dao.CsvMetadataDao
import com.armada.expiryapp.data.db.dao.DeviceLockDao
import com.armada.expiryapp.data.db.dao.ExpiryEntryDao
import com.armada.expiryapp.data.db.dao.ItemDao
import com.armada.expiryapp.data.db.dao.OutletDao
import com.armada.expiryapp.data.db.dao.OutletItemLinkDao
import com.armada.expiryapp.data.db.dao.StockEntryDao
import com.armada.expiryapp.data.db.dao.TeamLinkDao
import com.armada.expiryapp.data.db.entity.CsvMetadata
import com.armada.expiryapp.data.db.entity.DeviceLock
import com.armada.expiryapp.data.db.entity.ExpiryEntry
import com.armada.expiryapp.data.db.entity.Item
import com.armada.expiryapp.data.db.entity.Outlet
import com.armada.expiryapp.data.db.entity.OutletItemLink
import com.armada.expiryapp.data.db.entity.StockEntry
import com.armada.expiryapp.data.db.entity.TeamLink

@Database(
    entities = [
        Item::class,
        Outlet::class,
        ExpiryEntry::class,
        CsvMetadata::class,
        StockEntry::class,
        OutletItemLink::class,
        DeviceLock::class,
        TeamLink::class,
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun outletDao(): OutletDao
    abstract fun expiryEntryDao(): ExpiryEntryDao
    abstract fun csvMetadataDao(): CsvMetadataDao
    abstract fun stockEntryDao(): StockEntryDao
    abstract fun outletItemLinkDao(): OutletItemLinkDao
    abstract fun deviceLockDao(): DeviceLockDao
    abstract fun teamLinkDao(): TeamLinkDao
}
