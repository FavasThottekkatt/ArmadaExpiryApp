package com.armada.expiryapp.data.repository;

import com.armada.expiryapp.data.db.dao.CsvMetadataDao;
import com.armada.expiryapp.data.db.dao.ItemDao;
import com.armada.expiryapp.data.db.dao.OutletDao;
import com.armada.expiryapp.data.db.entity.CsvMetadata;
import com.armada.expiryapp.util.CsvParseResult;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B!\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0004\b\b\u0010\tJ\u0016\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0086@\u00a2\u0006\u0002\u0010\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/armada/expiryapp/data/repository/CsvImportRepository;", "", "itemDao", "Lcom/armada/expiryapp/data/db/dao/ItemDao;", "outletDao", "Lcom/armada/expiryapp/data/db/dao/OutletDao;", "csvMetadataDao", "Lcom/armada/expiryapp/data/db/dao/CsvMetadataDao;", "<init>", "(Lcom/armada/expiryapp/data/db/dao/ItemDao;Lcom/armada/expiryapp/data/db/dao/OutletDao;Lcom/armada/expiryapp/data/db/dao/CsvMetadataDao;)V", "importData", "", "parseResult", "Lcom/armada/expiryapp/util/CsvParseResult;", "(Lcom/armada/expiryapp/util/CsvParseResult;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class CsvImportRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.db.dao.ItemDao itemDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.db.dao.OutletDao outletDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.db.dao.CsvMetadataDao csvMetadataDao = null;
    
    @javax.inject.Inject()
    public CsvImportRepository(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.dao.ItemDao itemDao, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.dao.OutletDao outletDao, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.dao.CsvMetadataDao csvMetadataDao) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object importData(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.util.CsvParseResult parseResult, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}