package com.armada.expiryapp.data.repository;

import androidx.paging.PagingSource;
import com.armada.expiryapp.data.db.dao.ExpiryEntryDao;
import com.armada.expiryapp.data.db.entity.ExpiryEntry;
import kotlinx.coroutines.flow.Flow;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u001a\n\u0002\u0010 \n\u0002\b\f\b\u0007\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u0016\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0086@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u000b\u001a\u00020\f2\u0006\u0010\b\u001a\u00020\tH\u0086@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\r\u001a\u00020\f2\u0006\u0010\b\u001a\u00020\tH\u0086@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u0007H\u0086@\u00a2\u0006\u0002\u0010\u0010J\u001a\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u0014\u001a\u00020\u0015J\"\u0010\u0016\u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u0015J\u001c\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00130\u00192\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u001a\u001a\u00020\u0015J$\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00130\u00192\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u001a\u001a\u00020\u00152\u0006\u0010\u001c\u001a\u00020\u0015J$\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00130\u00192\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u001c\u001a\u00020\u00152\u0006\u0010\u001e\u001a\u00020\u0015J$\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00130\u00192\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u001e\u001a\u00020\u00152\u0006\u0010 \u001a\u00020\u0015J\u001e\u0010!\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u001a\u001a\u00020\u0015H\u0086@\u00a2\u0006\u0002\u0010\"J&\u0010#\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u001a\u001a\u00020\u00152\u0006\u0010\u001c\u001a\u00020\u0015H\u0086@\u00a2\u0006\u0002\u0010$J\u0018\u0010%\u001a\u0004\u0018\u00010\t2\u0006\u0010\u000f\u001a\u00020\u0007H\u0086@\u00a2\u0006\u0002\u0010\u0010J0\u0010&\u001a\u0004\u0018\u00010\t2\u0006\u0010\'\u001a\u00020\u00152\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010(\u001a\u00020\u00152\u0006\u0010)\u001a\u00020\u0015H\u0086@\u00a2\u0006\u0002\u0010*J\u001e\u0010+\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u00072\u0006\u0010,\u001a\u00020\u0013H\u0086@\u00a2\u0006\u0002\u0010-J\u001e\u0010.\u001a\u00020\f2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010/\u001a\u00020\u0015H\u0086@\u00a2\u0006\u0002\u0010\"J\u000e\u00100\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u00101J\u001a\u00102\u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u0014\u001a\u00020\u0015J\u001c\u00103\u001a\b\u0012\u0004\u0012\u00020\t042\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00a2\u0006\u0002\u00105J\u0014\u00106\u001a\b\u0012\u0004\u0012\u00020\u00130\u00192\u0006\u0010\u0014\u001a\u00020\u0015J4\u00107\u001a\b\u0012\u0004\u0012\u00020\t042\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010)\u001a\u00020\u00152\u0006\u00108\u001a\u00020\u00152\u0006\u0010/\u001a\u00020\u0015H\u0086@\u00a2\u0006\u0002\u0010*J,\u00109\u001a\b\u0012\u0004\u0012\u00020\t042\u0006\u0010)\u001a\u00020\u00152\u0006\u00108\u001a\u00020\u00152\u0006\u0010/\u001a\u00020\u0015H\u0086@\u00a2\u0006\u0002\u0010$J\u0010\u0010:\u001a\u0004\u0018\u00010\tH\u0086@\u00a2\u0006\u0002\u00101J\u0016\u0010;\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00a2\u0006\u0002\u00105J\u000e\u0010<\u001a\u00020\u0013H\u0086@\u00a2\u0006\u0002\u00101J\u001c\u0010=\u001a\b\u0012\u0004\u0012\u00020\t042\u0006\u0010>\u001a\u00020\u0013H\u0086@\u00a2\u0006\u0002\u0010?R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006@"}, d2 = {"Lcom/armada/expiryapp/data/repository/ExpiryEntryRepository;", "", "dao", "Lcom/armada/expiryapp/data/db/dao/ExpiryEntryDao;", "<init>", "(Lcom/armada/expiryapp/data/db/dao/ExpiryEntryDao;)V", "insert", "", "entry", "Lcom/armada/expiryapp/data/db/entity/ExpiryEntry;", "(Lcom/armada/expiryapp/data/db/entity/ExpiryEntry;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "update", "", "delete", "deleteById", "id", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getActiveEntriesPaged", "Landroidx/paging/PagingSource;", "", "outletCode", "", "searchActiveEntriesPaged", "query", "getExpiredCountFlow", "Lkotlinx/coroutines/flow/Flow;", "today", "getWithin30CountFlow", "d30", "getWithin60CountFlow", "d60", "getWithin90CountFlow", "d90", "getExpiredCount", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getWithin30Count", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "findById", "findDuplicate", "barcode", "expiryDate", "merchandiser", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "incrementQuantity", "additionalQty", "(JILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "archiveForOutletMonth", "monthPrefix", "archiveAllActive", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getArchivedEntriesPaged", "getArchivedEntries", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getArchivedCountFlow", "getEntriesForExport", "salesman", "getAllEntriesForMerchandiserMonth", "getLastActiveEntry", "getActiveCountForOutlet", "getTotalActiveCount", "getRecentEntries", "limit", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class ExpiryEntryRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.db.dao.ExpiryEntryDao dao = null;
    
    @javax.inject.Inject()
    public ExpiryEntryRepository(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.dao.ExpiryEntryDao dao) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.entity.ExpiryEntry entry, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object update(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.entity.ExpiryEntry entry, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object delete(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.entity.ExpiryEntry entry, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.paging.PagingSource<java.lang.Integer, com.armada.expiryapp.data.db.entity.ExpiryEntry> getActiveEntriesPaged(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.paging.PagingSource<java.lang.Integer, com.armada.expiryapp.data.db.entity.ExpiryEntry> searchActiveEntriesPaged(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    java.lang.String query) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Integer> getExpiredCountFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    java.lang.String today) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Integer> getWithin30CountFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    java.lang.String today, @org.jetbrains.annotations.NotNull()
    java.lang.String d30) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Integer> getWithin60CountFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    java.lang.String d30, @org.jetbrains.annotations.NotNull()
    java.lang.String d60) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Integer> getWithin90CountFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    java.lang.String d60, @org.jetbrains.annotations.NotNull()
    java.lang.String d90) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getExpiredCount(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    java.lang.String today, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getWithin30Count(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    java.lang.String today, @org.jetbrains.annotations.NotNull()
    java.lang.String d30, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object findById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.armada.expiryapp.data.db.entity.ExpiryEntry> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object findDuplicate(@org.jetbrains.annotations.NotNull()
    java.lang.String barcode, @org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    java.lang.String expiryDate, @org.jetbrains.annotations.NotNull()
    java.lang.String merchandiser, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.armada.expiryapp.data.db.entity.ExpiryEntry> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object incrementQuantity(long id, int additionalQty, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object archiveForOutletMonth(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    java.lang.String monthPrefix, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object archiveAllActive(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.paging.PagingSource<java.lang.Integer, com.armada.expiryapp.data.db.entity.ExpiryEntry> getArchivedEntriesPaged(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getArchivedEntries(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.armada.expiryapp.data.db.entity.ExpiryEntry>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Integer> getArchivedCountFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getEntriesForExport(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    java.lang.String merchandiser, @org.jetbrains.annotations.NotNull()
    java.lang.String salesman, @org.jetbrains.annotations.NotNull()
    java.lang.String monthPrefix, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.armada.expiryapp.data.db.entity.ExpiryEntry>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getAllEntriesForMerchandiserMonth(@org.jetbrains.annotations.NotNull()
    java.lang.String merchandiser, @org.jetbrains.annotations.NotNull()
    java.lang.String salesman, @org.jetbrains.annotations.NotNull()
    java.lang.String monthPrefix, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.armada.expiryapp.data.db.entity.ExpiryEntry>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getLastActiveEntry(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.armada.expiryapp.data.db.entity.ExpiryEntry> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getActiveCountForOutlet(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getTotalActiveCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getRecentEntries(int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.armada.expiryapp.data.db.entity.ExpiryEntry>> $completion) {
        return null;
    }
}