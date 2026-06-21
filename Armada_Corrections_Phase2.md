# ARMADA EXPIRY APP — CORRECTIONS PHASE 2
## 6 Modifications — Implement in order, compile after each group
## Read every word carefully. Do not skip any detail.

---

## CRITICAL RULE BEFORE STARTING

Read this entire document fully before writing a single line of code.
These 6 modifications affect core navigation, data flow, and UI layout.
Implement them in the numbered order below.
Compile after each modification group. Zero errors before moving to next.

---

## MODIFICATION 1 — TEAM LINKING (MOST CRITICAL — DO THIS FIRST)

### Overview
Team Linking permanently associates one merchandiser + their salesmen + their
outlets on a single device. It is COMPULSORY on first use. Without completing
Team Linking, the user CANNOT reach the New Entry screen or any other screen.

---

### 1A — NEW ROOM ENTITIES (add to AppDatabase, bump version to 4)

```kotlin
// Locks ONE merchandiser to this device permanently
@Entity(tableName = "device_lock")
data class DeviceLock(
    @PrimaryKey val id: Int = 1,        // Always 1 — only one record ever
    val merchandiserName: String,
    val lockedAt: String                // ISO timestamp
)

// Each row = one outlet linked to merchandiser + salesman
@Entity(
    tableName = "team_links",
    indices = [Index(value = ["outletCode"])]
)
data class TeamLink(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val merchandiserName: String,       // Always same as DeviceLock.merchandiserName
    val salesmanName: String,
    val outletCode: String,
    val outletName: String
)
```

Create DAOs: DeviceLockDao, TeamLinkDao.
Create Repositories: DeviceLockRepository, TeamLinkRepository.
Add MIGRATION_3_4 to DatabaseModule (creates device_lock + team_links tables).
Add both entities to AppDatabase version 4.

---

### 1B — NAVIGATION FLOW CHANGE

**After login → app checks DeviceLock table:**

```
DeviceLock does NOT exist (first ever use after install):
    → Navigate to Dashboard screen
    → Dashboard screen shows EMPTY with a prominent message:
      "Please complete Team Linking before continuing.
       Tap the menu (≡) and choose 'Team Linking'."
    → Next → button is HIDDEN (not just disabled — completely gone)
    → Outlet Name field is HIDDEN
    → Only the message is shown

DeviceLock EXISTS and TeamLinks count > 0:
    → Navigate to Dashboard screen
    → Dashboard shows ONLY the Outlet Name dropdown
    → Outlet Code auto-fills when outlet selected
    → Next → button visible and active when outlet selected
    → Merchandiser Name field: HIDDEN (auto-fills from DeviceLock)
    → Salesman Name field: HIDDEN (auto-fills from TeamLink for selected outlet)
```

**When user selects an outlet on Dashboard (after linking complete):**
```
→ outletCode and outletName fill from the selected outlet
→ merchandiserName auto-loads from DeviceLock.merchandiserName
→ salesmanName auto-loads from TeamLink WHERE outletCode = selected outletCode
→ All 4 values passed via SessionHolder to New Entry screen
→ All 4 values appear in Excel report headers
→ No manual input needed for merchandiser or salesman — ever again
```

---

### 1C — DASHBOARD SCREEN CHANGES

**DashboardViewModel changes:**
```kotlin
// Add these new flows:
val isTeamLinkingComplete: StateFlow<Boolean>
    // true when DeviceLock exists AND TeamLink count > 0

val linkedOutlets: StateFlow<List<Outlet>>
    // ONLY outlets from TeamLink for the locked merchandiser
    // When user searches outlet dropdown → search within THIS list only
    // NOT from the full outlets table

val lockedMerchandiser: StateFlow<String>
    // From DeviceLock.merchandiserName

// Remove these entirely from ViewModel:
// - merchandiser field/state
// - salesman field/state
// - MERCHANDISERS seed list
// - SALESMEN seed list

// Keep:
// - outletName field/state (searchable dropdown, filtered to linked outlets)
// - outletCode field/state (auto-fills from TeamLink)
// - salesman auto-fills from TeamLink when outlet selected — stored in SessionHolder
```

