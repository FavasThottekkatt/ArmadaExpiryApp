package com.armada.expiryapp.data.db.dao;

import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import com.armada.expiryapp.data.db.entity.Item;

@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0007\bg\u0018\u00002\u00020\u0001J\u001c\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0007J\u000e\u0010\b\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\tJ\u001c\u0010\n\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0097@\u00a2\u0006\u0002\u0010\u0007J\u0018\u0010\u000b\u001a\u0004\u0018\u00010\u00062\u0006\u0010\f\u001a\u00020\rH\u00a7@\u00a2\u0006\u0002\u0010\u000eJ\u0018\u0010\u000f\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0010\u001a\u00020\rH\u00a7@\u00a2\u0006\u0002\u0010\u000eJ\u0014\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u00060\u0012H\'J\u001c\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u0015\u001a\u00020\rH\'J\u001c\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u0006\u0010\u0015\u001a\u00020\rH\u00a7@\u00a2\u0006\u0002\u0010\u000eJ\u0014\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00a7@\u00a2\u0006\u0002\u0010\tJ\u001c\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u0006\u0010\u0015\u001a\u00020\rH\u00a7@\u00a2\u0006\u0002\u0010\u000eJ\u000e\u0010\u0019\u001a\u00020\u0013H\u00a7@\u00a2\u0006\u0002\u0010\t\u00a8\u0006\u001a"}, d2 = {"Lcom/armada/expiryapp/data/db/dao/ItemDao;", "", "insertAll", "", "items", "", "Lcom/armada/expiryapp/data/db/entity/Item;", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteAll", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "replaceAll", "findByBarcode", "barcode", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "findByProductCode", "code", "getAllPaged", "Landroidx/paging/PagingSource;", "", "searchByDescriptionOrCode", "query", "searchForDropdown", "getAll", "searchAll", "getCount", "app_debug"})
@androidx.room.Dao()
public abstract interface ItemDao {
    
    @androidx.room.Insert(onConflict = 5)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertAll(@org.jetbrains.annotations.NotNull()
    java.util.List<com.armada.expiryapp.data.db.entity.Item> items, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM items")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Transaction()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object replaceAll(@org.jetbrains.annotations.NotNull()
    java.util.List<com.armada.expiryapp.data.db.entity.Item> items, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM items WHERE barcode = :barcode LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object findByBarcode(@org.jetbrains.annotations.NotNull()
    java.lang.String barcode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.armada.expiryapp.data.db.entity.Item> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM items WHERE productCode = :code LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object findByProductCode(@org.jetbrains.annotations.NotNull()
    java.lang.String code, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.armada.expiryapp.data.db.entity.Item> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM items ORDER BY description ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract androidx.paging.PagingSource<java.lang.Integer, com.armada.expiryapp.data.db.entity.Item> getAllPaged();
    
    @androidx.room.Query(value = "\n        SELECT * FROM items\n        WHERE description LIKE \'%\' || :query || \'%\'\n           OR productCode  LIKE \'%\' || :query || \'%\'\n        ORDER BY description ASC\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract androidx.paging.PagingSource<java.lang.Integer, com.armada.expiryapp.data.db.entity.Item> searchByDescriptionOrCode(@org.jetbrains.annotations.NotNull()
    java.lang.String query);
    
    @androidx.room.Query(value = "\n        SELECT * FROM items\n        WHERE description LIKE \'%\' || :query || \'%\'\n           OR productCode  LIKE \'%\' || :query || \'%\'\n        ORDER BY description ASC\n        LIMIT 50\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object searchForDropdown(@org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.armada.expiryapp.data.db.entity.Item>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM items ORDER BY description ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.armada.expiryapp.data.db.entity.Item>> $completion);
    
    @androidx.room.Query(value = "\n        SELECT * FROM items\n        WHERE description LIKE \'%\' || :query || \'%\'\n           OR productCode  LIKE \'%\' || :query || \'%\'\n        ORDER BY description ASC\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object searchAll(@org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.armada.expiryapp.data.db.entity.Item>> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM items")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @kotlin.Metadata(mv = {2, 1, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
        
        @androidx.room.Transaction()
        @org.jetbrains.annotations.Nullable()
        public static java.lang.Object replaceAll(@org.jetbrains.annotations.NotNull()
        com.armada.expiryapp.data.db.dao.ItemDao $this, @org.jetbrains.annotations.NotNull()
        java.util.List<com.armada.expiryapp.data.db.entity.Item> items, @org.jetbrains.annotations.NotNull()
        kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
            return null;
        }
    }
}