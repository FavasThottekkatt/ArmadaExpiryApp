package com.armada.expiryapp.ui.screens.itemlinking;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.material3.ExposedDropdownMenuDefaults;
import androidx.compose.material3.OutlinedTextFieldDefaults;
import androidx.compose.material3.SnackbarHostState;
import androidx.compose.material3.TopAppBarDefaults;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextOverflow;
import com.armada.expiryapp.data.db.entity.Item;
import com.armada.expiryapp.data.db.entity.TeamLink;
import com.armada.expiryapp.ui.theme.ArmadaColors;

@kotlin.Metadata(mv = {2, 1, 0}, k = 2, xi = 48, d1 = {"\u0000>\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\u001a \u0010\u0000\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u001a:\u0010\u0006\u001a\u00020\u00012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000b2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\u000eH\u0003\u001a&\u0010\u000f\u001a\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u00a8\u0006\u0015"}, d2 = {"ItemLinkingScreen", "", "viewModel", "Lcom/armada/expiryapp/ui/screens/itemlinking/ItemLinkingViewModel;", "onBack", "Lkotlin/Function0;", "OutletDropdown", "linkedOutlets", "", "Lcom/armada/expiryapp/data/db/entity/TeamLink;", "selectedOutletCode", "", "selectedOutletName", "onSelect", "Lkotlin/Function1;", "ItemLinkRow", "item", "Lcom/armada/expiryapp/data/db/entity/Item;", "isLinked", "", "onToggle", "app_debug"})
public final class ItemLinkingScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void ItemLinkingScreen(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.ui.screens.itemlinking.ItemLinkingViewModel viewModel, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void OutletDropdown(java.util.List<com.armada.expiryapp.data.db.entity.TeamLink> linkedOutlets, java.lang.String selectedOutletCode, java.lang.String selectedOutletName, kotlin.jvm.functions.Function1<? super com.armada.expiryapp.data.db.entity.TeamLink, kotlin.Unit> onSelect) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ItemLinkRow(com.armada.expiryapp.data.db.entity.Item item, boolean isLinked, kotlin.jvm.functions.Function0<kotlin.Unit> onToggle) {
    }
}