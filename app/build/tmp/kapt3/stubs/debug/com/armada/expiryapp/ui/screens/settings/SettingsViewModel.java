package com.armada.expiryapp.ui.screens.settings;

import android.content.Context;
import androidx.lifecycle.ViewModel;
import com.armada.expiryapp.BuildConfig;
import com.armada.expiryapp.data.db.entity.CsvMetadata;
import com.armada.expiryapp.data.repository.CsvMetadataRepository;
import com.armada.expiryapp.data.repository.OutletItemLinkRepository;
import com.armada.expiryapp.data.session.SessionHolder;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.flow.SharedFlow;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;
import java.io.File;
import javax.inject.Inject;

@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0010\b\u0007\u0018\u00002\u00020\u0001B+\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0004\b\n\u0010\u000bJ\u0006\u00100\u001a\u000201J\u0006\u0010:\u001a\u000201J\u0006\u0010;\u001a\u000201J\u0006\u0010<\u001a\u000201J\u000e\u0010=\u001a\u0002012\u0006\u0010>\u001a\u00020\u0014J\u0006\u0010?\u001a\u000201J\u0006\u0010@\u001a\u000201R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u000e0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00140\u000e0\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00140\u000e0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0011R\u0016\u0010\u0017\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00180\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0019\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00180\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0011R\u0014\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001cX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0014\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00180\u001cX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00180\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010!R\u0011\u0010%\u001a\u00020&8F\u00a2\u0006\u0006\u001a\u0004\b\'\u0010(R\u0011\u0010)\u001a\u00020\u00188F\u00a2\u0006\u0006\u001a\u0004\b*\u0010+R\u0014\u0010,\u001a\b\u0012\u0004\u0012\u00020-0\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010.\u001a\b\u0012\u0004\u0012\u00020-0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010\u0011R\u0014\u00102\u001a\u00020\u0018X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u0010+R\u0014\u00104\u001a\u00020-X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b5\u00106R\u0011\u00107\u001a\u00020\u00148F\u00a2\u0006\u0006\u001a\u0004\b8\u00109\u00a8\u0006A"}, d2 = {"Lcom/armada/expiryapp/ui/screens/settings/SettingsViewModel;", "Landroidx/lifecycle/ViewModel;", "context", "Landroid/content/Context;", "csvMetadataRepository", "Lcom/armada/expiryapp/data/repository/CsvMetadataRepository;", "sessionHolder", "Lcom/armada/expiryapp/data/session/SessionHolder;", "linkRepository", "Lcom/armada/expiryapp/data/repository/OutletItemLinkRepository;", "<init>", "(Landroid/content/Context;Lcom/armada/expiryapp/data/repository/CsvMetadataRepository;Lcom/armada/expiryapp/data/session/SessionHolder;Lcom/armada/expiryapp/data/repository/OutletItemLinkRepository;)V", "csvMetadata", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/armada/expiryapp/data/db/entity/CsvMetadata;", "getCsvMetadata", "()Lkotlinx/coroutines/flow/StateFlow;", "_backupFiles", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Ljava/io/File;", "backupFiles", "getBackupFiles", "_crashLogContent", "", "crashLogContent", "getCrashLogContent", "_shareRequest", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "Lcom/armada/expiryapp/ui/screens/settings/ShareRequest;", "shareRequest", "Lkotlinx/coroutines/flow/SharedFlow;", "getShareRequest", "()Lkotlinx/coroutines/flow/SharedFlow;", "_snackMessage", "snackMessage", "getSnackMessage", "hasOutlet", "", "getHasOutlet", "()Z", "outletName", "getOutletName", "()Ljava/lang/String;", "_linkedItemCount", "", "linkedItemCount", "getLinkedItemCount", "refreshLinkedCount", "", "appVersion", "getAppVersion", "versionCode", "getVersionCode", "()I", "crashLogFile", "getCrashLogFile", "()Ljava/io/File;", "loadBackups", "loadCrashLog", "dismissCrashLog", "shareBackup", "file", "shareCrashLog", "clearCrashLog", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class SettingsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.repository.CsvMetadataRepository csvMetadataRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.session.SessionHolder sessionHolder = null;
    @org.jetbrains.annotations.NotNull()
    private final com.armada.expiryapp.data.repository.OutletItemLinkRepository linkRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.armada.expiryapp.data.db.entity.CsvMetadata>> csvMetadata = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<java.io.File>> _backupFiles = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<java.io.File>> backupFiles = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _crashLogContent = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> crashLogContent = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableSharedFlow<com.armada.expiryapp.ui.screens.settings.ShareRequest> _shareRequest = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.SharedFlow<com.armada.expiryapp.ui.screens.settings.ShareRequest> shareRequest = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableSharedFlow<java.lang.String> _snackMessage = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.SharedFlow<java.lang.String> snackMessage = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Integer> _linkedItemCount = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> linkedItemCount = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String appVersion = "1.0";
    private final int versionCode = com.armada.expiryapp.BuildConfig.VERSION_CODE;
    
    @javax.inject.Inject()
    public SettingsViewModel(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.repository.CsvMetadataRepository csvMetadataRepository, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.session.SessionHolder sessionHolder, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.data.repository.OutletItemLinkRepository linkRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.armada.expiryapp.data.db.entity.CsvMetadata>> getCsvMetadata() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<java.io.File>> getBackupFiles() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getCrashLogContent() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.SharedFlow<com.armada.expiryapp.ui.screens.settings.ShareRequest> getShareRequest() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.SharedFlow<java.lang.String> getSnackMessage() {
        return null;
    }
    
    public final boolean getHasOutlet() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOutletName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> getLinkedItemCount() {
        return null;
    }
    
    public final void refreshLinkedCount() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getAppVersion() {
        return null;
    }
    
    public final int getVersionCode() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.io.File getCrashLogFile() {
        return null;
    }
    
    public final void loadBackups() {
    }
    
    public final void loadCrashLog() {
    }
    
    public final void dismissCrashLog() {
    }
    
    public final void shareBackup(@org.jetbrains.annotations.NotNull()
    java.io.File file) {
    }
    
    public final void shareCrashLog() {
    }
    
    public final void clearCrashLog() {
    }
}