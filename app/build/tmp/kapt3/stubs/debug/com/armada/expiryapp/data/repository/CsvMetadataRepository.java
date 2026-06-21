package com.armada.expiryapp.data.repository;

import com.armada.expiryapp.data.db.dao.CsvMetadataDao;
import com.armada.expiryapp.data.db.entity.CsvMetadata;
import kotlinx.coroutines.flow.Flow;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 \u00142\u00020\u0001:\u0001\u0014B\u0011\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u0016\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0086@\u00a2\u0006\u0002\u0010\nJ\u0018\u0010\u000b\u001a\u0004\u0018\u00010\t2\u0006\u0010\f\u001a\u00020\rH\u0086@\u00a2\u0006\u0002\u0010\u000eJ\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\t0\u0010H\u0086@\u00a2\u0006\u0002\u0010\u0011J\u0012\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u00100\u0013R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/armada/expiryapp/data/repository/CsvMetadataRepository;", "", "dao", "Lcom/armada/expiryapp/data/db/dao/CsvMetadataDao;", "<init>", "(Lcom/armada/expiryapp/data/db/dao/CsvMetadataDao;)V", "upsert", "", "metadata", "Lcom/armada/expiryapp/data/db/entity/CsvMetadata;", "(Lcom/armada/expiryapp/data/db/entity/CsvMetadata;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getByType", "fileType", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAll", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllFlow", "Lkotlinx/coroutines/flow/Flow;", "Companion", "app_debug"})
public final class CsvMetadataRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.db.dao.CsvMetadataDao dao = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FILE_TYPE_ITEMS = "ITEMS";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FILE_TYPE_OUTLETS = "OUTLETS";
    @org.jetbrains.annotations.NotNull()
    public static final com.armada.expiryapp.data.repository.CsvMetadataRepository.Companion Companion = null;
    
    @javax.inject.Inject()
    public CsvMetadataRepository(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.dao.CsvMetadataDao dao) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object upsert(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.entity.CsvMetadata metadata, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getByType(@org.jetbrains.annotations.NotNull()
    java.lang.String fileType, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.armada.expiryapp.data.db.entity.CsvMetadata> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.armada.expiryapp.data.db.entity.CsvMetadata>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.armada.expiryapp.data.db.entity.CsvMetadata>> getAllFlow() {
        return null;
    }
    
    @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/armada/expiryapp/data/repository/CsvMetadataRepository$Companion;", "", "<init>", "()V", "FILE_TYPE_ITEMS", "", "FILE_TYPE_OUTLETS", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}