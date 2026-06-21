# ARMADA EXPIRY APP — CORRECTIONS PHASE 3
## 5 Corrections from Device Testing
## Implement in order. Compile after each correction. Zero errors before next.

---

## CORRECTION 1 — ITEM LINKING NOT ACTIVE

### Problem
Item Linking menu shows "Item Linking — No Outlet Selected" in grey.
Nothing happens when tapped. User cannot link SKUs to outlets.

### Root Cause
Item Linking currently checks `sessionHolder.outletCode` which is only set
after the user taps Next → on the Dashboard. If the user opens the menu
before going to New Entry, sessionHolder is empty → button is disabled.

### Fix — Add outlet selector INSIDE Item Linking screen

**ItemLinkingScreen.kt changes:**

The Item Linking screen must have its OWN outlet selector at the top.
It does NOT depend on SessionHolder. It independently loads linked outlets.

**New screen layout (top to bottom):**
```
┌─────────────────────────────────────────────────────┐
│ ← Back    ITEM LINKING                              │
├─────────────────────────────────────────────────────┤
│  Select Outlet:                                     │
│  [ Searchable dropdown — linked outlets only   ▼ ] │
│  (shows only outlets from TeamLink for this device) │
├─────────────────────────────────────────────────────┤
│  [shown only after outlet selected]                 │
│  [ 🔍 Search items by name or barcode............ ] │
├─────────────────────────────────────────────────────┤
│  [Link All Shown]     [Clear All Links]             │
├─────────────────────────────────────────────────────┤
│  ☑ AMICELLI HAZELNUT CREAM WAFER 12.5G  │ AL1004  │
│  ☐ EVIAN MINERAL WATER 500ML            │ EV500   │
│  ☑ KITKAT CHUNKY MILK CHOCOLATE 40G     │ KK40    │
│  (full screen height LazyColumn)                    │
│  (☑ = linked, ☐ = not linked)                      │
│  (tapping row toggles checkbox instantly)           │
└─────────────────────────────────────────────────────┘
```

**ItemLinkingViewModel.kt changes:**

Add these to the ViewModel:
```kotlin
// Inject DeviceLockRepository and TeamLinkRepository
// Load linked outlets for this device on init
private val _linkedOutlets = MutableStateFlow<List<TeamLink>>(emptyList())
val linkedOutlets: StateFlow<List<TeamLink>> = _linkedOutlets.asStateFlow()

private val _selectedOutletCode = MutableStateFlow("")
private val _selectedOutletName = MutableStateFlow("")
val selectedOutletCode: StateFlow<String> = _selectedOutletCode.asStateFlow()
val selectedOutletName: StateFlow<String> = _selectedOutletName.asStateFlow()

// Load linked outlets on init
init {
    viewModelScope.launch(Dispatchers.IO) {
        val lock = deviceLockRepository.get()
        if (lock != null) {
            _linkedOutlets.value = teamLinkRepository.getAllForMerchandiser(lock.merchandiserName)
        }
    }
}

// When outlet selected from dropdown:
fun selectOutlet(outletCode: String, outletName: String) {
    _selectedOutletCode.value = outletCode
    _selectedOutletName.value = outletName
    // Load existing links for this outlet
    loadLinkedBarcodes(outletCode)
    // Trigger item list to reload
}
```

**Menu access rule (SettingsDrawerContent.kt):**
```
Item Linking button is enabled when:
  isTeamLinkingComplete = true
  (outlet selection happens INSIDE the screen, not required before opening)

Remove the hasOutlet check entirely.
Change enabled condition to: enabled = isTeamLinkingComplete

Button label when team linking complete:
  "Item Linking — tap to manage"
Button label when team linking NOT complete:
  "Item Linking — complete Team Linking first" (greyed out)
```

**Effect on New Entry screen (already implemented, verify it works):**
```
When OutletItemLink records exist for current outlet:
  → Description search shows ONLY linked items
  → Barcode scan of unlinked item → warning dialog "Add anyway?"

When NO OutletItemLink records for current outlet:
  → Normal: all 1,197 items searchable (no restriction)
```

---

## CORRECTION 2 — REPORTS PAGE SHOWS ALL OUTLETS (NOT JUST LINKED)