**DashboardScreen changes:**
```
When isTeamLinkingComplete = false:
    Show ONLY this message centered on screen:
    ┌─────────────────────────────────────────────┐
    │                                             │
    │   Team Linking not yet completed.           │
    │                                             │
    │   Tap ≡ menu → Team Linking                 │
    │   to set up your outlets before continuing. │
    │                                             │
    └─────────────────────────────────────────────┘
    No outlet field. No Next button. Nothing else.

When isTeamLinkingComplete = true:
    Show ONLY:
    - Outlet Name searchable dropdown (filtered to linked outlets only)
    - Outlet Code (auto-filled, read-only)
    - Next → button (active when outlet selected)
    - Stat cards and records list below (as before)
    NO merchandiser field. NO salesman field. Ever.
```

---

### 1D — TEAM LINKING SCREEN (new screen, new file)

**File:** `ui/screens/teamlinking/TeamLinkingScreen.kt`
**ViewModel:** `ui/screens/teamlinking/TeamLinkingViewModel.kt`
**Route:** `Screen.TeamLinking : Screen("team_linking")`

**TeamLinkingScreen layout (top to bottom):**

```
┌─────────────────────────────────────────────────────┐
│  ← Back    TEAM LINKING                             │  ← top bar
├─────────────────────────────────────────────────────┤
│                                                     │
│  [IF DeviceLock NOT set — show merchandiser field]  │
│  Merchandiser Name *                                │
│  [ Searchable dropdown — 23 names + free text  ▼ ] │
│                                                     │
│  [IF DeviceLock IS set — show locked name, no edit] │
│  Merchandiser: AKHIL SUNNY THARAYIL  🔒             │
│  (grey background, non-editable, lock icon)         │
│                                                     │
│  Salesman Name *                                    │
│  [ Searchable dropdown — 7 names + free text   ▼ ] │
│                                                     │
│  Outlet Name *                                      │
│  [ Searchable dropdown — all 282 outlets       ▼ ] │
│                                                     │
│  Outlet Code (auto-fills when outlet selected)      │
│  [ Read-only text field                           ] │
│                                                     │
│  [ ✅  LINK  ]  ← full-width green button          │
│  (disabled until all 3 mandatory fields filled)     │
│                                                     │
├─────────────────────────────────────────────────────┤
│  LINKED OUTLETS (table of saved links)              │
│                                                     │
│  OUTLET NAME          │ SALESMAN       │  🗑        │
│  CARREFOUR KHALIDIYAH │ Rajesh         │  🗑        │
│  LULU MUSHRIF         │ Muneer         │  🗑        │
│  SPINNEYS KHALIDIYAH  │ Sreejith       │  🗑        │
│  (scrollable, word-wrap on outlet name)             │
│                                                     │
├─────────────────────────────────────────────────────┤
│  [      DONE — Linking Complete      ]              │
│  (full-width, brand.accent blue button)             │
│  (disabled if no links saved yet)                   │
└─────────────────────────────────────────────────────┘
```

**LINK button behaviour:**
1. Validates: Merchandiser + Salesman + Outlet all selected
2. If DeviceLock does NOT exist yet:
   → Save DeviceLock(merchandiserName = selectedMerchandiser)
   → Merchandiser field becomes locked (grey, non-editable, lock icon)
3. Check if this outletCode already exists in TeamLink table:
   → If YES: show Snackbar "This outlet is already linked. Delete it first to relink."
   → If NO: save new TeamLink record
4. Clear Salesman + Outlet fields (keep merchandiser locked)
5. Refresh the linked outlets table
6. Focus on Salesman field ready for next link

**Delete (🗑) button in linked table:**
- Tap → confirmation: "Remove [Outlet Name] from your linked outlets?"
- Confirm → delete TeamLink record → table refreshes

**DONE button behaviour:**
1. Disabled if TeamLink count = 0
2. On tap → AlertDialog:
   ```
   Title: "Confirm Team Linking"
   Message: "You have linked [N] outlets.
             Once confirmed, [Merchandiser Name] will be
             permanently set as the merchandiser for this device.
             Are you sure you want to continue?"
   Buttons: [OK] [Cancel]
   ```
3. On OK:
   → DeviceLock confirmed (already saved when LINK was first tapped)
   → Navigate back to Dashboard
   → Dashboard now shows outlet-only mode
4. On Cancel: stay on Team Linking screen

