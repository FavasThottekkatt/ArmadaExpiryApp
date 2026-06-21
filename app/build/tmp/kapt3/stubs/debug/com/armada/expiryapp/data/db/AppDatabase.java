package com.armada.expiryapp.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.armada.expiryapp.data.db.dao.CsvMetadataDao;
import com.armada.expiryapp.data.db.dao.ExpiryEntryDao;
import com.armada.expiryapp.data.db.dao.ItemDao;
import com.armada.expiryapp.data.db.dao.OutletDao;
import com.armada.expiryapp.data.db.dao.OutletItemLinkDao;
import com.armada.expiryapp.data.db.dao.StockEntryDao;
import com.armada.expiryapp.data.db.entity.CsvMetadata;
import com.armada.expiryapp.data.db.entity.ExpiryEntry;
import com.armada.expiryapp.data.db.entity.Item;
import com.armada.expiryapp.data.db.entity.Outlet;
import com.armada.expiryapp.data.db.entity.OutletItemLink;
import com.armada.expiryapp.data.db.entity.StockEntry;

@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H&J\b\u0010\u0006\u001a\u00020\u0007H&J\b\u0010\b\u001a\u00020\tH&J\b\u0010\n\u001a\u00020\u000bH&J\b\u0010\f\u001a\u00020\rH&J\b\u0010\u000e\u001a\u00020\u000fH&\u00a8\u0006\u0010"}, d2 = {"Lcom/armada/expiryapp/data/db/AppDatabase;", "Landroidx/room/RoomDatabase;", "<init>", "()V", "itemDao", "Lcom/armada/expiryapp/data/db/dao/ItemDao;", "outletDao", "Lcom/armada/expiryapp/data/db/dao/OutletDao;", "expiryEntryDao", "Lcom/armada/expiryapp/data/db/dao/ExpiryEntryDao;", "csvMetadataDao", "Lcom/armada/expiryapp/data/db/dao/CsvMetadataDao;", "stockEntryDao", "Lcom/armada/expiryapp/data/db/dao/StockEntryDao;", "outletItemLinkDao", "Lcom/armada/expiryapp/data/db/dao/OutletItemLinkDao;", "app_debug"})
@androidx.room.Database(entities = {com.armada.expiryapp.data.db.entity.Item.class, com.armada.expiryapp.data.db.entity.Outlet.class, com.armada.expiryapp.data.db.entity.ExpiryEntry.class, com.armada.expiryapp.data.db.entity.CsvMetadata.class, com.armada.expiryapp.data.db.entity.StockEntry.class, com.armada.expiryapp.data.db.entity.OutletItemLink.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends androidx.room.RoomDatabase {
    
    public AppDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.armada.expiryapp.data.db.dao.ItemDao itemDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.armada.expiryapp.data.db.dao.OutletDao outletDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.armada.expiryapp.data.db.dao.ExpiryEntryDao expiryEntryDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.armada.expiryapp.data.db.dao.CsvMetadataDao csvMetadataDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.armada.expiryapp.data.db.dao.StockEntryDao stockEntryDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.armada.expiryapp.data.db.dao.OutletItemLinkDao outletItemLinkDao();
}