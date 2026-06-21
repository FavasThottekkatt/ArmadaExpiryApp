# ARMADA EXPIRY APP — CORRECTIONS & IMPROVEMENTS
## Based on UAT Testing — Samsung S23 Ultra
## Implement ALL items in order. Compile after every change. Zero errors before next item.

---

## HOW TO USE THIS FILE

This file contains corrections and improvements identified during real device testing.
Implement each numbered item one at a time.
After completing ALL items in a group, compile and confirm zero errors before the next group.
Report completion after each group.

---

## GROUP 1 — REMOVE UNNECESSARY SCREEN ELEMENTS
### Target file: DashboardScreen.kt, NewEntryScreen.kt, MainActivity.kt

**1.1 — Dashboard screen: remove 4 elements**
- Remove the "OUTLET DETAILS" section heading/title
- Remove the "Expiry Dashboard" section heading/title
- Remove the "Quick SKU / Item Search" OutlinedTextField
- Remove the MasterDataInfoStrip composable and its call
- Keep everything else — stat cards, record cards, outlet selector, Next button

**1.2 — Top app bar: update text**
- Change "Armada Distribution" subtitle + "EXPIRY TRACKING" title to:
  Single line: "Armada Distribution AUH & AL AIN"
  Style: bold, 20sp minimum, white, highly visible
  Remove the two-line layout — use one bold prominent line instead

**1.3 — New Entry screen: remove step flow indicator**
- Remove the StepFlowIndicator composable entirely (the row showing ①ITEM→②DATE→③QTY→④DONE)
- Remove all step flow related state and logic from NewEntryViewModel

**1.4 — New Entry screen: remove FAB (floating action button)**
- Remove the FloatingActionButton composable entirely from NewEntryScreen
- The camera scan button inside the barcode field row remains

**1.5 — New Entry screen: remove NEW ENTRY button**
- Remove the NewEntryButton composable and its call
- Remove the RepeatThisItemButton (Bulk Mode button) — move Bulk Mode
  activation to a different trigger (see Item 2.5 below)
- Auto-save logic: when all 3 fields are complete (item filled + date valid + qty > 0)
  → save to Room AUTOMATICALLY with no button tap needed
  → clear fields automatically
  → ready for next item immediately
  This replaces the manual save button entirely.

---

## GROUP 2 — LAYOUT IMPROVEMENTS
### Target file: NewEntryScreen.kt, DashboardScreen.kt

**2.1 — Second page outlet banner: bold name, remove code**
- Outlet banner at top of New Entry screen:
  Show ONLY the outlet name — remove "| Code: [code]" from the banner
  Make outlet name: bold, 18sp minimum, white text, clearly readable
  Example: "CARREFOUR KHALIDIYAH MALL" — large and bold

**2.2 — Barcode and item code side by side**
- Currently: barcode field above, product code field below (separate rows)
- Change to: barcode field and product code field SIDE BY SIDE in one row
  Barcode field: weight 2 (wider)
  Code field: weight 1 (narrower)
  Both in the same Row composable

**2.3 — Numpad fixed at very bottom of screen**
- The numpad panel must be anchored to the VERY BOTTOM of the screen
  using Modifier.align(Alignment.Bottom) inside a Box
- The scrollable content (fields + table) occupies all space ABOVE the numpad
- Numpad never scrolls, never moves, always visible at screen bottom
- This gives maximum space to the item table

**2.4 — Outlet name on second page: bold and bigger**
- Already covered in 2.1 above

**2.5 — Bulk Mode: move trigger**
- Since the NEW ENTRY button is removed, Bulk Mode activates differently:
  After auto-save: show a small "🔁 Repeat" chip/badge near the top of the table
  Tapping it activates Bulk Mode (pre-fills last item, focuses date)
  Bulk Mode banner remains as designed

---

## GROUP 3 — NUMPAD REDESIGN
### Target file: NewEntryScreen.kt

**3.1 — Restructure numpad into 2 rows:**

Row 1 (digits only — BIGGER):
```
[ 0 ][ 1 ][ 2 ][ 3 ][ 4 ][ 5 ][ 6 ][ 7 ][ 8 ][ 9 ]
```
- 10 digit keys across full width
- Font size: 24sp bold (increased from 20sp)
- Key height: 52dp (increased from 44dp)
- More visual weight — dominant row

Row 2 (units + C key):
```
[     PC     ][    OUT    ][    CTN    ][      C      ]
```
- 4 buttons across full width
- C key: weight 1.5 (wider than PC/OUT/CTN which are weight 1 each)
- C key: numpad.clear red background, white text, 18sp bold
- C key label: "C" — large and obvious
- PC/OUT/CTN: same style as before, PC pre-selected (blue when active)
- Key height: 48dp

