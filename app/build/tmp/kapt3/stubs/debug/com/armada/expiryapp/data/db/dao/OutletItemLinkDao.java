package com.armada.expiryapp.data.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.armada.expiryapp.data.db.entity.OutletItemLink;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010\u0007\u001a\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00050\nH\u00a7@\u00a2\u0006\u0002\u0010\u000bJ\u001e\u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000eH\u00a7@\u00a2\u0006\u0002\u0010\u0010J\u0016\u0010\u0011\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000eH\u00a7@\u00a2\u0006\u0002\u0010\u0012J\u001c\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00050\n2\u0006\u0010\r\u001a\u00020\u000eH\u00a7@\u00a2\u0006\u0002\u0010\u0012J\u0016\u0010\u0014\u001a\u00020\u00152\u0006\u0010\r\u001a\u00020\u000eH\u00a7@\u00a2\u0006\u0002\u0010\u0012J\u0016\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00150\u00172\u0006\u0010\r\u001a\u00020\u000eH\'J$\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00050\n2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0019\u001a\u00020\u000eH\u00a7@\u00a2\u0006\u0002\u0010\u0010J\u001e\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000eH\u00a7@\u00a2\u0006\u0002\u0010\u0010\u00a8\u0006\u001c"}, d2 = {"Lcom/armada/expiryapp/data/db/dao/OutletItemLinkDao;", "", "insert", "", "link", "Lcom/armada/expiryapp/data/db/entity/OutletItemLink;", "(Lcom/armada/expiryapp/data/db/entity/OutletItemLink;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertAll", "", "links", "", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteByOutletAndBarcode", "outletCode", "", "barcode", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteAllForOutlet", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllForOutlet", "getCountForOutlet", "", "getCountForOutletFlow", "Lkotlinx/coroutines/flow/Flow;", "searchForDropdown", "query", "isLinked", "", "app_debug"})
@androidx.room.Dao()
public abstract interface OutletItemLinkDao {
    
    @androidx.room.Insert(onConflict = 5)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.entity.OutletItemLink link, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Insert(onConflict = 5)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertAll(@org.jetbrains.annotations.NotNull()
    java.util.List<com.armada.expiryapp.data.db.entity.OutletItemLink> links, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM outlet_item_links WHERE outletCode = :outletCode AND barcode = :barcode")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteByOutletAndBarcode(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    java.lang.String barcode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM outlet_item_links WHERE outletCode = :outletCode")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAllForOutlet(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM outlet_item_links WHERE outletCode = :outletCode ORDER BY description ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllForOutlet(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.armada.expiryapp.data.db.entity.OutletItemLink>> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM outlet_item_links WHERE outletCode = :outletCode")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getCountForOutlet(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM outlet_item_links WHERE outletCode = :outletCode")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Integer> getCountForOutletFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode);
    
    @androidx.room.Query(value = "\n        SELECT * FROM outlet_item_links\n        WHERE outletCode = :outletCode\n          AND (description LIKE \'%\' || :query || \'%\'\n               OR productCode  LIKE \'%\' || :query || \'%\')\n        ORDER BY description ASC\n        LIMIT 50\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object searchForDropdown(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.armada.expiryapp.data.db.entity.OutletItemLink>> $completion);
    
    @androidx.room.Query(value = "\n        SELECT EXISTS(\n            SELECT 1 FROM outlet_item_links\n            WHERE outletCode = :outletCode AND barcode = :barcode\n        )\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object isLinked(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    java.lang.String barcode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion);
}