package com.armada.expiryapp.data.repository;

import com.armada.expiryapp.data.db.dao.OutletItemLinkDao;
import com.armada.expiryapp.data.db.entity.OutletItemLink;
import kotlinx.coroutines.flow.Flow;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u0016\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0086@\u00a2\u0006\u0002\u0010\nJ\u001c\u0010\u000b\u001a\u00020\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\t0\u000eH\u0086@\u00a2\u0006\u0002\u0010\u000fJ\u001e\u0010\u0010\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0012H\u0086@\u00a2\u0006\u0002\u0010\u0014J\u0016\u0010\u0015\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\u0012H\u0086@\u00a2\u0006\u0002\u0010\u0016J\u001c\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\t0\u000e2\u0006\u0010\u0011\u001a\u00020\u0012H\u0086@\u00a2\u0006\u0002\u0010\u0016J\u0016\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0011\u001a\u00020\u0012H\u0086@\u00a2\u0006\u0002\u0010\u0016J\u0014\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00190\u001b2\u0006\u0010\u0011\u001a\u00020\u0012J$\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\t0\u000e2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u001d\u001a\u00020\u0012H\u0086@\u00a2\u0006\u0002\u0010\u0014J\u001e\u0010\u001e\u001a\u00020\u001f2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0012H\u0086@\u00a2\u0006\u0002\u0010\u0014R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006 "}, d2 = {"Lcom/armada/expiryapp/data/repository/OutletItemLinkRepository;", "", "dao", "Lcom/armada/expiryapp/data/db/dao/OutletItemLinkDao;", "<init>", "(Lcom/armada/expiryapp/data/db/dao/OutletItemLinkDao;)V", "insert", "", "link", "Lcom/armada/expiryapp/data/db/entity/OutletItemLink;", "(Lcom/armada/expiryapp/data/db/entity/OutletItemLink;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertAll", "", "links", "", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteByOutletAndBarcode", "outletCode", "", "barcode", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteAllForOutlet", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllForOutlet", "getCountForOutlet", "", "getCountForOutletFlow", "Lkotlinx/coroutines/flow/Flow;", "searchForDropdown", "query", "isLinked", "", "app_debug"})
public final class OutletItemLinkRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.db.dao.OutletItemLinkDao dao = null;
    
    @javax.inject.Inject()
    public OutletItemLinkRepository(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.dao.OutletItemLinkDao dao) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.entity.OutletItemLink link, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object insertAll(@org.jetbrains.annotations.NotNull()
    java.util.List<com.armada.expiryapp.data.db.entity.OutletItemLink> links, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteByOutletAndBarcode(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    java.lang.String barcode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteAllForOutlet(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getAllForOutlet(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.armada.expiryapp.data.db.entity.OutletItemLink>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getCountForOutlet(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Integer> getCountForOutletFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object searchForDropdown(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.armada.expiryapp.data.db.entity.OutletItemLink>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object isLinked(@org.jetbrains.annotations.NotNull()
    java.lang.String outletCode, @org.jetbrains.annotations.NotNull()
    java.lang.String barcode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
}