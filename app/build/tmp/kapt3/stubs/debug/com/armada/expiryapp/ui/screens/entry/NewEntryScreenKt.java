package com.armada.expiryapp.ui.screens.entry;

import android.view.HapticFeedbackConstants;
import androidx.compose.foundation.ExperimentalFoundationApi;
import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.foundation.text.KeyboardOptions;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.ButtonDefaults;
import androidx.compose.material3.CardDefaults;
import androidx.compose.material3.OutlinedTextFieldDefaults;
import androidx.compose.material3.SnackbarDuration;
import androidx.compose.material3.SnackbarHostState;
import androidx.compose.material3.SnackbarResult;
import androidx.compose.material3.SwipeToDismissBoxValue;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.input.ImeAction;
import androidx.compose.ui.text.input.KeyboardType;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.unit.Dp;
import androidx.paging.LoadState;
import com.armada.expiryapp.data.db.entity.ExpiryEntry;
import com.armada.expiryapp.data.db.entity.Item;
import com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.ActiveField;
import com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.SaveDialogState;
import com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.UnlinkedItemState;
import com.armada.expiryapp.ui.theme.ArmadaColors;
import java.time.LocalDate;

@kotlin.Metadata(mv = {2, 1, 0}, k = 2, xi = 48, d1 = {"\u0000|\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u001f\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u001a2\u0010\u0000\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u001a\u001a\u0010\u0007\u001a\u00020\u00012\u0006\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0003\u001a \u0010\f\u001a\u00020\u00012\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0003\u001a \u0010\u000e\u001a\u00020\u00012\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0003\u001a\u00e2\u0001\u0010\u0010\u001a\u00020\u00012\u0006\u0010\u0011\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\t2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\t2\u0006\u0010\u0019\u001a\u00020\t2\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001c0\u001b2\u0006\u0010\u001d\u001a\u00020\u00152\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\u0012\u0010!\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\"2\u0012\u0010#\u001a\u000e\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\u00010\"2\u0012\u0010$\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\"2\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0003\u001a8\u0010\'\u001a\u00020\u00012\u0006\u0010\u0012\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\t2\f\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0003\u001aL\u0010)\u001a\u00020\u00012\u0006\u0010\u0011\u001a\u00020\t2\u0006\u0010*\u001a\u00020\u00152\f\u0010+\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010,\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010-\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0003\u001a<\u0010.\u001a\u00020\u00012\u0006\u0010/\u001a\u00020\t2\u0012\u00100\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\"2\f\u00101\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0003\u001aX\u00102\u001a\u00020\u00012\u0006\u0010/\u001a\u00020\t2\u0012\u00100\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\"2\f\u00103\u001a\b\u0012\u0004\u0012\u00020\u001c0\u001b2\u0012\u00104\u001a\u000e\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\u00010\"2\u0006\u0010\u001d\u001a\u00020\u00152\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0003\u001a(\u00105\u001a\u00020\u00012\u0006\u00106\u001a\u00020\u001c2\f\u00107\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0003\u001aT\u00108\u001a\u00020\u00012\u0006\u00109\u001a\u00020\t2\u0006\u0010*\u001a\u00020\u00152\u0006\u0010:\u001a\u00020\u00152\f\u0010;\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010<\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010=\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0003\u001a0\u0010>\u001a\u00020\u00012\u0006\u00109\u001a\u00020\t2\u0006\u0010*\u001a\u00020\u00152\f\u0010;\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0003\u001a\u0012\u0010?\u001a\u00020\u00012\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0003\u001a8\u0010@\u001a\u00020\u00012\u0006\u0010A\u001a\u00020B2\u0006\u0010C\u001a\u00020D2\u0006\u0010E\u001a\u00020F2\f\u0010G\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0003\u001a\u001a\u0010H\u001a\u00020\u00012\u0006\u0010I\u001a\u00020J2\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0003\u001ay\u0010K\u001a\u00020\u00012\u0006\u0010A\u001a\u00020B2\f\u0010L\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052K\u0010M\u001aG\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\bO\u0012\b\bP\u0012\u0004\b\b(Q\u0012\u0013\u0012\u00110D\u00a2\u0006\f\bO\u0012\b\bP\u0012\u0004\b\b(R\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\bO\u0012\b\bP\u0012\u0004\b\b(S\u0012\u0004\u0012\u00020\u00010N2\f\u0010T\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001af\u0010U\u001a\u00020\u00012\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010V\u001a\u00020\t2\u0012\u0010W\u001a\u000e\u0012\u0004\u0012\u00020D\u0012\u0004\u0012\u00020\u00010\"2\f\u0010X\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010Y\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\u0012\u0010Z\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\"2\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0003\u001ai\u0010[\u001a\u00020\u00012\u0006\u0010\\\u001a\u00020\t2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010]\u001a\u00020\u00152\b\b\u0002\u0010^\u001a\u00020\u00152\u0010\b\u0002\u0010_\u001a\n\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u00052\b\b\u0002\u0010`\u001a\u00020a2\b\b\u0002\u0010b\u001a\u00020c2\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0003\u00a2\u0006\u0004\bd\u0010e\u001a0\u0010f\u001a\u00020\u00012\u0006\u0010\\\u001a\u00020\t2\u0006\u0010g\u001a\u00020\u00152\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0003\u00a8\u0006h"}, d2 = {"NewEntryScreen", "", "viewModel", "Lcom/armada/expiryapp/ui/screens/entry/NewEntryViewModel;", "onScanClick", "Lkotlin/Function0;", "onOcrClick", "OutletBanner", "outletName", "", "modifier", "Landroidx/compose/ui/Modifier;", "BulkModeBanner", "onTurnOff", "RepeatChip", "onClick", "ItemSection", "barcode", "description", "productCode", "itemFilled", "", "activeField", "Lcom/armada/expiryapp/ui/screens/entry/NewEntryViewModel$ActiveField;", "descriptionQuery", "productCodeQuery", "descriptionResults", "", "Lcom/armada/expiryapp/data/db/entity/Item;", "isSearching", "onBarcodeTap", "onNextTap", "onScanTap", "onDescriptionChange", "Lkotlin/Function1;", "onDescriptionItemSelect", "onProductCodeChange", "onProductCodeLookup", "onClearItem", "ItemFilledCard", "onClear", "BarcodeInputRow", "isActive", "onTap", "onNext", "onScan", "ProductCodeInputRow", "query", "onQueryChange", "onLookup", "DescriptionSearchRow", "results", "onItemSelected", "DescriptionResultItem", "item", "onSelect", "DateSection", "displayValue", "isComplete", "onFieldTap", "onOcrTap", "onCalendarTap", "QtySection", "EntryTableHeader", "EntryTableRow", "entry", "Lcom/armada/expiryapp/data/db/entity/ExpiryEntry;", "index", "", "today", "Ljava/time/LocalDate;", "onLongPress", "SwipeDeleteBackground", "targetValue", "Landroidx/compose/material3/SwipeToDismissBoxValue;", "EditEntryDialog", "onDismiss", "onSave", "Lkotlin/Function3;", "Lkotlin/ParameterName;", "name", "newExpiryIso", "newQty", "newUnit", "onDelete", "NumPad", "selectedUnit", "onDigit", "onClearSingle", "onClearAll", "onUnitSelect", "NumpadKey", "label", "enabled", "isClearKey", "onLongClick", "keyHeight", "Landroidx/compose/ui/unit/Dp;", "fontSize", "Landroidx/compose/ui/unit/TextUnit;", "NumpadKey-fiE7y7o", "(Ljava/lang/String;Lkotlin/jvm/functions/Function0;ZZLkotlin/jvm/functions/Function0;FJLandroidx/compose/ui/Modifier;)V", "UnitKey", "isSelected", "app_debug"})
@kotlin.OptIn(markerClass = {androidx.compose.foundation.ExperimentalFoundationApi.class, androidx.compose.material3.ExperimentalMaterial3Api.class})
public final class NewEntryScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void NewEntryScreen(@org.jetbrains.annotations.NotNull()
    com.armada.expiryapp.ui.screens.entry.NewEntryViewModel viewModel, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onScanClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOcrClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void OutletBanner(java.lang.String outletName, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void BulkModeBanner(kotlin.jvm.functions.Function0<kotlin.Unit> onTurnOff, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void RepeatChip(kotlin.jvm.functions.Function0<kotlin.Unit> onClick, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ItemSection(java.lang.String barcode, java.lang.String description, java.lang.String productCode, boolean itemFilled, com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.ActiveField activeField, java.lang.String descriptionQuery, java.lang.String productCodeQuery, java.util.List<com.armada.expiryapp.data.db.entity.Item> descriptionResults, boolean isSearching, kotlin.jvm.functions.Function0<kotlin.Unit> onBarcodeTap, kotlin.jvm.functions.Function0<kotlin.Unit> onNextTap, kotlin.jvm.functions.Function0<kotlin.Unit> onScanTap, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onDescriptionChange, kotlin.jvm.functions.Function1<? super com.armada.expiryapp.data.db.entity.Item, kotlin.Unit> onDescriptionItemSelect, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onProductCodeChange, kotlin.jvm.functions.Function0<kotlin.Unit> onProductCodeLookup, kotlin.jvm.functions.Function0<kotlin.Unit> onClearItem, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ItemFilledCard(java.lang.String description, java.lang.String barcode, java.lang.String productCode, kotlin.jvm.functions.Function0<kotlin.Unit> onClear, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void BarcodeInputRow(java.lang.String barcode, boolean isActive, kotlin.jvm.functions.Function0<kotlin.Unit> onTap, kotlin.jvm.functions.Function0<kotlin.Unit> onNext, kotlin.jvm.functions.Function0<kotlin.Unit> onScan, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ProductCodeInputRow(java.lang.String query, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onQueryChange, kotlin.jvm.functions.Function0<kotlin.Unit> onLookup, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void DescriptionSearchRow(java.lang.String query, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onQueryChange, java.util.List<com.armada.expiryapp.data.db.entity.Item> results, kotlin.jvm.functions.Function1<? super com.armada.expiryapp.data.db.entity.Item, kotlin.Unit> onItemSelected, boolean isSearching, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void DescriptionResultItem(com.armada.expiryapp.data.db.entity.Item item, kotlin.jvm.functions.Function0<kotlin.Unit> onSelect, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void DateSection(java.lang.String displayValue, boolean isActive, boolean isComplete, kotlin.jvm.functions.Function0<kotlin.Unit> onFieldTap, kotlin.jvm.functions.Function0<kotlin.Unit> onOcrTap, kotlin.jvm.functions.Function0<kotlin.Unit> onCalendarTap, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void QtySection(java.lang.String displayValue, boolean isActive, kotlin.jvm.functions.Function0<kotlin.Unit> onFieldTap, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void EntryTableHeader(androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void EntryTableRow(com.armada.expiryapp.data.db.entity.ExpiryEntry entry, int index, java.time.LocalDate today, kotlin.jvm.functions.Function0<kotlin.Unit> onLongPress, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SwipeDeleteBackground(androidx.compose.material3.SwipeToDismissBoxValue targetValue, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void EditEntryDialog(com.armada.expiryapp.data.db.entity.ExpiryEntry entry, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, kotlin.jvm.functions.Function3<? super java.lang.String, ? super java.lang.Integer, ? super java.lang.String, kotlin.Unit> onSave, kotlin.jvm.functions.Function0<kotlin.Unit> onDelete) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void NumPad(com.armada.expiryapp.ui.screens.entry.NewEntryViewModel.ActiveField activeField, java.lang.String selectedUnit, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onDigit, kotlin.jvm.functions.Function0<kotlin.Unit> onClearSingle, kotlin.jvm.functions.Function0<kotlin.Unit> onClearAll, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onUnitSelect, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void UnitKey(java.lang.String label, boolean isSelected, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, androidx.compose.ui.Modifier modifier) {
    }
}