### Problem
The "Search Outlet" field in Reports shows all 282 outlets.
After Team Linking, only linked outlets should be visible anywhere in the app.

### Fix — ReportsViewModel.kt

**Change the outlet search in ReportsViewModel:**

```kotlin
// Current (WRONG): searches ALL outlets from outlets table
// Fix: search only from TeamLink records for locked merchandiser

// Add these injections to ReportsViewModel:
// private val deviceLockRepository: DeviceLockRepository
// private val teamLinkRepository: TeamLinkRepository

// Add this state:
private val _linkedOutlets = MutableStateFlow<List<TeamLink>>(emptyList())

// Load on init:
init {
    viewModelScope.launch(Dispatchers.IO) {
        val lock = deviceLockRepository.get()
        if (lock != null) {
            _linkedOutlets.value = teamLinkRepository
                .getAllForMerchandiser(lock.merchandiserName)
        }
    }
    loadPastExports()
}

// Replace the outlet search function:
fun setOutletQuery(query: String) {
    _outletQuery.value = query
    viewModelScope.launch(Dispatchers.IO) {
        val links = _linkedOutlets.value
        _outletResults.value = if (query.isBlank()) {
            links.map { Outlet(outletCode = it.outletCode,
                               outletName = it.outletName, shortName = "") }
        } else {
            links
                .filter { it.outletName.contains(query, ignoreCase = true) }
                .map { Outlet(outletCode = it.outletCode,
                              outletName = it.outletName, shortName = "") }
        }
    }
}
```

**Also apply the same fix to HistoryViewModel.kt:**
History page also has an outlet selector — it must also show only linked outlets.
Apply the same pattern: inject DeviceLockRepository + TeamLinkRepository,
load linked outlets on init, filter search within linked outlets only.

---

## CORRECTION 3 — REMOVE "ARCHIVE THIS MONTH" BUTTON FROM HISTORY

### Problem
The "Archive This Month" button in History page can be accidentally tapped,
causing confusion. The monthly archive is already handled automatically
by WorkManager on the 1st of every month.

### Fix — HistoryScreen.kt

**Simply remove the Archive This Month button entirely.**

Find this button in HistoryScreen.kt:
```kotlin
OutlinedButton(
    onClick  = viewModel::requestArchive,
    ...
) {
    Text("Archive This Month", ...)
}
```

Delete it completely. Also delete:
- `showArchiveDialog` state collection
- `showArchiveDialog` AlertDialog composable
- The `if (showArchiveDialog)` block

**Keep the Export History button** — that is still needed.

**In HistoryViewModel.kt:**
- Remove `requestArchive()`, `confirmArchive()`, `dismissArchiveDialog()` functions
- Remove `_showArchiveDialog` state
- Remove `isArchiving` state
- Keep everything else

---

## CORRECTION 4 — "ALL OUTLETS" TEXT REPORT NOT WORKING CORRECTLY

### Problem
When user taps "All Outlets" in Share Text Report, it only shows the
currently selected outlet's data — not all linked outlets.

### Root Cause
The current implementation fetches entries filtered by the selected outlet.
It needs to fetch entries for ALL linked outlets under this merchandiser.

### Fix — ReportsViewModel.kt

**Replace the All Outlets text report logic:**

```kotlin
fun shareTextAllOutlets() {
    _showTextScopeDialog.value = false
    viewModelScope.launch(Dispatchers.IO + handler) {

        // Step 1: get locked merchandiser
        val lock = deviceLockRepository.get() ?: run {
            _snackMessage.tryEmit("Merchandiser not set. Complete Team Linking first.")
            return@launch
        }

        // Step 2: get ALL linked outlets for this merchandiser
        val links = teamLinkRepository.getAllForMerchandiser(lock.merchandiserName)
        if (links.isEmpty()) {
            _snackMessage.tryEmit("No outlets linked. Complete Team Linking first.")
            return@launch
        }

        // Step 3: get current month prefix
        val monthPrefix = java.time.LocalDate.now()
            .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"))

        // Step 4: fetch entries for EACH linked outlet
        val sections = mutableListOf<Pair<String, List<ExpiryEntry>>>()
        links.forEach { link ->
            val entries = entryRepository.getEntriesForExport(
                outletCode   = link.outletCode,
                merchandiser = lock.merchandiserName,
                salesman     = link.salesmanName,
                monthPrefix  = monthPrefix,
            )
            if (entries.isNotEmpty()) {
                sections.add(Pair(link.outletName, entries))
            }
        }

        if (sections.isEmpty()) {
            _snackMessage.tryEmit("No entries found for any linked outlet this month.")
            return@launch
        }

        // Step 5: build text report
        val text = buildTextReport(sections)
        _shareText.tryEmit(text)
    }
}
```

