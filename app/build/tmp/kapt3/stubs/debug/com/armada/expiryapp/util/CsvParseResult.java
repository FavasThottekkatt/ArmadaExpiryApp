package com.armada.expiryapp.util;

import android.content.Context;
import com.armada.expiryapp.data.db.entity.Item;
import com.armada.expiryapp.data.db.entity.Outlet;
import java.security.MessageDigest;

@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0010\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\'\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0007\u00a2\u0006\u0004\b\t\u0010\nJ\t\u0010\u0012\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0013\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u0015\u001a\u00020\u0007H\u00c6\u0003J1\u0010\u0016\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u0007H\u00c6\u0001J\u0013\u0010\u0017\u001a\u00020\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001a\u001a\u00020\u001bH\u00d6\u0001J\t\u0010\u001c\u001a\u00020\u0007H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\b\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010\u00a8\u0006\u001d"}, d2 = {"Lcom/armada/expiryapp/util/CsvParseResult;", "", "items", "Lcom/armada/expiryapp/util/ItemParseResult;", "outlets", "Lcom/armada/expiryapp/util/OutletParseResult;", "itemsMd5", "", "outletsMd5", "<init>", "(Lcom/armada/expiryapp/util/ItemParseResult;Lcom/armada/expiryapp/util/OutletParseResult;Ljava/lang/String;Ljava/lang/String;)V", "getItems", "()Lcom/armada/expiryapp/util/ItemParseResult;", "getOutlets", "()Lcom/armada/expiryapp/util/OutletParseResult;", "getItemsMd5", "()Ljava/lang/String;", "getOutletsMd5", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
public final class CsvParseResult {
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.util.ItemParseResult items = null;
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.util.OutletParseResult outlets = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String itemsMd5 = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String outletsMd5 = null;
    
    public CsvParseResult(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.util.ItemParseResult items, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.util.OutletParseResult outlets, @org.jetbrains.annotations.NotNull()
    java.lang.String itemsMd5, @org.jetbrains.annotations.NotNull()
    java.lang.String outletsMd5) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.armada.expiryapp.util.ItemParseResult getItems() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.armada.expiryapp.util.OutletParseResult getOutlets() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getItemsMd5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOutletsMd5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.armada.expiryapp.util.ItemParseResult component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.armada.expiryapp.util.OutletParseResult component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.armada.expiryapp.util.CsvParseResult copy(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.util.ItemParseResult items, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.util.OutletParseResult outlets, @org.jetbrains.annotations.NotNull()
    java.lang.String itemsMd5, @org.jetbrains.annotations.NotNull()
    java.lang.String outletsMd5) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}