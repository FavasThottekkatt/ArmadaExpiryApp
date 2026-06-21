package com.armada.expiryapp.data.repository;

import androidx.paging.PagingSource;
import com.armada.expiryapp.data.db.dao.ItemDao;
import com.armada.expiryapp.data.db.entity.ExpiryEntry;
import com.armada.expiryapp.data.db.entity.Item;
import java.util.Collections;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u0006\u0010\n\u001a\u00020\u000bJ\u0018\u0010\f\u001a\u0004\u0018\u00010\t2\u0006\u0010\r\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u000eJ\u0018\u0010\u000f\u001a\u0004\u0018\u00010\t2\u0006\u0010\u0010\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u000eJ\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\t0\u0012J\u001a\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u0015\u001a\u00020\bJ\u001c\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\t0\u00172\u0006\u0010\u0015\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u000eJ\u0014\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\t0\u0017H\u0086@\u00a2\u0006\u0002\u0010\u0019J\u001c\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\t0\u00172\u0006\u0010\u0015\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u000eJ\u000e\u0010\u001b\u001a\u00020\u0013H\u0086@\u00a2\u0006\u0002\u0010\u0019J\u001c\u0010\u001c\u001a\u00020\u000b2\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u001e0\u0017H\u0086@\u00a2\u0006\u0002\u0010\u001fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0006\u001a\u0010\u0012\u0004\u0012\u00020\b\u0012\u0006\u0012\u0004\u0018\u00010\t0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006 "}, d2 = {"Lcom/armada/expiryapp/data/repository/ItemRepository;", "", "itemDao", "Lcom/armada/expiryapp/data/db/dao/ItemDao;", "<init>", "(Lcom/armada/expiryapp/data/db/dao/ItemDao;)V", "barcodeCache", "", "", "Lcom/armada/expiryapp/data/db/entity/Item;", "clearCache", "", "findByBarcode", "barcode", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "findByProductCode", "code", "getAllPaged", "Landroidx/paging/PagingSource;", "", "searchByDescriptionOrCode", "query", "searchForDropdown", "", "getAll", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "searchAll", "getCount", "preloadRecentBarcodes", "recentEntries", "Lcom/armada/expiryapp/data/db/entity/ExpiryEntry;", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class ItemRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.db.dao.ItemDao itemDao = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.String, com.armada.expiryapp.data.db.entity.Item> barcodeCache = null;
    
    @javax.inject.Inject()
    public ItemRepository(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.dao.ItemDao itemDao) {
        super();
    }
    
    public final void clearCache() {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object findByBarcode(@org.jetbrains.annotations.NotNull()
    java.lang.String barcode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.armada.expiryapp.data.db.entity.Item> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object findByProductCode(@org.jetbrains.annotations.NotNull()
    java.lang.String code, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.armada.expiryapp.data.db.entity.Item> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.paging.PagingSource<java.lang.Integer, com.armada.expiryapp.data.db.entity.Item> getAllPaged() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.paging.PagingSource<java.lang.Integer, com.armada.expiryapp.data.db.entity.Item> searchByDescriptionOrCode(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object searchForDropdown(@org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.armada.expiryapp.data.db.entity.Item>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.armada.expiryapp.data.db.entity.Item>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object searchAll(@org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.armada.expiryapp.data.db.entity.Item>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object preloadRecentBarcodes(@org.jetbrains.annotations.NotNull()
    java.util.List<com.armada.expiryapp.data.db.entity.ExpiryEntry> recentEntries, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}