**The existing buildTextReport() function already handles multiple sections correctly.**
Just make sure shareTextAllOutlets() passes ALL outlets, not just the selected one.

---

## CORRECTION 5 — EXCEL REPORT SHOULD INCLUDE ALL LINKED OUTLETS

### Problem
The "Share Excel" button only exports the currently selected outlet.
It should export ALL linked outlets, each in its own sheet, in one file.

### Fix — ReportsViewModel.kt

**Replace the exportExcel() function:**

```kotlin
fun exportExcel() {
    _isExportingExcel.value = true
    viewModelScope.launch(Dispatchers.IO + handler) {
        try {
            // Step 1: get locked merchandiser
            val lock = deviceLockRepository.get() ?: run {
                _snackMessage.tryEmit("Merchandiser not set. Complete Team Linking first.")
                _isExportingExcel.value = false
                return@launch
            }

            // Step 2: get ALL linked outlets for this merchandiser
            val links = teamLinkRepository.getAllForMerchandiser(lock.merchandiserName)
            if (links.isEmpty()) {
                _snackMessage.tryEmit("No outlets linked.")
                _isExportingExcel.value = false
                return@launch
            }

            // Step 3: get current month prefix
            val monthPrefix = java.time.LocalDate.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"))

            // Step 4: build list of (outlet, entries) for all linked outlets
            val outletEntries = mutableListOf<Pair<
                com.armada.expiryapp.data.db.entity.Outlet, List<ExpiryEntry>>>()

            links.forEach { link ->
                val entries = entryRepository.getEntriesForExport(
                    outletCode   = link.outletCode,
                    merchandiser = lock.merchandiserName,
                    salesman     = link.salesmanName,
                    monthPrefix  = monthPrefix,
                )
                val outlet = com.armada.expiryapp.data.db.entity.Outlet(
                    outletCode = link.outletCode,
                    outletName = link.outletName,
                    shortName  = link.outletName.take(31),
                )
                outletEntries.add(Pair(outlet, entries))
            }

            // Step 5: build multi-sheet Excel
            // One call to ExcelExporter per outlet — it handles cumulative logic
            // The file name is based on merchandiser + salesman + month
            // Since multiple salesmen may exist, use merchandiser name only in filename
            val file = excelExporter.buildMultiOutletFile(
                outletEntries = outletEntries,
                merchandiser  = lock.merchandiserName,
                context       = context,
            )

            AutoBackup(context).backup()
            loadPastExports()
            _shareFile.tryEmit(file)

        } catch (oom: OutOfMemoryError) {
            _snackMessage.tryEmit("Not enough memory. Close other apps and try again.")
        } catch (e: Exception) {
            _snackMessage.tryEmit("Export failed: ${e.localizedMessage ?: "error"}")
        } finally {
            _isExportingExcel.value = false
        }
    }
}
```

**ExcelExporter.kt — add buildMultiOutletFile() function:**

