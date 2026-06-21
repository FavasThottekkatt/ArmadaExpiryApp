package com.armada.expiryapp.ui.screens.settings;

import android.content.Intent;
import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.SnackbarHostState;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.vector.ImageVector;
import androidx.compose.ui.text.font.FontWeight;
import androidx.core.content.FileProvider;
import com.armada.expiryapp.ui.theme.ArmadaColors;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@kotlin.Metadata(mv = {2, 1, 0}, k = 2, xi = 48, d1 = {"\u0000.\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a<\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0007\u001a\u0018\u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0003\u001a\b\u0010\r\u001a\u00020\u0001H\u0003\u001a\u001e\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\u0003H\u0003\u00a8\u0006\u0012"}, d2 = {"SettingsDrawerContent", "", "onReimport", "Lkotlin/Function0;", "onItemLinking", "onTeamLinking", "viewModel", "Lcom/armada/expiryapp/ui/screens/settings/SettingsViewModel;", "SectionHeader", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "title", "", "SectionDivider", "BackupFileRow", "file", "Ljava/io/File;", "onShare", "app_debug"})
public final class SettingsDrawerContentKt {
    
    @androidx.compose.runtime.Composable()
    public static final void SettingsDrawerContent(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onReimport, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onItemLinking, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onTeamLinking, @org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.ui.screens.settings.SettingsViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SectionHeader(androidx.compose.ui.graphics.vector.ImageVector icon, java.lang.String title) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SectionDivider() {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void BackupFileRow(java.io.File file, kotlin.jvm.functions.Function0<kotlin.Unit> onShare) {
    }
}