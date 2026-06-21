package com.armada.expiryapp.ui.screens.csvimport;

import android.content.Context;
import androidx.lifecycle.ViewModel;
import com.armada.expiryapp.data.repository.CsvImportRepository;
import com.armada.expiryapp.data.repository.CsvMetadataRepository;
import com.armada.expiryapp.data.repository.ItemRepository;
import com.armada.expiryapp.util.AutoBackup;
import com.armada.expiryapp.util.CsvParseResult;
import com.armada.expiryapp.util.CsvParser;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001:\u0001\u001dB+\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0004\b\n\u0010\u000bJ\b\u0010\u0013\u001a\u00020\u0014H\u0002J\u0006\u0010\u0015\u001a\u00020\u0014J\u0016\u0010\u0016\u001a\u00020\u00142\u0006\u0010\u0017\u001a\u00020\u0018H\u0082@\u00a2\u0006\u0002\u0010\u0019J\u000e\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u001b\u001a\u00020\u001cR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012\u00a8\u0006\u001e"}, d2 = {"Lcom/armada/expiryapp/ui/screens/csvimport/CsvImportViewModel;", "Landroidx/lifecycle/ViewModel;", "context", "Landroid/content/Context;", "csvImportRepository", "Lcom/armada/expiryapp/data/repository/CsvImportRepository;", "csvMetadataRepository", "Lcom/armada/expiryapp/data/repository/CsvMetadataRepository;", "itemRepository", "Lcom/armada/expiryapp/data/repository/ItemRepository;", "<init>", "(Landroid/content/Context;Lcom/armada/expiryapp/data/repository/CsvImportRepository;Lcom/armada/expiryapp/data/repository/CsvMetadataRepository;Lcom/armada/expiryapp/data/repository/ItemRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/armada/expiryapp/ui/screens/csvimport/CsvImportViewModel$UiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "checkStatus", "", "triggerReimport", "parseAssets", "isFirstImport", "", "(ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "confirmImport", "parseResult", "Lcom/armada/expiryapp/util/CsvParseResult;", "UiState", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class CsvImportViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.repository.CsvImportRepository csvImportRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.repository.CsvMetadataRepository csvMetadataRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.repository.ItemRepository itemRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.armada.expiryapp.ui.screens.csvimport.CsvImportViewModel.UiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.armada.expiryapp.ui.screens.csvimport.CsvImportViewModel.UiState> uiState = null;
    
    @javax.inject.Inject()
    public CsvImportViewModel(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.repository.CsvImportRepository csvImportRepository, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.repository.CsvMetadataRepository csvMetadataRepository, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.repository.ItemRepository itemRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.armada.expiryapp.ui.screens.csvimport.CsvImportViewModel.UiState> getUiState() {
        return null;
    }
    
    private final void checkStatus() {
    }
    
    public final void triggerReimport() {
    }
    
    private final java.lang.Object parseAssets(boolean isFirstImport, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    public final void confirmImport(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.util.CsvParseResult parseResult) {
    }
    
    @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0006\u0004\u0005\u0006\u0007\b\tB\t\b\u0004\u00a2\u0006\u0004\b\u0002\u0010\u0003\u0082\u0001\u0006\n\u000b\f\r\u000e\u000f\u00a8\u0006\u0010"}, d2 = {"Lcom/armada/expiryapp/ui/screens/csvimport/CsvImportViewModel$UiState;", "", "<init>", "()V", "CheckingStatus", "Idle", "ShowValidation", "Importing", "ImportComplete", "Error", "Lcom/armada/expiryapp/ui/screens/csvimport/CsvImportViewModel$UiState$CheckingStatus;", "Lcom/armada/expiryapp/ui/screens/csvimport/CsvImportViewModel$UiState$Error;", "Lcom/armada/expiryapp/ui/screens/csvimport/CsvImportViewModel$UiState$Idle;", "Lcom/armada/expiryapp/ui/screens/csvimport/CsvImportViewModel$UiState$ImportComplete;", "Lcom/armada/expiryapp/ui/screens/csvimport/CsvImportViewModel$UiState$Importing;", "Lcom/armada/expiryapp/ui/screens/csvimport/CsvImportViewModel$UiState$ShowValidation;", "app_debug"})
    public static abstract class UiState {
        
        private UiState() {
            super();
        }
        
        @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003\u00a8\u0006\u0004"}, d2 = {"Lcom/armada/expiryapp/ui/screens/csvimport/CsvImportViewModel$UiState$CheckingStatus;", "Lcom/armada/expiryapp/ui/screens/csvimport/CsvImportViewModel$UiState;", "<init>", "()V", "app_debug"})
        public static final class CheckingStatus extends com.armada.expiryapp.ui.screens.csvimport.CsvImportViewModel.UiState {
            @org.jetbrains.annotations.NotNull()
            public static final com.armada.expiryapp.ui.screens.csvimport.CsvImportViewModel.UiState.CheckingStatus INSTANCE = null;
            
            private CheckingStatus() {
            }
        }
        
        @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\t\u0010\b\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\t\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u00d6\u0003J\t\u0010\u000e\u001a\u00020\u000fH\u00d6\u0001J\t\u0010\u0010\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\u0011"}, d2 = {"Lcom/armada/expiryapp/ui/screens/csvimport/CsvImportViewModel$UiState$Error;", "Lcom/armada/expiryapp/ui/screens/csvimport/CsvImportViewModel$UiState;", "message", "", "<init>", "(Ljava/lang/String;)V", "getMessage", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app_debug"})
        public static final class Error extends com.armada.expiryapp.ui.screens.csvimport.CsvImportViewModel.UiState {
            @org.jetbrains.annotations.NotNull()
            private final java.lang.String message = null;
            
            public Error(@org.jetbrains.annotations.NotNull()
            java.lang.String message) {
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String getMessage() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String component1() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.armada.expiryapp.ui.screens.csvimport.CsvImportViewModel.UiState.Error copy(@org.jetbrains.annotations.NotNull()
            java.lang.String message) {
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
        
        @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003\u00a8\u0006\u0004"}, d2 = {"Lcom/armada/expiryapp/ui/screens/csvimport/CsvImportViewModel$UiState$Idle;", "Lcom/armada/expiryapp/ui/screens/csvimport/CsvImportViewModel$UiState;", "<init>", "()V", "app_debug"})
        public static final class Idle extends com.armada.expiryapp.ui.screens.csvimport.CsvImportViewModel.UiState {
            @org.jetbrains.annotations.NotNull()
            public static final com.armada.expiryapp.ui.screens.csvimport.CsvImportViewModel.UiState.Idle INSTANCE = null;
            
            private Idle() {
            }
        }
        
        @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003\u00a8\u0006\u0004"}, d2 = {"Lcom/armada/expiryapp/ui/screens/csvimport/CsvImportViewModel$UiState$ImportComplete;", "Lcom/armada/expiryapp/ui/screens/csvimport/CsvImportViewModel$UiState;", "<init>", "()V", "app_debug"})
        public static final class ImportComplete extends com.armada.expiryapp.ui.screens.csvimport.CsvImportViewModel.UiState {
            @org.jetbrains.annotations.NotNull()
            public static final com.armada.expiryapp.ui.screens.csvimport.CsvImportViewModel.UiState.ImportComplete INSTANCE = null;
            
            private ImportComplete() {
            }
        }
        
        @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003\u00a8\u0006\u0004"}, d2 = {"Lcom/armada/expiryapp/ui/screens/csvimport/CsvImportViewModel$UiState$Importing;", "Lcom/armada/expiryapp/ui/screens/csvimport/CsvImportViewModel$UiState;", "<init>", "()V", "app_debug"})
        public static final class Importing extends com.armada.expiryapp.ui.screens.csvimport.CsvImportViewModel.UiState {
            @org.jetbrains.annotations.NotNull()
            public static final com.armada.expiryapp.ui.screens.csvimport.CsvImportViewModel.UiState.Importing INSTANCE = null;
            
            private Importing() {
            }
        }
        
        @kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0004\b\u0006\u0010\u0007J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\f\u001a\u00020\u0005H\u00c6\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u000e\u001a\u00020\u00052\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u00d6\u0003J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001J\t\u0010\u0013\u001a\u00020\u0014H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\n\u00a8\u0006\u0015"}, d2 = {"Lcom/armada/expiryapp/ui/screens/csvimport/CsvImportViewModel$UiState$ShowValidation;", "Lcom/armada/expiryapp/ui/screens/csvimport/CsvImportViewModel$UiState;", "parseResult", "Lcom/armada/expiryapp/util/CsvParseResult;", "isFirstImport", "", "<init>", "(Lcom/armada/expiryapp/util/CsvParseResult;Z)V", "getParseResult", "()Lcom/armada/expiryapp/util/CsvParseResult;", "()Z", "component1", "component2", "copy", "equals", "other", "", "hashCode", "", "toString", "", "app_debug"})
        public static final class ShowValidation extends com.armada.expiryapp.ui.screens.csvimport.CsvImportViewModel.UiState {
            @org.jetbrains.annotations.NotNull()
            private final com.armada.expiryapp.util.CsvParseResult parseResult = null;
            private final boolean isFirstImport = false;
            
            public ShowValidation(@org.jetbrains.annotations.NotNull()
            com.armada.expiryapp.util.CsvParseResult parseResult, boolean isFirstImport) {
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.armada.expiryapp.util.CsvParseResult getParseResult() {
                return null;
            }
            
            public final boolean isFirstImport() {
                return false;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.armada.expiryapp.util.CsvParseResult component1() {
                return null;
            }
            
            public final boolean component2() {
                return false;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.armada.expiryapp.ui.screens.csvimport.CsvImportViewModel.UiState.ShowValidation copy(@org.jetbrains.annotations.NotNull()
            com.armada.expiryapp.util.CsvParseResult parseResult, boolean isFirstImport) {
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
}