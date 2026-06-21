package com.armada.expiryapp.util;

import android.content.Context;
import com.armada.expiryapp.data.db.entity.Item;
import com.armada.expiryapp.data.db.entity.Outlet;
import java.security.MessageDigest;

@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0019\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BE\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\u0006\u0012\u0006\u0010\t\u001a\u00020\u0006\u0012\u0006\u0010\n\u001a\u00020\u0006\u0012\u0006\u0010\u000b\u001a\u00020\u0006\u00a2\u0006\u0004\b\f\u0010\rJ\u000f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u001c\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u001d\u001a\u00020\u0006H\u00c6\u0003JU\u0010\u001e\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\u00062\b\b\u0002\u0010\t\u001a\u00020\u00062\b\b\u0002\u0010\n\u001a\u00020\u00062\b\b\u0002\u0010\u000b\u001a\u00020\u0006H\u00c6\u0001J\u0013\u0010\u001f\u001a\u00020 2\b\u0010!\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\"\u001a\u00020\u0006H\u00d6\u0001J\t\u0010#\u001a\u00020$H\u00d6\u0001R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0007\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011R\u0011\u0010\b\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0011R\u0011\u0010\t\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0011R\u0011\u0010\n\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0011R\u0011\u0010\u000b\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0011\u00a8\u0006%"}, d2 = {"Lcom/armada/expiryapp/util/ItemParseResult;", "", "validItems", "", "Lcom/armada/expiryapp/data/db/entity/Item;", "totalRows", "", "blankBarcodes", "blankDescriptions", "duplicateBarcodes", "nullProductCodes", "skippedRows", "<init>", "(Ljava/util/List;IIIIII)V", "getValidItems", "()Ljava/util/List;", "getTotalRows", "()I", "getBlankBarcodes", "getBlankDescriptions", "getDuplicateBarcodes", "getNullProductCodes", "getSkippedRows", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "", "other", "hashCode", "toString", "", "app_debug"})
public final class ItemParseResult {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.armada.expiryapp.data.db.entity.Item> validItems = null;
    private final int totalRows = 0;
    private final int blankBarcodes = 0;
    private final int blankDescriptions = 0;
    private final int duplicateBarcodes = 0;
    private final int nullProductCodes = 0;
    private final int skippedRows = 0;
    
    public ItemParseResult(@org.jetbrains.annotations.NotNull()
    java.util.List<com.armada.expiryapp.data.db.entity.Item> validItems, int totalRows, int blankBarcodes, int blankDescriptions, int duplicateBarcodes, int nullProductCodes, int skippedRows) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.armada.expiryapp.data.db.entity.Item> getValidItems() {
        return null;
    }
    
    public final int getTotalRows() {
        return 0;
    }
    
    public final int getBlankBarcodes() {
        return 0;
    }
    
    public final int getBlankDescriptions() {
        return 0;
    }
    
    public final int getDuplicateBarcodes() {
        return 0;
    }
    
    public final int getNullProductCodes() {
        return 0;
    }
    
    public final int getSkippedRows() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.armada.expiryapp.data.db.entity.Item> component1() {
        return null;
    }
    
    public final int component2() {
        return 0;
    }
    
    public final int component3() {
        return 0;
    }
    
    public final int component4() {
        return 0;
    }
    
    public final int component5() {
        return 0;
    }
    
    public final int component6() {
        return 0;
    }
    
    public final int component7() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.armada.expiryapp.util.ItemParseResult copy(@org.jetbrains.annotations.NotNull()
    java.util.List<com.armada.expiryapp.data.db.entity.Item> validItems, int totalRows, int blankBarcodes, int blankDescriptions, int duplicateBarcodes, int nullProductCodes, int skippedRows) {
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