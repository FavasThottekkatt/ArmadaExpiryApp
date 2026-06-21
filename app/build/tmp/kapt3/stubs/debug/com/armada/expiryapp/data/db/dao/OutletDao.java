package com.armada.expiryapp.data.db.dao;

import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import com.armada.expiryapp.data.db.entity.Outlet;

@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\bg\u0018\u00002\u00020\u0001J\u001c\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0007J\u000e\u0010\b\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\tJ\u001c\u0010\n\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0097@\u00a2\u0006\u0002\u0010\u0007J\u001c\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00060\f2\u0006\u0010\u000e\u001a\u00020\u000fH\'J\u001c\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u0006\u0010\u000e\u001a\u00020\u000fH\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00a7@\u00a2\u0006\u0002\u0010\tJ\u0018\u0010\u0013\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0014\u001a\u00020\u000fH\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u000e\u0010\u0015\u001a\u00020\rH\u00a7@\u00a2\u0006\u0002\u0010\t\u00a8\u0006\u0016"}, d2 = {"Lcom/armada/expiryapp/data/db/dao/OutletDao;", "", "insertAll", "", "outlets", "", "Lcom/armada/expiryapp/data/db/entity/Outlet;", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteAll", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "replaceAll", "searchByNameOrCode", "Landroidx/paging/PagingSource;", "", "query", "", "searchForDropdown", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAll", "findByCode", "code", "getCount", "app_debug"})
@androidx.room.Dao()
public abstract interface OutletDao {
    
    @androidx.room.Insert(onConflict = 5)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertAll(@org.jetbrains.annotations.NotNull()
    java.util.List<com.armada.expiryapp.data.db.entity.Outlet> outlets, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM outlets")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Transaction()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object replaceAll(@org.jetbrains.annotations.NotNull()
    java.util.List<com.armada.expiryapp.data.db.entity.Outlet> outlets, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "\n        SELECT * FROM outlets\n        WHERE outletName LIKE \'%\' || :query || \'%\'\n           OR outletCode  LIKE \'%\' || :query || \'%\'\n        ORDER BY outletName ASC\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract androidx.paging.PagingSource<java.lang.Integer, com.armada.expiryapp.data.db.entity.Outlet> searchByNameOrCode(@org.jetbrains.annotations.NotNull()
    java.lang.String query);
    
    @androidx.room.Query(value = "\n        SELECT * FROM outlets\n        WHERE outletName LIKE \'%\' || :query || \'%\'\n           OR outletCode  LIKE \'%\' || :query || \'%\'\n        ORDER BY outletName ASC\n        LIMIT 20\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object searchForDropdown(@org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.armada.expiryapp.data.db.entity.Outlet>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM outlets ORDER BY outletName ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.armada.expiryapp.data.db.entity.Outlet>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM outlets WHERE outletCode = :code LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object findByCode(@org.jetbrains.annotations.NotNull()
    java.lang.String code, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.armada.expiryapp.data.db.entity.Outlet> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM outlets")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @kotlin.Metadata(mv = {2, 1, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
        
        @androidx.room.Transaction()
        @org.jetbrains.annotations.Nullable()
        public static java.lang.Object replaceAll(@org.jetbrains.annotations.NotNull()
        com.armada.expiryapp.data.db.dao.OutletDao $this, @org.jetbrains.annotations.NotNull()
        java.util.List<com.armada.expiryapp.data.db.entity.Outlet> outlets, @org.jetbrains.annotations.NotNull()
        kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
            return null;
        }
    }
}