**Back button behaviour:**
- If TeamLink count = 0 AND DeviceLock not set: allow back (user hasn't started yet)
- If TeamLink count > 0 OR DeviceLock is set: show warning:
  "Team Linking is not complete. You cannot use the app without completing this.
   Do you want to continue linking?"
  [Continue Linking] [Exit App] — no "go back" option

---

### 1E — HAMBURGER MENU CHANGES

Add "Team Linking" as the FIRST item in the hamburger menu.

**When Team Linking is complete:**
- Menu item shows: "Team Linking ✓ (edit)"
- Tapping it opens TeamLinkingScreen in EDIT mode
- In edit mode: merchandiser field is LOCKED (grey, non-editable)
- User can ADD new outlet-salesman links
- User can DELETE existing links
- DONE button saves and returns to Dashboard
- User CANNOT change the merchandiser name

**When Team Linking is NOT complete:**
- Menu item shows: "Team Linking ⚠️ (required)"
- Tapping opens TeamLinkingScreen in setup mode

---

### 1F — OUTLET LINKING ENFORCEMENT

After Team Linking is complete:
- The full 282-outlet dropdown is REPLACED by a filtered dropdown
- Filtered dropdown shows ONLY outlets from TeamLink table
  WHERE merchandiserName = DeviceLock.merchandiserName
- If user types in outlet search → searches within filtered list only
- When outlet selected → salesman auto-fills from TeamLink record
- Both merchandiser and salesman are passed silently to SessionHolder

---

## MODIFICATION 2 — ITEM LINKING (OPTIONAL)

### Overview
Optional feature. User can link specific SKUs to a specific outlet.
When links exist for an outlet, only those SKUs appear in the item search.
No links = normal behaviour (all 1,197 items searchable).

---

### 2A — NEW ROOM ENTITY (add to AppDatabase version 4 migration)

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

Create OutletItemLinkDao, OutletItemLinkRepository.
Add to AppDatabase version 4 migration.

---

### 2B — ITEM LINKING SCREEN

**File:** `ui/screens/itemlinking/ItemLinkingScreen.kt`
**ViewModel:** `ui/screens/itemlinking/ItemLinkingViewModel.kt`
**Route:** `Screen.ItemLinking : Screen("item_linking")`

**Access:** Hamburger menu on Dashboard screen ONLY.
Available ONLY after Team Linking is complete.
If Team Linking not complete → menu item greyed out with tooltip:
"Complete Team Linking first"

**Screen layout:**
```
┌─────────────────────────────────────────────────────┐
│  ← Back    ITEM LINKING                             │
│            [Current Outlet Name]                    │  ← subtitle
├─────────────────────────────────────────────────────┤
│  [ 🔍 Search items by name or barcode............ ] │
├─────────────────────────────────────────────────────┤
│  [Link All Shown]  [Clear All Links]                │  ← action buttons
├─────────────────────────────────────────────────────┤
│  ☑ │ AMICELLI HAZELNUT CREAM WAFER 12.5G  │ AL1004 │
│  ☐ │ EVIAN MINERAL WATER 500ML            │ EV500  │
│  ☑ │ KITKAT CHUNKY MILK CHOCOLATE 40G     │ KK40   │
│  (full screen height LazyColumn, word-wrap)         │
│  (☑ = linked, ☐ = not linked)                      │
│  (tapping row toggles checkbox)                     │
└─────────────────────────────────────────────────────┘
```

**Behaviour:**
- Opens with the CURRENTLY SELECTED outlet pre-loaded
- Shows all 1,197 items with checkboxes
- Items already linked to this outlet: pre-checked (☑)
- Tapping any row toggles its link status immediately (saves to Room instantly)
- Search filters the list in real time (150ms debounce)
- "Link All Shown" button: links all currently visible/filtered items
- "Clear All Links" button: removes all OutletItemLink records for this outlet
  → confirmation dialog first
- Back button: save state is already persisted (saves happen per-tap)

**Effect on New Entry screen:**
```
When OutletItemLink records exist for current outletCode:
  → Description search (Path B) searches ONLY linked items
  → Barcode scan: if scanned barcode NOT in linked items:
    Snackbar: "This item is not linked to [Outlet Name].
               Add it anyway?"
    [Yes, Add] → allow entry as normal
    [No]       → cancel, do not fill fields

When NO OutletItemLink records for current outletCode:
  → Normal behaviour: all 1,197 items searchable
  → No restriction on barcode scan
```

**Menu item label:**
```
If outlet selected and links exist:    "Item Linking ([N] items linked)"
If outlet selected and no links:       "Item Linking (no items linked)"
If no outlet selected:                 "Item Linking (select outlet first)" — greyed out
```

---

## MODIFICATION 3 — BOLD TEXT IN STOCK WHATSAPP REPORT

**File:** `ui/screens/stock/StockViewModel.kt`

In WhatsApp, bold text is created by wrapping with asterisks: `*text*`

**Current output:**
```
CARREFOUR KHALIDIYAH
20 June 2026  10:30 AM

LOW STOCK
AMICELLI HAZELNUT - 5 PC

OUT OF STOCK
RED BULL ENERGY DRINK 250ML
```

**Required output:**
```
*CARREFOUR KHALIDIYAH*
20 June 2026  10:30 AM

*LOW STOCK*
AMICELLI HAZELNUT - 5 PC

*OUT OF STOCK*
RED BULL ENERGY DRINK 250ML
```

**Exact change in buildStockTextReport() or equivalent:**
```kotlin
// Outlet name: wrap with *asterisks*
sb.append("*${sessionHolder.outletName}*\n")
sb.append(now).append("\n\n")

// LOW STOCK heading: wrap with *asterisks*
sb.append("*LOW STOCK*\n")

// OUT OF STOCK heading: wrap with *asterisks*
sb.append("*OUT OF STOCK*\n")
```

Only these 3 lines get bold formatting.
Item descriptions are NOT bold.
Date/time line is NOT bold.

---

## MODIFICATION 4 — REMOVE CSV OPTIONS FROM SETTINGS MENU

**File:** `ui/screens/settings/SettingsDrawerContent.kt`

Check the Settings drawer for any of these items and REMOVE them:
- Any button or link that says "ItemList.csv"
- Any button or link that says "Outlets.csv"
- Any file browser for CSV files
- Any direct CSV file path display

**Keep ONLY these items in the Settings drawer:**
1. Team Linking (moved to TOP of menu — see Modification 1E)
2. Item Linking (second item)
3. Re-import Master Data
4. Backups
5. Crash Log
6. About

If ItemList.csv or Outlets.csv file paths appear anywhere in the Settings UI
as tappable elements or displayed paths, remove them.
The Re-import Master Data button is sufficient — it handles CSV internally.

---

## MODIFICATION 5 — STOCK SCREEN: MOVE NUMPAD TO BOTTOM + ADD C BUTTON

**File:** `ui/screens/stock/StockScreen.kt`

### Current layout (WRONG):
```
Search bar (top)
Numpad 0-9 row
PC | OUT | CTN | SHARE row
Column headers
Item list (scrollable)
```

### Required layout (CORRECT):
```
Search bar (fixed at top)
Column headers (fixed below search)
Item list (scrollable — takes ALL remaining space)
─────────────────────────────────────── ← fixed bottom panel
Numpad: [0][1][2][3][4][5][6][7][8][9]
Units:  [  PC  ][  OUT  ][  CTN  ][ C ][ SHARE ]
```

### Implementation details:

**Overall screen structure:**
```kotlin
Box(modifier = Modifier.fillMaxSize()) {

    Column(modifier = Modifier.fillMaxSize()) {

        // Search bar — fixed at very top
        SearchBar(...)

        // Column headers — fixed below search
        StockColumnHeaders(...)

        // Item list — takes ALL remaining space above numpad
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = numpadHeightDp), // dynamic padding = numpad height
            ...
        ) { ... }
    }

    // Fixed numpad panel — anchored to very bottom
    StockNumpadPanel(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .onGloballyPositioned { numpadHeightPx = it.size.height }
    )
}
```

**StockNumpadPanel — two rows:**

Row 1 — digits (full width, 10 keys):
```
[ 0 ][ 1 ][ 2 ][ 3 ][ 4 ][ 5 ][ 6 ][ 7 ][ 8 ][ 9 ]
```
- Font: 22sp bold
- Height: 50dp
- Equal weight (weight(1f) each)
- Digits go to the currently focused QTY field in the list

Row 2 — units + C + SHARE:
```
[    PC    ][   OUT   ][   CTN   ][  C  ][  SHARE  ]
```
- PC, OUT, CTN: weight(1f) each
- C button: weight(0.8f), RED background (#C0392B), white text "C", 18sp bold
  → Single tap: deletes last digit in focused QTY field
  → Long press: clears entire focused QTY field
- SHARE button: weight(1.5f), brand.accent blue, white text "SHARE", 14sp bold
  → Generates and shares the WhatsApp text report

**C button in stock screen behaviour:**
- Targets the currently focused QTY field in the item list
- If no QTY field is focused: C key is dimmed (opacity 0.4), does nothing
- Single tap: remove last digit from focused row's qty string
- Long press (>500ms): clear entire qty for focused row

**StockViewModel changes:**
- Add `onStockClearSingle()` function: removes last character from focused row qty
- Add `onStockClearAll()` function: clears focused row qty entirely
- The focused row concept: track which row's QTY field the user last tapped

---

## MODIFICATION 6 — AUTO-SAVE: WAIT FOR PC/OUT/CTN BEFORE SAVING

**File:** `ui/screens/entry/NewEntryViewModel.kt`

### The Problem
Currently `startAutoSave()` triggers when:
- itemFilled = true
- expiryDateRaw.length == 8
- quantity.toIntOrNull() > 0

This fires the moment the user enters the FIRST digit of quantity.
Example: user types "2" (wanting to enter "24") → auto-save fires immediately
→ entry saved as "2 PC" → fields clear → user cannot type "24" or choose "OUT"

### The Fix — Unit selection triggers save

**Remove `startAutoSave()` entirely from the ViewModel.**

**New save trigger:** Save happens when user TAPS PC, OUT, or CTN — not before.

**New `setUnit()` function:**
```kotlin
fun setUnit(unit: String) {
    _unit.value = unit
    // Attempt save when unit is selected — this is the save trigger
    val raw = _expiryDateRaw.value
    val qty = _quantity.value.toIntOrNull() ?: 0
    if (_itemFilled.value && raw.length == 8 && isDateValid(raw) && qty > 0) {
        saveEntry()   // This calls the full validation pipeline
    } else {
        // Show which field is still missing
        when {
            !_itemFilled.value -> _snackMessage.tryEmit("Please select an item first.")
            raw.length < 8     -> _snackMessage.tryEmit("Please enter a complete expiry date.")
            qty <= 0           -> _snackMessage.tryEmit("Please enter a quantity first.")
        }
    }
}
```

**New user flow on New Entry screen:**
```
① Scan barcode → item fills automatically
② Enter date using numpad (8 taps) → date fills
③ Tap qty digits on numpad (1, 2, 3... as many digits as needed)
   → digits accumulate: "1", "12", "124" etc.
   → NO auto-save yet — user can keep entering digits
④ Tap PC or OUT or CTN
   → setUnit() called
   → if item + date + qty all valid → save fires
   → fields clear → focus returns to barcode
   → ready for next item
```

**Important: The C button behaviour in numpad (for qty field):**
- Single tap: removes last digit from qty string → accumulation can be corrected
- Long press: clears entire qty field
- This allows correction BEFORE tapping PC/OUT/CTN

**The PC/OUT/CTN buttons must ALWAYS be visible/active when QTY field has been entered:**
- Remove the dimming logic for unit buttons when QTY field is not "focused"
- Instead: unit buttons are ALWAYS at full opacity (not dimmed)
- Rationale: the user needs to tap them to trigger save — they must always be visible and tappable

**After unit selection and save:**
- `clearEntry()` resets `_unit.value = "PC"` (PC is always default again)
- All fields clear
- `_activeField.value = ActiveField.BARCODE`
- Ready for next scan

**Edge case — what if user taps PC/OUT/CTN before entering qty:**
- `setUnit()` checks qty > 0
- If qty = 0: show Snackbar "Please enter a quantity first." and do NOT save
- User must enter qty before tapping unit

---

## COMPILE CHECKPOINTS

After Modification 1 (Team Linking entities + navigation): compile → zero errors ✅
After Modification 1 (Team Linking screen + dashboard changes): compile → zero errors ✅
After Modification 2 (Item Linking): compile → zero errors ✅
After Modification 3 (Bold stock report): compile → zero errors ✅
After Modification 4 (Remove CSV menu items): compile → zero errors ✅
After Modification 5 (Stock numpad to bottom + C button): compile → zero errors ✅
After Modification 6 (Auto-save fix): compile → zero errors ✅

Final: build debug APK → install on Samsung S23 Ultra → test all 6 modifications.

---

## IMPLEMENTATION ORDER

1. Start with Modification 6 (auto-save fix) — smallest change, critical bug fix
2. Modification 3 (bold stock report) — one-minute change
3. Modification 4 (remove CSV menu items) — quick cleanup
4. Modification 5 (stock numpad to bottom) — layout change
5. Modification 2 (Item Linking) — new feature, medium complexity
6. Modification 1 (Team Linking) — largest, most critical, do last when codebase is stable

Report after each modification. Do not proceed without instruction.
