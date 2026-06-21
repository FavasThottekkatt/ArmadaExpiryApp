package com.armada.expiryapp.data.db;

import androidx.annotation.NonNull;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenDelegate;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.SQLite;
import androidx.sqlite.SQLiteConnection;
import com.armada.expiryapp.data.db.dao.CsvMetadataDao;
import com.armada.expiryapp.data.db.dao.CsvMetadataDao_Impl;
import com.armada.expiryapp.data.db.dao.DeviceLockDao;
import com.armada.expiryapp.data.db.dao.DeviceLockDao_Impl;
import com.armada.expiryapp.data.db.dao.ExpiryEntryDao;
import com.armada.expiryapp.data.db.dao.ExpiryEntryDao_Impl;
import com.armada.expiryapp.data.db.dao.ItemDao;
import com.armada.expiryapp.data.db.dao.ItemDao_Impl;
import com.armada.expiryapp.data.db.dao.OutletDao;
import com.armada.expiryapp.data.db.dao.OutletDao_Impl;
import com.armada.expiryapp.data.db.dao.OutletItemLinkDao;
import com.armada.expiryapp.data.db.dao.OutletItemLinkDao_Impl;
import com.armada.expiryapp.data.db.dao.StockEntryDao;
import com.armada.expiryapp.data.db.dao.StockEntryDao_Impl;
import com.armada.expiryapp.data.db.dao.TeamLinkDao;
import com.armada.expiryapp.data.db.dao.TeamLinkDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile ItemDao _itemDao;

  private volatile OutletDao _outletDao;

  private volatile ExpiryEntryDao _expiryEntryDao;

  private volatile CsvMetadataDao _csvMetadataDao;

  private volatile StockEntryDao _stockEntryDao;

  private volatile OutletItemLinkDao _outletItemLinkDao;

  private volatile DeviceLockDao _deviceLockDao;

  private volatile TeamLinkDao _teamLinkDao;

  @Override
  @NonNull
  protected RoomOpenDelegate createOpenDelegate() {
    final RoomOpenDelegate _openDelegate = new RoomOpenDelegate(5, "b2bef08f9bc8506b85fc4709454b4241", "1ad32a1f5a8c04a80c86f421b14da530") {
      @Override
      public void createAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `barcode` TEXT NOT NULL, `description` TEXT NOT NULL, `productCode` TEXT)");
        SQLite.execSQL(connection, "CREATE UNIQUE INDEX IF NOT EXISTS `index_items_barcode` ON `items` (`barcode`)");
        SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_items_description` ON `items` (`description`)");
        SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_items_productCode` ON `items` (`productCode`)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `outlets` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `outletCode` TEXT NOT NULL, `outletName` TEXT NOT NULL, `shortName` TEXT NOT NULL)");
        SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_outlets_outletName` ON `outlets` (`outletName`)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `expiry_entries` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `barcode` TEXT NOT NULL, `description` TEXT NOT NULL, `productCode` TEXT, `expiryDate` TEXT NOT NULL, `quantity` INTEGER NOT NULL, `unit` TEXT NOT NULL, `outletName` TEXT NOT NULL, `outletCode` TEXT NOT NULL, `merchandiser` TEXT NOT NULL, `salesman` TEXT NOT NULL, `entryTimestamp` TEXT NOT NULL, `archived` INTEGER NOT NULL)");
        SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_expiry_entries_expiryDate` ON `expiry_entries` (`expiryDate`)");
        SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_expiry_entries_archived` ON `expiry_entries` (`archived`)");
        SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_expiry_entries_outletCode` ON `expiry_entries` (`outletCode`)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `csv_metadata` (`fileType` TEXT NOT NULL, `importedAt` TEXT NOT NULL, `recordCount` INTEGER NOT NULL, `skippedRows` INTEGER NOT NULL, `fileHash` TEXT NOT NULL, PRIMARY KEY(`fileType`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `stock_entries` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `barcode` TEXT NOT NULL, `description` TEXT NOT NULL, `productCode` TEXT, `isOos` INTEGER NOT NULL, `quantity` INTEGER NOT NULL, `unit` TEXT NOT NULL, `outletName` TEXT NOT NULL, `outletCode` TEXT NOT NULL, `merchandiser` TEXT NOT NULL, `salesman` TEXT NOT NULL, `entryTimestamp` TEXT NOT NULL, `archived` INTEGER NOT NULL)");
        SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_stock_entries_outletCode` ON `stock_entries` (`outletCode`)");
        SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_stock_entries_archived` ON `stock_entries` (`archived`)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `outlet_item_links` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `outletCode` TEXT NOT NULL, `barcode` TEXT NOT NULL, `description` TEXT NOT NULL, `productCode` TEXT)");
        SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_outlet_item_links_outletCode` ON `outlet_item_links` (`outletCode`)");
        SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_outlet_item_links_barcode` ON `outlet_item_links` (`barcode`)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `device_lock` (`id` INTEGER NOT NULL, `merchandiserName` TEXT NOT NULL, `lockedAt` TEXT NOT NULL, PRIMARY KEY(`id`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `team_links` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `merchandiserName` TEXT NOT NULL, `salesmanName` TEXT NOT NULL, `outletCode` TEXT NOT NULL, `outletName` TEXT NOT NULL)");
        SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_team_links_outletCode` ON `team_links` (`outletCode`)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        SQLite.execSQL(connection, "INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'b2bef08f9bc8506b85fc4709454b4241')");
      }

      @Override
      public void dropAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `items`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `outlets`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `expiry_entries`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `csv_metadata`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `stock_entries`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `outlet_item_links`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `device_lock`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `team_links`");
      }

      @Override
      public void onCreate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      public void onOpen(@NonNull final SQLiteConnection connection) {
        internalInitInvalidationTracker(connection);
      }

      @Override
      public void onPreMigrate(@NonNull final SQLiteConnection connection) {
        DBUtil.dropFtsSyncTriggers(connection);
      }

      @Override
      public void onPostMigrate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      @NonNull
      public RoomOpenDelegate.ValidationResult onValidateSchema(
          @NonNull final SQLiteConnection connection) {
        final Map<String, TableInfo.Column> _columnsItems = new HashMap<String, TableInfo.Column>(4);
        _columnsItems.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsItems.put("barcode", new TableInfo.Column("barcode", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsItems.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsItems.put("productCode", new TableInfo.Column("productCode", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysItems = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesItems = new HashSet<TableInfo.Index>(3);
        _indicesItems.add(new TableInfo.Index("index_items_barcode", true, Arrays.asList("barcode"), Arrays.asList("ASC")));
        _indicesItems.add(new TableInfo.Index("index_items_description", false, Arrays.asList("description"), Arrays.asList("ASC")));
        _indicesItems.add(new TableInfo.Index("index_items_productCode", false, Arrays.asList("productCode"), Arrays.asList("ASC")));
        final TableInfo _infoItems = new TableInfo("items", _columnsItems, _foreignKeysItems, _indicesItems);
        final TableInfo _existingItems = TableInfo.read(connection, "items");
        if (!_infoItems.equals(_existingItems)) {
          return new RoomOpenDelegate.ValidationResult(false, "items(com.armada.expiryapp.data.db.entity.Item).\n"
                  + " Expected:\n" + _infoItems + "\n"
                  + " Found:\n" + _existingItems);
        }
        final Map<String, TableInfo.Column> _columnsOutlets = new HashMap<String, TableInfo.Column>(4);
        _columnsOutlets.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOutlets.put("outletCode", new TableInfo.Column("outletCode", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOutlets.put("outletName", new TableInfo.Column("outletName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOutlets.put("shortName", new TableInfo.Column("shortName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysOutlets = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesOutlets = new HashSet<TableInfo.Index>(1);
        _indicesOutlets.add(new TableInfo.Index("index_outlets_outletName", false, Arrays.asList("outletName"), Arrays.asList("ASC")));
        final TableInfo _infoOutlets = new TableInfo("outlets", _columnsOutlets, _foreignKeysOutlets, _indicesOutlets);
        final TableInfo _existingOutlets = TableInfo.read(connection, "outlets");
        if (!_infoOutlets.equals(_existingOutlets)) {
          return new RoomOpenDelegate.ValidationResult(false, "outlets(com.armada.expiryapp.data.db.entity.Outlet).\n"
                  + " Expected:\n" + _infoOutlets + "\n"
                  + " Found:\n" + _existingOutlets);
        }
        final Map<String, TableInfo.Column> _columnsExpiryEntries = new HashMap<String, TableInfo.Column>(13);
        _columnsExpiryEntries.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpiryEntries.put("barcode", new TableInfo.Column("barcode", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpiryEntries.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpiryEntries.put("productCode", new TableInfo.Column("productCode", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpiryEntries.put("expiryDate", new TableInfo.Column("expiryDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpiryEntries.put("quantity", new TableInfo.Column("quantity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpiryEntries.put("unit", new TableInfo.Column("unit", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpiryEntries.put("outletName", new TableInfo.Column("outletName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpiryEntries.put("outletCode", new TableInfo.Column("outletCode", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpiryEntries.put("merchandiser", new TableInfo.Column("merchandiser", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpiryEntries.put("salesman", new TableInfo.Column("salesman", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpiryEntries.put("entryTimestamp", new TableInfo.Column("entryTimestamp", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpiryEntries.put("archived", new TableInfo.Column("archived", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysExpiryEntries = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesExpiryEntries = new HashSet<TableInfo.Index>(3);
        _indicesExpiryEntries.add(new TableInfo.Index("index_expiry_entries_expiryDate", false, Arrays.asList("expiryDate"), Arrays.asList("ASC")));
        _indicesExpiryEntries.add(new TableInfo.Index("index_expiry_entries_archived", false, Arrays.asList("archived"), Arrays.asList("ASC")));
        _indicesExpiryEntries.add(new TableInfo.Index("index_expiry_entries_outletCode", false, Arrays.asList("outletCode"), Arrays.asList("ASC")));
        final TableInfo _infoExpiryEntries = new TableInfo("expiry_entries", _columnsExpiryEntries, _foreignKeysExpiryEntries, _indicesExpiryEntries);
        final TableInfo _existingExpiryEntries = TableInfo.read(connection, "expiry_entries");
        if (!_infoExpiryEntries.equals(_existingExpiryEntries)) {
          return new RoomOpenDelegate.ValidationResult(false, "expiry_entries(com.armada.expiryapp.data.db.entity.ExpiryEntry).\n"
                  + " Expected:\n" + _infoExpiryEntries + "\n"
                  + " Found:\n" + _existingExpiryEntries);
        }
        final Map<String, TableInfo.Column> _columnsCsvMetadata = new HashMap<String, TableInfo.Column>(5);
        _columnsCsvMetadata.put("fileType", new TableInfo.Column("fileType", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCsvMetadata.put("importedAt", new TableInfo.Column("importedAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCsvMetadata.put("recordCount", new TableInfo.Column("recordCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCsvMetadata.put("skippedRows", new TableInfo.Column("skippedRows", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCsvMetadata.put("fileHash", new TableInfo.Column("fileHash", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysCsvMetadata = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesCsvMetadata = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCsvMetadata = new TableInfo("csv_metadata", _columnsCsvMetadata, _foreignKeysCsvMetadata, _indicesCsvMetadata);
        final TableInfo _existingCsvMetadata = TableInfo.read(connection, "csv_metadata");
        if (!_infoCsvMetadata.equals(_existingCsvMetadata)) {
          return new RoomOpenDelegate.ValidationResult(false, "csv_metadata(com.armada.expiryapp.data.db.entity.CsvMetadata).\n"
                  + " Expected:\n" + _infoCsvMetadata + "\n"
                  + " Found:\n" + _existingCsvMetadata);
        }
        final Map<String, TableInfo.Column> _columnsStockEntries = new HashMap<String, TableInfo.Column>(13);
        _columnsStockEntries.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStockEntries.put("barcode", new TableInfo.Column("barcode", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStockEntries.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStockEntries.put("productCode", new TableInfo.Column("productCode", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStockEntries.put("isOos", new TableInfo.Column("isOos", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStockEntries.put("quantity", new TableInfo.Column("quantity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStockEntries.put("unit", new TableInfo.Column("unit", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStockEntries.put("outletName", new TableInfo.Column("outletName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStockEntries.put("outletCode", new TableInfo.Column("outletCode", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStockEntries.put("merchandiser", new TableInfo.Column("merchandiser", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStockEntries.put("salesman", new TableInfo.Column("salesman", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStockEntries.put("entryTimestamp", new TableInfo.Column("entryTimestamp", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStockEntries.put("archived", new TableInfo.Column("archived", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysStockEntries = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesStockEntries = new HashSet<TableInfo.Index>(2);
        _indicesStockEntries.add(new TableInfo.Index("index_stock_entries_outletCode", false, Arrays.asList("outletCode"), Arrays.asList("ASC")));
        _indicesStockEntries.add(new TableInfo.Index("index_stock_entries_archived", false, Arrays.asList("archived"), Arrays.asList("ASC")));
        final TableInfo _infoStockEntries = new TableInfo("stock_entries", _columnsStockEntries, _foreignKeysStockEntries, _indicesStockEntries);
        final TableInfo _existingStockEntries = TableInfo.read(connection, "stock_entries");
        if (!_infoStockEntries.equals(_existingStockEntries)) {
          return new RoomOpenDelegate.ValidationResult(false, "stock_entries(com.armada.expiryapp.data.db.entity.StockEntry).\n"
                  + " Expected:\n" + _infoStockEntries + "\n"
                  + " Found:\n" + _existingStockEntries);
        }
        final Map<String, TableInfo.Column> _columnsOutletItemLinks = new HashMap<String, TableInfo.Column>(5);
        _columnsOutletItemLinks.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOutletItemLinks.put("outletCode", new TableInfo.Column("outletCode", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOutletItemLinks.put("barcode", new TableInfo.Column("barcode", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOutletItemLinks.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOutletItemLinks.put("productCode", new TableInfo.Column("productCode", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysOutletItemLinks = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesOutletItemLinks = new HashSet<TableInfo.Index>(2);
        _indicesOutletItemLinks.add(new TableInfo.Index("index_outlet_item_links_outletCode", false, Arrays.asList("outletCode"), Arrays.asList("ASC")));
        _indicesOutletItemLinks.add(new TableInfo.Index("index_outlet_item_links_barcode", false, Arrays.asList("barcode"), Arrays.asList("ASC")));
        final TableInfo _infoOutletItemLinks = new TableInfo("outlet_item_links", _columnsOutletItemLinks, _foreignKeysOutletItemLinks, _indicesOutletItemLinks);
        final TableInfo _existingOutletItemLinks = TableInfo.read(connection, "outlet_item_links");
        if (!_infoOutletItemLinks.equals(_existingOutletItemLinks)) {
          return new RoomOpenDelegate.ValidationResult(false, "outlet_item_links(com.armada.expiryapp.data.db.entity.OutletItemLink).\n"
                  + " Expected:\n" + _infoOutletItemLinks + "\n"
                  + " Found:\n" + _existingOutletItemLinks);
        }
        final Map<String, TableInfo.Column> _columnsDeviceLock = new HashMap<String, TableInfo.Column>(3);
        _columnsDeviceLock.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceLock.put("merchandiserName", new TableInfo.Column("merchandiserName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceLock.put("lockedAt", new TableInfo.Column("lockedAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysDeviceLock = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesDeviceLock = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDeviceLock = new TableInfo("device_lock", _columnsDeviceLock, _foreignKeysDeviceLock, _indicesDeviceLock);
        final TableInfo _existingDeviceLock = TableInfo.read(connection, "device_lock");
        if (!_infoDeviceLock.equals(_existingDeviceLock)) {
          return new RoomOpenDelegate.ValidationResult(false, "device_lock(com.armada.expiryapp.data.db.entity.DeviceLock).\n"
                  + " Expected:\n" + _infoDeviceLock + "\n"
                  + " Found:\n" + _existingDeviceLock);
        }
        final Map<String, TableInfo.Column> _columnsTeamLinks = new HashMap<String, TableInfo.Column>(5);
        _columnsTeamLinks.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamLinks.put("merchandiserName", new TableInfo.Column("merchandiserName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamLinks.put("salesmanName", new TableInfo.Column("salesmanName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamLinks.put("outletCode", new TableInfo.Column("outletCode", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamLinks.put("outletName", new TableInfo.Column("outletName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysTeamLinks = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesTeamLinks = new HashSet<TableInfo.Index>(1);
        _indicesTeamLinks.add(new TableInfo.Index("index_team_links_outletCode", false, Arrays.asList("outletCode"), Arrays.asList("ASC")));
        final TableInfo _infoTeamLinks = new TableInfo("team_links", _columnsTeamLinks, _foreignKeysTeamLinks, _indicesTeamLinks);
        final TableInfo _existingTeamLinks = TableInfo.read(connection, "team_links");
        if (!_infoTeamLinks.equals(_existingTeamLinks)) {
          return new RoomOpenDelegate.ValidationResult(false, "team_links(com.armada.expiryapp.data.db.entity.TeamLink).\n"
                  + " Expected:\n" + _infoTeamLinks + "\n"
                  + " Found:\n" + _existingTeamLinks);
        }
        return new RoomOpenDelegate.ValidationResult(true, null);
      }
    };
    return _openDelegate;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final Map<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final Map<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "items", "outlets", "expiry_entries", "csv_metadata", "stock_entries", "outlet_item_links", "device_lock", "team_links");
  }

  @Override
  public void clearAllTables() {
    super.performClear(false, "items", "outlets", "expiry_entries", "csv_metadata", "stock_entries", "outlet_item_links", "device_lock", "team_links");
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final Map<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(ItemDao.class, ItemDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(OutletDao.class, OutletDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ExpiryEntryDao.class, ExpiryEntryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CsvMetadataDao.class, CsvMetadataDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(StockEntryDao.class, StockEntryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(OutletItemLinkDao.class, OutletItemLinkDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DeviceLockDao.class, DeviceLockDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TeamLinkDao.class, TeamLinkDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final Set<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public ItemDao itemDao() {
    if (_itemDao != null) {
      return _itemDao;
    } else {
      synchronized(this) {
        if(_itemDao == null) {
          _itemDao = new ItemDao_Impl(this);
        }
        return _itemDao;
      }
    }
  }

  @Override
  public OutletDao outletDao() {
    if (_outletDao != null) {
      return _outletDao;
    } else {
      synchronized(this) {
        if(_outletDao == null) {
          _outletDao = new OutletDao_Impl(this);
        }
        return _outletDao;
      }
    }
  }

  @Override
  public ExpiryEntryDao expiryEntryDao() {
    if (_expiryEntryDao != null) {
      return _expiryEntryDao;
    } else {
      synchronized(this) {
        if(_expiryEntryDao == null) {
          _expiryEntryDao = new ExpiryEntryDao_Impl(this);
        }
        return _expiryEntryDao;
      }
    }
  }

  @Override
  public CsvMetadataDao csvMetadataDao() {
    if (_csvMetadataDao != null) {
      return _csvMetadataDao;
    } else {
      synchronized(this) {
        if(_csvMetadataDao == null) {
          _csvMetadataDao = new CsvMetadataDao_Impl(this);
        }
        return _csvMetadataDao;
      }
    }
  }

  @Override
  public StockEntryDao stockEntryDao() {
    if (_stockEntryDao != null) {
      return _stockEntryDao;
    } else {
      synchronized(this) {
        if(_stockEntryDao == null) {
          _stockEntryDao = new StockEntryDao_Impl(this);
        }
        return _stockEntryDao;
      }
    }
  }

  @Override
  public OutletItemLinkDao outletItemLinkDao() {
    if (_outletItemLinkDao != null) {
      return _outletItemLinkDao;
    } else {
      synchronized(this) {
        if(_outletItemLinkDao == null) {
          _outletItemLinkDao = new OutletItemLinkDao_Impl(this);
        }
        return _outletItemLinkDao;
      }
    }
  }

  @Override
  public DeviceLockDao deviceLockDao() {
    if (_deviceLockDao != null) {
      return _deviceLockDao;
    } else {
      synchronized(this) {
        if(_deviceLockDao == null) {
          _deviceLockDao = new DeviceLockDao_Impl(this);
        }
        return _deviceLockDao;
      }
    }
  }

  @Override
  public TeamLinkDao teamLinkDao() {
    if (_teamLinkDao != null) {
      return _teamLinkDao;
    } else {
      synchronized(this) {
        if(_teamLinkDao == null) {
          _teamLinkDao = new TeamLinkDao_Impl(this);
        }
        return _teamLinkDao;
      }
    }
  }
}
