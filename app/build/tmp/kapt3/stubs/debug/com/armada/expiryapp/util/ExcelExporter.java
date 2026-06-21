package com.armada.expiryapp.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.armada.expiryapp.data.db.entity.ExpiryEntry;
import com.armada.expiryapp.data.db.entity.Outlet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u0000 \u001e2\u00020\u0001:\u0001\u001eB\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J6\u0010\t\u001a\u00020\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\b\b\u0002\u0010\u0013\u001a\u00020\u0011J.\u0010\u0014\u001a\u00020\n2\u001e\u0010\u0015\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\u000f\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\f0\u00160\f2\u0006\u0010\u0010\u001a\u00020\u0011J \u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u0011H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\n \b*\u0004\u0018\u00010\u00070\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"Lcom/armada/expiryapp/util/ExcelExporter;", "", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "excelDateFmt", "Ljava/time/format/DateTimeFormatter;", "kotlin.jvm.PlatformType", "buildAndSaveFile", "Ljava/io/File;", "entries", "", "Lcom/armada/expiryapp/data/db/entity/ExpiryEntry;", "outlet", "Lcom/armada/expiryapp/data/db/entity/Outlet;", "merchandiser", "", "salesman", "fileLabel", "buildMultiOutletFile", "outletEntries", "Lkotlin/Pair;", "writeCell", "", "row", "Lorg/apache/poi/xssf/usermodel/XSSFRow;", "col", "", "value", "Companion", "app_debug"})
public final class ExcelExporter {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    private final java.time.format.DateTimeFormatter excelDateFmt = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "ExcelExporter";
    @org.jetbrains.annotations.NotNull()
    public static final com.armada.expiryapp.util.ExcelExporter.Companion Companion = null;
    
    public ExcelExporter(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @kotlin.jvm.Throws(exceptionClasses = {java.lang.Exception.class, java.lang.OutOfMemoryError.class})
    @org.jetbrains.annotations.NotNull()
    public final java.io.File buildAndSaveFile(@org.jetbrains.annotations.NotNull()
    java.util.List<com.armada.expiryapp.data.db.entity.ExpiryEntry> entries, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.db.entity.Outlet outlet, @org.jetbrains.annotations.NotNull()
    java.lang.String merchandiser, @org.jetbrains.annotations.NotNull()
    java.lang.String salesman, @org.jetbrains.annotations.NotNull()
    java.lang.String fileLabel) {
        return null;
    }
    
    @kotlin.jvm.Throws(exceptionClasses = {java.lang.Exception.class, java.lang.OutOfMemoryError.class})
    @org.jetbrains.annotations.NotNull()
    public final java.io.File buildMultiOutletFile(@org.jetbrains.annotations.NotNull()
    java.util.List<? extends kotlin.Pair<com.armada.expiryapp.data.db.entity.Outlet, ? extends java.util.List<com.armada.expiryapp.data.db.entity.ExpiryEntry>>> outletEntries, @org.jetbrains.annotations.NotNull()
    java.lang.String merchandiser) {
        return null;
    }
    
    private final void writeCell(org.apache.poi.xssf.usermodel.XSSFRow row, int col, java.lang.String value) {
    }
    
    @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/armada/expiryapp/util/ExcelExporter$Companion;", "", "<init>", "()V", "TAG", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}