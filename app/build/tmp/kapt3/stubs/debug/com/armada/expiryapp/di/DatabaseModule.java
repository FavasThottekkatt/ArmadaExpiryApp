package com.armada.expiryapp.di;

import android.content.Context;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.armada.expiryapp.data.db.AppDatabase;
import com.armada.expiryapp.data.db.dao.CsvMetadataDao;
import com.armada.expiryapp.data.db.dao.DeviceLockDao;
import com.armada.expiryapp.data.db.dao.ExpiryEntryDao;
import com.armada.expiryapp.data.db.dao.ItemDao;
import com.armada.expiryapp.data.db.dao.OutletDao;
import com.armada.expiryapp.data.db.dao.OutletItemLinkDao;
import com.armada.expiryapp.data.db.dao.StockEntryDao;
import com.armada.expiryapp.data.db.dao.TeamLinkDao;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;

@dagger.Module()
@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\u0004\u001a\u00020\u00052\b\b\u0001\u0010\u0006\u001a\u00020\u0007H\u0007J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0005H\u0007J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\n\u001a\u00020\u0005H\u0007J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\n\u001a\u00020\u0005H\u0007J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\n\u001a\u00020\u0005H\u0007J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\n\u001a\u00020\u0005H\u0007J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\n\u001a\u00020\u0005H\u0007J\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\n\u001a\u00020\u0005H\u0007J\u0010\u0010\u0017\u001a\u00020\u00182\u0006\u0010\n\u001a\u00020\u0005H\u0007\u00a8\u0006\u0019"}, d2 = {"Lcom/armada/expiryapp/di/DatabaseModule;", "", "<init>", "()V", "provideDatabase", "Lcom/armada/expiryapp/data/db/AppDatabase;", "context", "Landroid/content/Context;", "provideItemDao", "Lcom/armada/expiryapp/data/db/dao/ItemDao;", "db", "provideOutletDao", "Lcom/armada/expiryapp/data/db/dao/OutletDao;", "provideExpiryEntryDao", "Lcom/armada/expiryapp/data/db/dao/ExpiryEntryDao;", "provideCsvMetadataDao", "Lcom/armada/expiryapp/data/db/dao/CsvMetadataDao;", "provideStockEntryDao", "Lcom/armada/expiryapp/data/db/dao/StockEntryDao;", "provideOutletItemLinkDao", "Lcom/armada/expiryapp/data/db/dao/OutletItemLinkDao;", "provideDeviceLockDao", "Lcom/armada/expiryapp/data/db/dao/DeviceLockDao;", "provideTeamLinkDao", "Lcom/armada/expiryapp/data/db/dao/TeamLinkDao;", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class DatabaseModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.armada.expiryapp.di.DatabaseModule INSTANCE = null;
    
    private DatabaseModule() {
        super();
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.armada.expiryapp.data.db.AppDatabase provideDatabase(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.armada.expiryapp.data.db.dao.ItemDao provideItemDao(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.AppDatabase db) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.armada.expiryapp.data.db.dao.OutletDao provideOutletDao(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.AppDatabase db) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.armada.expiryapp.data.db.dao.ExpiryEntryDao provideExpiryEntryDao(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.AppDatabase db) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.armada.expiryapp.data.db.dao.CsvMetadataDao provideCsvMetadataDao(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.AppDatabase db) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.armada.expiryapp.data.db.dao.StockEntryDao provideStockEntryDao(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.AppDatabase db) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.armada.expiryapp.data.db.dao.OutletItemLinkDao provideOutletItemLinkDao(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.AppDatabase db) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.armada.expiryapp.data.db.dao.DeviceLockDao provideDeviceLockDao(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.AppDatabase db) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.armada.expiryapp.data.db.dao.TeamLinkDao provideTeamLinkDao(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.AppDatabase db) {
        return null;
    }
}