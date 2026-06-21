package com.armada.expiryapp.util;

import com.armada.expiryapp.data.db.entity.ExpiryEntry;
import com.armada.expiryapp.ui.theme.ArmadaColors;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\b\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\b\u00a8\u0006\t"}, d2 = {"Lcom/armada/expiryapp/util/Status;", "", "<init>", "(Ljava/lang/String;I)V", "EXPIRED", "WITHIN_30", "WITHIN_60", "WITHIN_90", "SAFE", "app_debug"})
public enum Status {
    /*public static final*/ EXPIRED /* = new EXPIRED() */,
    /*public static final*/ WITHIN_30 /* = new WITHIN_30() */,
    /*public static final*/ WITHIN_60 /* = new WITHIN_60() */,
    /*public static final*/ WITHIN_90 /* = new WITHIN_90() */,
    /*public static final*/ SAFE /* = new SAFE() */;
    
    Status() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.armada.expiryapp.util.Status> getEntries() {
        return null;
    }
}