**3.2 — C key behaviour (unchanged from spec):**
- Single tap: delete last digit in focused field
- Long press: clear entire focused field

**3.3 — Unit row activation:**
- PC/OUT/CTN dimmed (opacity 0.4) unless QTY field focused
- C key always active when any numpad field is focused

---

## GROUP 4 — REPORT IMPROVEMENTS

**4.1 — WhatsApp expiry text report: add date/time**
Target file: ReportsViewModel.kt
- At the very top of the text report, add current date and time:
```
Expiry Report
20 June 2026  10:30 AM

CARREFOUR KHALIDIYAH

AMICELLI HAZELNUT - 12/05/26 - 12 PC
...
```
Format: dd MMMM yyyy  hh:mm a (e.g. "20 June 2026  10:30 AM")
Place it on line 2, one blank line before the first outlet name

**4.2 — WhatsApp stock report: add date/time**
Target file: StockViewModel.kt
- Add current date and time below the outlet name:
```
CARREFOUR KHALIDIYAH
20 June 2026  10:30 AM

LOW STOCK
...
```
Format: same as 4.1

**4.3 — Monthly auto-archive on 1st of every month**
- Add a new WorkManager PeriodicWorkRequest:
  Fires at midnight (00:00) on the 1st of every month
  Action: for ALL expiry entries where archived = false:
    → set archived = true (move to History)
  After archiving: the New Entry table appears empty — fresh month
  All archived data remains viewable in History tab
  Schedule with BOOT_COMPLETED receiver (same pattern as reminder notification)
  WorkManager job name: "monthly_archive_job"

**4.4 — Excel report fix**
- Excel export is not generating correctly on device
- Review ExcelExporter.kt for any file path or POI issues
- Test with getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
- Ensure FileProvider URI is correctly generated
- Add detailed error logging to identify the exact failure point
- Fix and confirm Excel export works end-to-end

---

## GROUP 5 — FUNCTIONAL FIXES

**5.1 — Add DELETE to long-press edit popup**
Target file: NewEntryScreen.kt, NewEntryViewModel.kt
- Long-press on a table row currently shows EditEntryDialog
- Add a "Delete" button/option INSIDE the EditEntryDialog
- Delete button: red text, at the bottom of the dialog
- On tap: confirmation "Delete this entry?" → Yes/No
- Yes: delete from Room, dismiss dialog, table updates
- No: return to edit dialog

**5.2 — Remove CSV options from Settings menu**
Target file: SettingsDrawerContent.kt
- Remove any direct CSV file links or file browser options
- Keep ONLY:
  1. Re-import Master Data (button that goes to CsvValidation screen)
  2. Backups section
  3. Crash Log section
  4. About section
  (Item Linking option will be added in Phase 22)

**5.3 — Manual entries never saved to CSV**
This is already correct by design — manual entries go to Room only, never to CSV.
Add a code comment in CsvParser.kt and ItemRepository.kt confirming this:
// NOTE: Manual barcode/description entries are NEVER written back to CSV source files.
// They exist only in Room database for the current month's reporting period.

**5.4 — OCR: prefer expiry date over production date**
Target file: OcrDateParser.kt
- When multiple dates are found in an OCR frame, use label proximity to choose:

EXPIRY label keywords (prefer this date):
  EXP, EXPIRY, EXPIRY DATE, BEST BEFORE, BEST BY, BB, BBD,
  USE BY, USE BEFORE, CONSUME BEFORE, SELL BY, VALID UNTIL,
  VALID THRU, VALID TILL, BEST BEFORE END, EXP DATE, EXP:

PRODUCTION label keywords (ignore/deprioritize):
  MFD, MFG, MFGD, MANUFACTURED, MANUFACTURE DATE,
  DATE OF MFG, DATE OF MANUFACTURE, PROD, PRODUCTION,
  DOM, DATE OF MANUFACTURE, PKD, PACKED, PACKING DATE

Logic:
1. Extract ALL dates from the frame
2. For each date, look within 50 characters before/after for label keywords
3. If a date has an EXPIRY label → HIGH confidence, prefer it
4. If a date has a PRODUCTION label → mark it as production, skip it
5. If a date has NO label → treat as MEDIUM confidence
6. If ONLY production-labeled dates found → show all and let user pick
7. This solves the EXP/MFD printed together problem on FMCG products

---

