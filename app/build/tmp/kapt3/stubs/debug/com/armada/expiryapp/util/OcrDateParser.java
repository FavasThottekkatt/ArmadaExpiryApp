package com.armada.expiryapp.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Locale;

@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0002$%B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00140\u000f2\u0006\u0010\u0016\u001a\u00020\u0006J\u000e\u0010\u0017\u001a\u00020\u00062\u0006\u0010\u0018\u001a\u00020\u0019J\u000e\u0010\u001a\u001a\u00020\u00062\u0006\u0010\u0018\u001a\u00020\u0019J\u0014\u0010\u001b\u001a\u00020\u0006*\u00020\u00132\u0006\u0010\u001c\u001a\u00020\u0007H\u0002J*\u0010\u001d\u001a\u0004\u0018\u00010\u00142\u0006\u0010\u001e\u001a\u00020\u00072\u0006\u0010\u001f\u001a\u00020\u00072\u0006\u0010 \u001a\u00020\u00072\u0006\u0010!\u001a\u00020\"H\u0002J\"\u0010#\u001a\u0004\u0018\u00010\u00142\u0006\u0010\u001e\u001a\u00020\u00072\u0006\u0010\u001f\u001a\u00020\u00072\u0006\u0010!\u001a\u00020\"H\u0002R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00060\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00060\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R.\u0010\u000e\u001a\"\u0012\u001e\u0012\u001c\u0012\u0004\u0012\u00020\u0011\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00020\u0013\u0012\u0006\u0012\u0004\u0018\u00010\u00140\u00120\u00100\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006&"}, d2 = {"Lcom/armada/expiryapp/util/OcrDateParser;", "", "<init>", "()V", "MONTHS_SHORT", "", "", "", "MONTHS_FULL", "EXPIRY_LABELS", "", "PRODUCTION_LABELS", "MS", "MF", "patterns", "", "Lkotlin/Pair;", "Lkotlin/text/Regex;", "Lkotlin/Function1;", "Lkotlin/text/MatchResult;", "Lcom/armada/expiryapp/util/OcrDateParser$ParsedDate;", "parseAll", "text", "toRawDigits", "date", "Ljava/time/LocalDate;", "toDisplayString", "g", "index", "tryDate", "year", "month", "day", "conf", "Lcom/armada/expiryapp/util/OcrDateParser$Confidence;", "tryLastDayOf", "Confidence", "ParsedDate", "app_debug"})
public final class OcrDateParser {
    @org.jetbrains.annotations.NotNull()
    private static final java.util.Map<java.lang.String, java.lang.Integer> MONTHS_SHORT = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.Map<java.lang.String, java.lang.Integer> MONTHS_FULL = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.Set<java.lang.String> EXPIRY_LABELS = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.Set<java.lang.String> PRODUCTION_LABELS = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String MS = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String MF = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<kotlin.Pair<kotlin.text.Regex, kotlin.jvm.functions.Function1<kotlin.text.MatchResult, com.armada.expiryapp.util.OcrDateParser.ParsedDate>>> patterns = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.armada.expiryapp.util.OcrDateParser INSTANCE = null;
    
    private OcrDateParser() {
        super();
    }
    
    /**
     * Parse all dates from [text], then classify each by nearby label keywords:
     * - EXPIRY label within 50 chars → keep, force HIGH confidence
     * - PRODUCTION label within 50 chars → mark as production
     * - No label → keep as MEDIUM
     *
     * Return order: expiry-labeled first; if none, unlabeled; if only production, return all so user can pick.
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.armada.expiryapp.util.OcrDateParser.ParsedDate> parseAll(@org.jetbrains.annotations.NotNull()
    java.lang.String text) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String toRawDigits(@org.jetbrains.annotations.NotNull()
    java.time.LocalDate date) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String toDisplayString(@org.jetbrains.annotations.NotNull()
    java.time.LocalDate date) {
        return null;
    }
    
    private final java.lang.String g(kotlin.text.MatchResult $this$g, int index) {
        return null;
    }
    
    private final com.armada.expiryapp.util.OcrDateParser.ParsedDate tryDate(int year, int month, int day, com.armada.expiryapp.util.OcrDateParser.Confidence conf) {
        return null;
    }
    
    private final com.armada.expiryapp.util.OcrDateParser.ParsedDate tryLastDayOf(int year, int month, com.armada.expiryapp.util.OcrDateParser.Confidence conf) {
        return null;
    }
    
    @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003j\u0002\b\u0004j\u0002\b\u0005\u00a8\u0006\u0006"}, d2 = {"Lcom/armada/expiryapp/util/OcrDateParser$Confidence;", "", "<init>", "(Ljava/lang/String;I)V", "HIGH", "MEDIUM", "app_debug"})
    public static enum Confidence {
        /*public static final*/ HIGH /* = new HIGH() */,
        /*public static final*/ MEDIUM /* = new MEDIUM() */;
        
        Confidence() {
        }
        
        @org.jetbrains.annotations.NotNull()
        public static kotlin.enums.EnumEntries<com.armada.expiryapp.util.OcrDateParser.Confidence> getEntries() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0004\b\u0006\u0010\u0007J\t\u0010\f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\r\u001a\u00020\u0005H\u00c6\u0003J\u001d\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0012\u001a\u00020\u0013H\u00d6\u0001J\t\u0010\u0014\u001a\u00020\u0015H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0016"}, d2 = {"Lcom/armada/expiryapp/util/OcrDateParser$ParsedDate;", "", "date", "Ljava/time/LocalDate;", "confidence", "Lcom/armada/expiryapp/util/OcrDateParser$Confidence;", "<init>", "(Ljava/time/LocalDate;Lcom/armada/expiryapp/util/OcrDateParser$Confidence;)V", "getDate", "()Ljava/time/LocalDate;", "getConfidence", "()Lcom/armada/expiryapp/util/OcrDateParser$Confidence;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_debug"})
    public static final class ParsedDate {
        @org.jetbrains.annotations.NotNull()
        private final java.time.LocalDate date = null;
        @org.jetbrains.annotations.NotNull()
        private final com.armada.expiryapp.util.OcrDateParser.Confidence confidence = null;
        
        public ParsedDate(@org.jetbrains.annotations.NotNull()
        java.time.LocalDate date, @org.jetbrains.annotations.NotNull()
        com.armada.expiryapp.util.OcrDateParser.Confidence confidence) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.time.LocalDate getDate() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.armada.expiryapp.util.OcrDateParser.Confidence getConfidence() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.time.LocalDate component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.armada.expiryapp.util.OcrDateParser.Confidence component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.armada.expiryapp.util.OcrDateParser.ParsedDate copy(@org.jetbrains.annotations.NotNull()
        java.time.LocalDate date, @org.jetbrains.annotations.NotNull()
        com.armada.expiryapp.util.OcrDateParser.Confidence confidence) {
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
}