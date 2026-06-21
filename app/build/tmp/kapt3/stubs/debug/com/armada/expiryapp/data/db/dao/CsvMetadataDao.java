package com.armada.expiryapp.data.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.armada.expiryapp.data.db.entity.CsvMetadata;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0018\u0010\u0007\u001a\u0004\u0018\u00010\u00052\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00050\fH\u00a7@\u00a2\u0006\u0002\u0010\rJ\u0014\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\f0\u000fH\'\u00a8\u0006\u0010"}, d2 = {"Lcom/armada/expiryapp/data/db/dao/CsvMetadataDao;", "", "upsert", "", "metadata", "Lcom/armada/expiryapp/data/db/entity/CsvMetadata;", "(Lcom/armada/expiryapp/data/db/entity/CsvMetadata;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getByType", "fileType", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAll", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllFlow", "Lkotlinx/coroutines/flow/Flow;", "app_debug"})
@androidx.room.Dao()
public abstract interface CsvMetadataDao {
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object upsert(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.entity.CsvMetadata metadata, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM csv_metadata WHERE fileType = :fileType LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getByType(@org.jetbrains.annotations.NotNull()
    java.lang.String fileType, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.armada.expiryapp.data.db.entity.CsvMetadata> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM csv_metadata")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.armada.expiryapp.data.db.entity.CsvMetadata>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM csv_metadata")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.armada.expiryapp.data.db.entity.CsvMetadata>> getAllFlow();
}