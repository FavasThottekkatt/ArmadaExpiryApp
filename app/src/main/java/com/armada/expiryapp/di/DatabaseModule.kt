package com.armada.expiryapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.armada.expiryapp.data.db.AppDatabase
import com.armada.expiryapp.data.db.dao.CsvMetadataDao
import com.armada.expiryapp.data.db.dao.DeviceLockDao
import com.armada.expiryapp.data.db.dao.ExpiryEntryDao
import com.armada.expiryapp.data.db.dao.ItemDao
import com.armada.expiryapp.data.db.dao.OutletDao
import com.armada.expiryapp.data.db.dao.OutletItemLinkDao
import com.armada.expiryapp.data.db.dao.StockEntryDao
import com.armada.expiryapp.data.db.dao.TeamLinkDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `device_lock` (
                `id`               INTEGER NOT NULL PRIMARY KEY,
                `merchandiserName` TEXT NOT NULL,
                `lockedAt`         TEXT NOT NULL
            )
        """.trimIndent())
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `team_links` (
                `id`               INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `merchandiserName` TEXT NOT NULL,
                `salesmanName`     TEXT NOT NULL,
                `outletCode`       TEXT NOT NULL,
                `outletName`       TEXT NOT NULL
            )
        """.trimIndent())
        db.execSQL(
            "CREATE INDEX IF NOT EXISTS `index_team_links_outletCode` " +
            "ON `team_links`(`outletCode`)"
        )
    }
}

private val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `outlet_item_links` (
                `id`          INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `outletCode`  TEXT NOT NULL,
                `barcode`     TEXT NOT NULL,
                `description` TEXT NOT NULL,
                `productCode` TEXT
            )
        """.trimIndent())
        db.execSQL(
            "CREATE INDEX IF NOT EXISTS `index_outlet_item_links_outletCode` " +
            "ON `outlet_item_links`(`outletCode`)"
        )
        db.execSQL(
            "CREATE INDEX IF NOT EXISTS `index_outlet_item_links_barcode` " +
            "ON `outlet_item_links`(`barcode`)"
        )
    }
}

private val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `stock_entries` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `barcode` TEXT NOT NULL,
                `description` TEXT NOT NULL,
                `productCode` TEXT,
                `isOos` INTEGER NOT NULL DEFAULT 0,
                `quantity` INTEGER NOT NULL DEFAULT 0,
                `unit` TEXT NOT NULL DEFAULT 'PC',
                `outletName` TEXT NOT NULL,
                `outletCode` TEXT NOT NULL,
                `merchandiser` TEXT NOT NULL,
                `salesman` TEXT NOT NULL,
                `entryTimestamp` TEXT NOT NULL,
                `archived` INTEGER NOT NULL DEFAULT 0
            )
        """.trimIndent())
        db.execSQL(
            "CREATE INDEX IF NOT EXISTS `index_stock_entries_outletCode` " +
            "ON `stock_entries`(`outletCode`)"
        )
        db.execSQL(
            "CREATE INDEX IF NOT EXISTS `index_stock_entries_archived` " +
            "ON `stock_entries`(`archived`)"
        )
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "armada_expiry.db")
            .setJournalMode(RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING)
            .addMigrations(MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()

    @Provides fun provideItemDao(db: AppDatabase): ItemDao = db.itemDao()

    @Provides fun provideOutletDao(db: AppDatabase): OutletDao = db.outletDao()

    @Provides fun provideExpiryEntryDao(db: AppDatabase): ExpiryEntryDao = db.expiryEntryDao()

    @Provides fun provideCsvMetadataDao(db: AppDatabase): CsvMetadataDao = db.csvMetadataDao()

    @Provides fun provideStockEntryDao(db: AppDatabase): StockEntryDao = db.stockEntryDao()

    @Provides fun provideOutletItemLinkDao(db: AppDatabase): OutletItemLinkDao = db.outletItemLinkDao()

    @Provides fun provideDeviceLockDao(db: AppDatabase): DeviceLockDao = db.deviceLockDao()

    @Provides fun provideTeamLinkDao(db: AppDatabase): TeamLinkDao = db.teamLinkDao()
}
