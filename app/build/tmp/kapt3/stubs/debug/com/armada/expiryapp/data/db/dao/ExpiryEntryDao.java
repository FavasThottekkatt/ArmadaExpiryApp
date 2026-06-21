package com.armada.expiryapp.data.db.dao;

import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.armada.expiryapp.data.db.entity.ExpiryEntry;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u001a\n\u0002\u0010 \n\u0002\b\f\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\t\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\n\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\fJ\u001c\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00050\u000e2\u0006\u0010\u0010\u001a\u00020\u0011H\'J$\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00050\u000e2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u0011H\'J\u001e\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u000f0\u00152\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u0011H\'J&\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u000f0\u00152\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0018\u001a\u00020\u0011H\'J&\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u000f0\u00152\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0018\u001a\u00020\u00112\u0006\u0010\u001a\u001a\u00020\u0011H\'J&\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u000f0\u00152\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u001a\u001a\u00020\u00112\u0006\u0010\u001c\u001a\u00020\u0011H\'J\u001e\u0010\u001d\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u0011H\u00a7@\u00a2\u0006\u0002\u0010\u001eJ&\u0010\u001f\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0018\u001a\u00020\u0011H\u00a7@\u00a2\u0006\u0002\u0010 J\u0018\u0010!\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\fJ0\u0010\"\u001a\u0004\u0018\u00010\u00052\u0006\u0010#\u001a\u00020\u00112\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010$\u001a\u00020\u00112\u0006\u0010%\u001a\u00020\u0011H\u00a7@\u00a2\u0006\u0002\u0010&J\u001e\u0010\'\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\u00032\u0006\u0010(\u001a\u00020\u000fH\u00a7@\u00a2\u0006\u0002\u0010)J\u001e\u0010*\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010+\u001a\u00020\u0011H\u00a7@\u00a2\u0006\u0002\u0010\u001eJ\u000e\u0010,\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010-J\u001c\u0010.\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00050\u000e2\u0006\u0010\u0010\u001a\u00020\u0011H\'J\u001c\u0010/\u001a\b\u0012\u0004\u0012\u00020\u0005002\u0006\u0010\u0010\u001a\u00020\u0011H\u00a7@\u00a2\u0006\u0002\u00101J\u0016\u00102\u001a\b\u0012\u0004\u0012\u00020\u000f0\u00152\u0006\u0010\u0010\u001a\u00020\u0011H\'J4\u00103\u001a\b\u0012\u0004\u0012\u00020\u0005002\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010%\u001a\u00020\u00112\u0006\u00104\u001a\u00020\u00112\u0006\u0010+\u001a\u00020\u0011H\u00a7@\u00a2\u0006\u0002\u0010&J,\u00105\u001a\b\u0012\u0004\u0012\u00020\u0005002\u0006\u0010%\u001a\u00020\u00112\u0006\u00104\u001a\u00020\u00112\u0006\u0010+\u001a\u00020\u0011H\u00a7@\u00a2\u0006\u0002\u0010 J\u0010\u00106\u001a\u0004\u0018\u00010\u0005H\u00a7@\u00a2\u0006\u0002\u0010-J\u0016\u00107\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u00a7@\u00a2\u0006\u0002\u00101J\u000e\u00108\u001a\u00020\u000fH\u00a7@\u00a2\u0006\u0002\u0010-J\u001c\u00109\u001a\b\u0012\u0004\u0012\u00020\u0005002\u0006\u0010:\u001a\u00020\u000fH\u00a7@\u00a2\u0006\u0002\u0010;\u00a8\u0006<"}, d2 = {"Lcom/armada/expiryapp/data/db/dao/ExpiryEntryDao;", "", "insert", "", "entry", "Lcom/armada/expiryapp/data/db/entity/ExpiryEntry;", "(Lcom/armada/expiryapp/data/db/entity/ExpiryEntry;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "update", "", "delete", "deleteById", "id", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getActiveEntriesPaged", "Landroidx/paging/PagingSource;", "", "outletCode", "", "searchActiveEntriesPaged", "query", "getExpiredCountFlow", "Lkotlinx/coroutines/flow/Flow;", "today", "getWithin30CountFlow", "d30", "getWithin60CountFlow", "d60", "getWithin90CountFlow", "d90", "getExpiredCount", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getWithin30Count", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "findById", "findDuplicate", "barcode", "expiryDate", "merchandiser", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "incrementQuantity", "additionalQty", "(JILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "archiveForOutletMonth", "monthPrefix", "archiveAllActive", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getArchivedEntriesPaged", "getArchivedEntries", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getArchivedCountFlow", "getEntriesForExport", "salesman", "getAllEntriesForMerchandiserMonth", "getLastActiveEntry", "getActiveCountForOutlet", "getTotalActiveCount", "getRecentEntries", "limit", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface ExpiryEntryDao {
    
    @androidx.room.Insert()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.entity.ExpiryEntry entry, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object update(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.entity.ExpiryEntry entry, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object delete(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.entity.ExpiryEntry entry, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM expiry_entries WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "\n        SELECT * FROM expiry_entries\n        WHERE outletCode = :outletCode AND archived = 0\n        ORDER BY expiryDate ASC\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract androidx.paging.PagingSource<java.lang.Integer, com.armada.expiryapp.data.db.entity.ExpiryEntry> getActiveEntriesPaged(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode);
    
    @androidx.room.Query(value = "\n        SELECT * FROM expiry_entries\n        WHERE outletCode = :outletCode AND archived = 0\n          AND (description LIKE \'%\' || :query || \'%\' OR productCode LIKE \'%\' || :query || \'%\')\n        ORDER BY expiryDate ASC\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract androidx.paging.PagingSource<java.lang.Integer, com.armada.expiryapp.data.db.entity.ExpiryEntry> searchActiveEntriesPaged(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    java.lang.String query);
    
    @androidx.room.Query(value = "\n        SELECT COUNT(*) FROM expiry_entries\n        WHERE outletCode = :outletCode AND archived = 0 AND expiryDate < :today\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Integer> getExpiredCountFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    java.lang.String today);
    
    @androidx.room.Query(value = "\n        SELECT COUNT(*) FROM expiry_entries\n        WHERE outletCode = :outletCode AND archived = 0\n          AND expiryDate >= :today AND expiryDate <= :d30\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Integer> getWithin30CountFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    java.lang.String today, @org.jetbrains.annotations.NotNull()
    java.lang.String d30);
    
    @androidx.room.Query(value = "\n        SELECT COUNT(*) FROM expiry_entries\n        WHERE outletCode = :outletCode AND archived = 0\n          AND expiryDate > :d30 AND expiryDate <= :d60\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Integer> getWithin60CountFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    java.lang.String d30, @org.jetbrains.annotations.NotNull()
    java.lang.String d60);
    
    @androidx.room.Query(value = "\n        SELECT COUNT(*) FROM expiry_entries\n        WHERE outletCode = :outletCode AND archived = 0\n          AND expiryDate > :d60 AND expiryDate <= :d90\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Integer> getWithin90CountFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    java.lang.String d60, @org.jetbrains.annotations.NotNull()
    java.lang.String d90);
    
    @androidx.room.Query(value = "\n        SELECT COUNT(*) FROM expiry_entries\n        WHERE outletCode = :outletCode AND archived = 0 AND expiryDate < :today\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getExpiredCount(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    java.lang.String today, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "\n        SELECT COUNT(*) FROM expiry_entries\n        WHERE outletCode = :outletCode AND archived = 0\n          AND expiryDate >= :today AND expiryDate <= :d30\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getWithin30Count(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    java.lang.String today, @org.jetbrains.annotations.NotNull()
    java.lang.String d30, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM expiry_entries WHERE id = :id LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object findById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.armada.expiryapp.data.db.entity.ExpiryEntry> $completion);
    
    @androidx.room.Query(value = "\n        SELECT * FROM expiry_entries\n        WHERE barcode      = :barcode\n          AND outletCode   = :outletCode\n          AND expiryDate   = :expiryDate\n          AND merchandiser = :merchandiser\n          AND archived = 0\n        LIMIT 1\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object findDuplicate(@org.jetbrains.annotations.NotNull()
    java.lang.String barcode, @org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    java.lang.String expiryDate, @org.jetbrains.annotations.NotNull()
    java.lang.String merchandiser, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.armada.expiryapp.data.db.entity.ExpiryEntry> $completion);
    
    @androidx.room.Query(value = "UPDATE expiry_entries SET quantity = quantity + :additionalQty WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object incrementQuantity(long id, int additionalQty, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "\n        UPDATE expiry_entries SET archived = 1\n        WHERE outletCode = :outletCode AND archived = 0\n          AND entryTimestamp LIKE :monthPrefix || \'%\'\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object archiveForOutletMonth(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    java.lang.String monthPrefix, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE expiry_entries SET archived = 1 WHERE archived = 0")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object archiveAllActive(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "\n        SELECT * FROM expiry_entries\n        WHERE outletCode = :outletCode AND archived = 1\n        ORDER BY expiryDate ASC\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract androidx.paging.PagingSource<java.lang.Integer, com.armada.expiryapp.data.db.entity.ExpiryEntry> getArchivedEntriesPaged(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode);
    
    @androidx.room.Query(value = "\n        SELECT * FROM expiry_entries\n        WHERE outletCode = :outletCode AND archived = 1\n        ORDER BY expiryDate ASC\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getArchivedEntries(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.armada.expiryapp.data.db.entity.ExpiryEntry>> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM expiry_entries WHERE outletCode = :outletCode AND archived = 1")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Integer> getArchivedCountFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode);
    
    @androidx.room.Query(value = "\n        SELECT * FROM expiry_entries\n        WHERE outletCode   = :outletCode\n          AND merchandiser = :merchandiser\n          AND salesman     = :salesman\n          AND archived = 0\n          AND entryTimestamp LIKE :monthPrefix || \'%\'\n        ORDER BY expiryDate ASC\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEntriesForExport(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    java.lang.String merchandiser, @org.jetbrains.annotations.NotNull()
    java.lang.String salesman, @org.jetbrains.annotations.NotNull()
    java.lang.String monthPrefix, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.armada.expiryapp.data.db.entity.ExpiryEntry>> $completion);
    
    @androidx.room.Query(value = "\n        SELECT * FROM expiry_entries\n        WHERE merchandiser = :merchandiser\n          AND salesman     = :salesman\n          AND archived = 0\n          AND entryTimestamp LIKE :monthPrefix || \'%\'\n        ORDER BY outletName ASC, expiryDate ASC\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllEntriesForMerchandiserMonth(@org.jetbrains.annotations.NotNull()
    java.lang.String merchandiser, @org.jetbrains.annotations.NotNull()
    java.lang.String salesman, @org.jetbrains.annotations.NotNull()
    java.lang.String monthPrefix, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.armada.expiryapp.data.db.entity.ExpiryEntry>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM expiry_entries WHERE archived = 0 ORDER BY entryTimestamp DESC LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getLastActiveEntry(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.armada.expiryapp.data.db.entity.ExpiryEntry> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM expiry_entries WHERE archived = 0 AND outletCode = :outletCode")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getActiveCountForOutlet(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM expiry_entries WHERE archived = 0")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTotalActiveCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM expiry_entries ORDER BY entryTimestamp DESC LIMIT :limit")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getRecentEntries(int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.armada.expiryapp.data.db.entity.ExpiryEntry>> $completion);
}