## GROUP 6 — NEW FEATURES (implement as new phases)

### PHASE 22 — TEAM LINKING
**Purpose:** Lock one merchandiser to device. Link each outlet to its salesman.
After linking, only linked outlets appear. Salesman auto-fills.

**New Room entities needed:**

```kotlin
@Entity(tableName = "device_lock")
data class DeviceLock(
  @PrimaryKey val id: Int = 1,   // always 1 — only one record
  val merchandiserName: String,
  val lockedAt: String           // ISO timestamp
)

@Entity(
  tableName = "team_links",
  indices = [Index(value = ["outletCode"], unique = true)]
)
data class TeamLink(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val merchandiserName: String,
  val salesmanName: String,
  val outletCode: String,
  val outletName: String
)
```

**Team Linking screen flow:**

STEP 1 — Merchandiser selection (first time only):
- Show list of all 23 merchandiser names
- User taps their name
- Confirmation: "You are selecting [Name] as the merchandiser for this device.
  This cannot be changed without clearing app data. Confirm?"
- On confirm: saved to DeviceLock table permanently

STEP 2 — Outlet-Salesman linking:
- Shows list of ALL 282 outlets (searchable)
- For each outlet: show outlet name + a salesman picker dropdown
- User selects which salesman is assigned to each outlet they visit
- They can link as many outlets as needed
- "Link Completed" button at bottom — confirms all links are saved

STEP 3 — After linking completed:
- Dashboard outlet dropdown shows ONLY linked outlets
- When outlet selected → salesman auto-fills from TeamLink table
- Merchandiser always auto-fills from DeviceLock
- No manual merchandiser/salesman fields on Dashboard

**IMPORTANT RULES:**
- One merchandiser per device ONLY — DeviceLock enforced
- If DeviceLock exists: skip STEP 1, go to STEP 2 directly (for editing)
- If no TeamLinks exist after lock: cannot proceed to Dashboard
- Compulsory: must complete linking before accessing any other screen
- Team Linking accessible from hamburger menu for editing later
- Editing: can add/remove outlet-salesman links but CANNOT change merchandiser

**Navigation change:**
- After login → check DeviceLock → if none: go to Team Linking
- After Team Linking completed → go to Dashboard
- Dashboard outlet dropdown: shows only linked outlets
- On outlet select: auto-fill merchandiser (from DeviceLock) + salesman (from TeamLink)
- No merchandiser/salesman visible on Dashboard screen itself

---

### PHASE 23 — ITEM LINKING PER OUTLET
**Purpose:** Optional. Link specific SKUs to specific outlets.
When links exist, only those items appear in barcode/description search.

**New Room entity:**

```kotlin
@Entity(
  tableName = "outlet_item_links",
  indices = [
    Index(value = ["outletCode"]),
    Index(value = ["barcode"])
  ]
)
data class OutletItemLink(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val outletCode: String,
  val barcode: String,
  val description: String,
  val productCode: String?
)
```

**Item Linking screen:**
- Accessible ONLY from hamburger menu on New Entry screen (second page)
- Not accessible from Dashboard or other screens
- Shows outlet name at top (current selected outlet)
- Searchable list of ALL 1,197 items
- Checkbox on each row — tick to link to current outlet
- "Save Links" button — saves all checked items to OutletItemLink
- "Clear All Links" option for current outlet

**Effect on New Entry screen:**
- When OutletItemLink records exist for current outlet:
  → Description search searches ONLY linked items
  → Barcode scan: if scanned item not in linked list, show warning:
    "This item is not linked to this outlet. Add it anyway?"
    Yes → allow entry. No → cancel.
- When NO OutletItemLink records for current outlet:
  → Normal behaviour: search all 1,197 items (no restriction)
- Optional feature — outlets without links work exactly as before

**Menu placement:**
- Hamburger menu on New Entry screen only
- Label: "Item Linking for [Outlet Name]"
- Greyed out if no outlet selected

---

## COMPILE CHECKPOINTS

After Group 1: compile → zero errors ✅
After Group 2: compile → zero errors ✅
After Group 3: compile → zero errors ✅
After Group 4: compile → zero errors ✅
After Group 5: compile → zero errors ✅
After Phase 22: compile → zero errors ✅
After Phase 23: compile → zero errors ✅

Then rebuild debug APK and install on Samsung S23 Ultra for re-testing.

---

## FINAL NOTE

Do not start Phase 22 or Phase 23 until Groups 1-5 are all complete
and a new debug APK has been tested on the Samsung S23 Ultra and confirmed working.

Report after each group completion. Do not proceed without instruction.
