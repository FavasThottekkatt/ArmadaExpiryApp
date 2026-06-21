package com.armada.expiryapp.data.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.armada.expiryapp.data.db.entity.StockEntry;

@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\nJ,\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00050\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u000eH\u00a7@\u00a2\u0006\u0002\u0010\u0011J&\u0010\u0012\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u000eH\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u0016\u0010\u0013\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000eH\u00a7@\u00a2\u0006\u0002\u0010\u0014\u00a8\u0006\u0015"}, d2 = {"Lcom/armada/expiryapp/data/db/dao/StockEntryDao;", "", "upsert", "", "entry", "Lcom/armada/expiryapp/data/db/entity/StockEntry;", "(Lcom/armada/expiryapp/data/db/entity/StockEntry;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteById", "", "id", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getActiveEntries", "", "outletCode", "", "merchandiser", "salesman", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteAllForSession", "archiveForOutlet", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface StockEntryDao {
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object upsert(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.entity.StockEntry entry, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Query(value = "DELETE FROM stock_entries WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "\n        SELECT * FROM stock_entries\n        WHERE outletCode   = :outletCode\n          AND merchandiser = :merchandiser\n          AND salesman     = :salesman\n          AND archived = 0\n        ORDER BY description ASC\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getActiveEntries(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    java.lang.String merchandiser, @org.jetbrains.annotations.NotNull()
    java.lang.String salesman, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.armada.expiryapp.data.db.entity.StockEntry>> $completion);
    
    @androidx.room.Query(value = "\n        DELETE FROM stock_entries\n        WHERE outletCode   = :outletCode\n          AND merchandiser = :merchandiser\n          AND salesman     = :salesman\n          AND archived = 0\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAllForSession(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    java.lang.String merchandiser, @org.jetbrains.annotations.NotNull()
    java.lang.String salesman, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "\n        UPDATE stock_entries SET archived = 1\n        WHERE outletCode = :outletCode AND archived = 0\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object archiveForOutlet(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}