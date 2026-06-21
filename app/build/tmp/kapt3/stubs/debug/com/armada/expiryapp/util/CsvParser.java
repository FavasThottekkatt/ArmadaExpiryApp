package com.armada.expiryapp.util;

import android.content.Context;
import com.armada.expiryapp.data.db.entity.Item;
import com.armada.expiryapp.data.db.entity.Outlet;
import java.security.MessageDigest;

@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u00c6\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007J\u0016\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t2\u0006\u0010\u000b\u001a\u00020\fH\u0002J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000b\u001a\u00020\fH\u0002J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u000b\u001a\u00020\fH\u0002J\u001b\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\n0\t2\u0006\u0010\u0012\u001a\u00020\nH\u0000\u00a2\u0006\u0002\b\u0013J\u0010\u0010\u0014\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0002\u00a8\u0006\u0015"}, d2 = {"Lcom/armada/expiryapp/util/CsvParser;", "", "<init>", "()V", "parse", "Lcom/armada/expiryapp/util/CsvParseResult;", "context", "Landroid/content/Context;", "toLines", "", "", "bytes", "", "parseItems", "Lcom/armada/expiryapp/util/ItemParseResult;", "parseOutlets", "Lcom/armada/expiryapp/util/OutletParseResult;", "parseLine", "line", "parseLine$app_debug", "md5", "app_debug"})
public final class CsvParser {
    @org.jetbrains.annotations.NotNull()
    public static final com.armada.expiryapp.util.CsvParser INSTANCE = null;
    
    private CsvParser() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.armada.expiryapp.util.CsvParseResult parse(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    private final java.util.List<java.lang.String> toLines(byte[] bytes) {
        return null;
    }
    
    private final com.armada.expiryapp.util.ItemParseResult parseItems(byte[] bytes) {
        return null;
    }
    
    private final com.armada.expiryapp.util.OutletParseResult parseOutlets(byte[] bytes) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> parseLine$app_debug(@org.jetbrains.annotations.NotNull()
    java.lang.String line) {
        return null;
    }
    
    private final java.lang.String md5(byte[] bytes) {
        return null;
    }
}