```kotlin
@Throws(Exception::class, OutOfMemoryError::class)
fun buildMultiOutletFile(
    outletEntries: List<Pair<Outlet, List<ExpiryEntry>>>,
    merchandiser:  String,
    context:       Context,
): File {
    val monthYear = LocalDate.now()
        .format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault()))
    val sanitize = { s: String -> s.replace(Regex("[\\\\/:*?\"<>|]"), "").trim() }
    val fileName = "${sanitize(merchandiser)} – $monthYear – Expiry Report.xlsx"
    val docsDir  = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        ?: throw IllegalStateException("External Documents folder unavailable")
    docsDir.mkdirs()
    val file = File(docsDir, fileName)

    var workbook: XSSFWorkbook? = null
    try {
        // Open existing file if it exists, else create new
        workbook = if (file.exists()) {
            runCatching {
                file.inputStream().use { XSSFWorkbook(it) }
            }.getOrElse { XSSFWorkbook() }
        } else {
            XSSFWorkbook()
        }

        // Build/rebuild one sheet per outlet
        outletEntries.forEach { (outlet, entries) ->
            val sheetName = outlet.shortName.take(31).ifBlank {
                outlet.outletName.take(31)
            }
            // Remove existing sheet if present
            val existingIdx = workbook.getSheetIndex(sheetName)
            if (existingIdx >= 0) workbook.removeSheetAt(existingIdx)

            // Create fresh sheet
            val sheet: XSSFSheet = workbook.createSheet(sheetName)

            // Get salesman for this outlet from entries (or blank)
            val salesman = entries.firstOrNull()?.salesman ?: ""

            // Header rows
            sheet.createRow(0).apply { cell(this, 0, "SALESMAN :"); cell(this, 1, salesman) }
            sheet.createRow(1).apply { cell(this, 0, "MERCHANDISER :"); cell(this, 1, merchandiser) }
            sheet.createRow(2).apply { cell(this, 0, "OUTLET NAME :"); cell(this, 1, outlet.outletName) }
            sheet.createRow(3).apply { cell(this, 0, "CODE :"); cell(this, 1, outlet.outletCode) }
            sheet.createRow(4) // blank
            sheet.createRow(5).apply {
                cell(this, 0, "CODE")
                cell(this, 1, "DESCRIPTION")
                cell(this, 2, "EXPIRY DATE")
                cell(this, 3, "QTY")
            }

            // Data rows sorted ascending by expiry date
            entries.sortedBy { it.expiryDate }.forEachIndexed { i, e ->
                val rowNum = i + 6
                sheet.createRow(rowNum).apply {
                    cell(this, 0, e.productCode ?: "")
                    cell(this, 1, e.description)
                    cell(this, 2, e.expiryDate.toDisplayDate())
                    cell(this, 3, "${e.quantity} ${e.unit}")
                }
            }

            // Column widths
            sheet.setColumnWidth(0, 3840)
            sheet.setColumnWidth(1, 11520)
            sheet.setColumnWidth(2, 3584)
            sheet.setColumnWidth(3, 3072)
        }

        // Save file
        file.outputStream().use { workbook.write(it) }
        Log.i(TAG, "Multi-outlet Excel saved: ${file.name}")
        return file

    } finally {
        workbook?.close()
    }
}

// Helper to create a cell with a string value
private fun cell(row: org.apache.poi.xssf.usermodel.XSSFRow, col: Int, value: String) {
    row.createCell(col).setCellValue(value)
}
```

**Note on filename for Correction 5:**
Since the Excel now covers ALL outlets for ONE merchandiser (multiple salesmen
may be involved), the filename format changes to:
```
[Merchandiser Name] – [Month Year] – Expiry Report.xlsx
```
Example: `Akhil Sunny – June 2026 – Expiry Report.xlsx`

---

## SUMMARY OF FILES TO CHANGE

```
Correction 1: ItemLinkingViewModel.kt + ItemLinkingScreen.kt
              + SettingsDrawerContent.kt (remove hasOutlet check)

Correction 2: ReportsViewModel.kt + HistoryViewModel.kt
              (filter outlet search to linked outlets only)

Correction 3: HistoryScreen.kt + HistoryViewModel.kt
              (remove Archive This Month button and related code)

Correction 4: ReportsViewModel.kt
              (fix shareTextAllOutlets to fetch ALL linked outlets)

Correction 5: ReportsViewModel.kt + ExcelExporter.kt
              (export ALL linked outlets in one multi-sheet file)
```

---

## COMPILE CHECKPOINTS

After Correction 1: compile → zero errors ✅
After Correction 2: compile → zero errors ✅
After Correction 3: compile → zero errors ✅
After Corrections 4 + 5 together: compile → zero errors ✅

Build debug APK after all corrections complete.
Report APK path and file size.
Then wait for instruction.
