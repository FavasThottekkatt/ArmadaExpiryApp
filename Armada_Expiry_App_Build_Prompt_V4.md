# MASTER BUILD PROMPT — VERSION 4
## Expiry Tracking App — Armada Distribution (Abu Dhabi & Al Ain)

> Paste this entire document into Cursor / Claude Code as the project brief.
> It is written for an AI coding agent. Follow every instruction exactly.
> Do not skip, summarise, or reinterpret any section.
> **[V4 NEW]** items are additions over V3. Everything else from V3 is retained in full.

---

## 0. HOW TO USE THIS PROMPT (agent — read this first, every time)

You are building a production-grade native Android app. Do not generate the whole app in one shot. Build it strictly in the phases listed in Section 15, **one phase at a time**. After every phase: compile, run on a real or emulated device, confirm **zero errors and zero warnings**, then STOP and report:

> "Phase N complete. Ready for Phase N+1."

Do not proceed to the next phase until explicitly told to.

If anything in this brief is ambiguous, choose the safest performant option and leave a clearly marked `// TODO(confirm):` comment instead of guessing silently.

---

### HARD RULES — these override everything else, no exceptions:

1. **NEVER freeze the UI.** Any operation over ~16 ms (DB import, OCR, Excel export, large queries) must run on a background coroutine / `Dispatchers.IO`. The main thread is for drawing only.

2. **NEVER read CSV or Excel at runtime.** CSV/Excel is for import and export only. The live runtime database is Room (SQLite), always.

3. **NEVER load all records at once.** Use Paging 3 — load 25 rows at a time.

4. **NEVER lose data.** Every entry is written to Room the instant it is created, before any UI update.

5. **NEVER cover an active entry field with the soft keyboard.** Use `WindowCompat.setDecorFitsSystemWindows(window, false)` + `Modifier.imePadding()`. The custom numpad (Section 6.3) replaces the system keyboard for number input — the system keyboard must be suppressed on numpad-controlled fields.

6. **NEVER use fixed pixel widths.** All layouts fully responsive using `fillMaxWidth`, `weight`, `BoxWithConstraints`, or adaptive dp values.

7. **[V4] NEVER allocate objects inside scroll callbacks, `onDraw`, or `LazyColumn` item composables.** All data must be pre-computed. Use `remember {}` and `derivedStateOf {}` aggressively.

8. **[V4] NEVER catch `Throwable` or `Exception` silently.** Every catch block must: (a) log to crash log file, and (b) show a user-friendly Snackbar or dialog. No silent swallowing.

9. **[V4] NEVER start a coroutine without a `CoroutineExceptionHandler`.** Every `launch {}` block must have a named exception handler that logs the error and updates UI state accordingly.

10. **[V4] NEVER let Apache POI run without an OOM guard.** Wrap all POI operations in a try-catch that catches both `Exception` and `OutOfMemoryError`, frees resources in `finally`, and shows a user-friendly error if export fails.

---

## 1. PROJECT SUMMARY & NON-NEGOTIABLES

A lightweight, fast, fully offline Android app for Armada Distribution merchandisers in Abu Dhabi & Al Ain. Purpose: scan product barcodes at FMCG retail outlets, record expiry dates and quantities, and share a monthly Excel report by email or WhatsApp — all without any internet connection except at the moment of sharing.

**Non-negotiables:**

- 100% offline operation. No internet required except when the user actively shares a report.
- Buttery smooth on entry-level Android phones. 60 fps scrolling, instant barcode lookup, zero jank, zero freezes, zero crashes.
- Barcode scan → instant item fill from a local Room DB of 3,000+ products.
- Superb OCR for reading difficult, small, embossed, or faded printed expiry dates.
- Minimal training needed — a merchandiser must use it correctly on the very first try without any manual.
- Fully standalone. No server, no cloud sync, no admin panel, no user management.
- All stored data must always be fully retrievable by the app, even after the app is closed or the device restarts.
- Ships as a signed release APK and is Google Play Store publishable.

**[V4] Master data source format:** The bundled master data files are **CSV** format (ItemList.csv, Outlets.csv). These are imported into Room on first launch. The app exports reports to **.xlsx** (Excel) format. CSV is used only for import — never for runtime lookup.

---

## 2. TECH STACK (LOCKED — do not substitute any item)

| Layer | Choice | Reason |
|---|---|---|
| Language | Kotlin | Native Android, modern, safe |
| UI framework | Jetpack Compose (Material 3) | Declarative, smooth, responsive |
| Architecture | MVVM + Repository pattern | Clean separation, testable |
| Local DB | Room (SQLite) with **WAL mode** [V4] | WAL = faster concurrent reads during writes |
| DB indexes | Indexed barcode + description + productCode columns | Sub-50ms lookup on 3,000+ rows |
| Async | Kotlin Coroutines + Flow + SupervisorJob [V4] | Non-blocking, isolated failure per child |
| Paging | Paging 3 with prefetch [V4] | Pre-loads next page before user scrolls |
| Barcode scan | ML Kit Barcode Scanning | Fast, fully offline |
| OCR | ML Kit Text Recognition v2 | On-device, offline, superior accuracy |
| Excel export | Apache POI (org.apache.poi:poi-ooxml) with OOM guard [V4] | Full .xlsx read/write |
| CSV import | OpenCSV or Kotlin stdlib CSV parsing | Import only — never for runtime |
| File sharing | FileProvider + Android ShareCompat | Share .xlsx via Gmail, WhatsApp, etc. |
| File storage | `getExternalFilesDir` (app-scoped, no permission needed on API 29+) | Always retrievable |
| Notifications | WorkManager + NotificationCompat | Local scheduled notifications |
| Haptics | HapticFeedbackConstants + ToneGenerator | Scan feedback |
| Dependency injection | Hilt | Clean, compile-time safe |
| Date handling | java.time (API 26+) / ThreeTenABP for API 24–25 | ISO storage, locale display |
| Crash logging | Custom CrashHandler writing to local .log file | See Section 14 |
| Assets | assets/ folder for logo PNG + seed CSVs | Copied to local storage on first run |
| **[V4] Baseline Profiles** | `androidx.profileinstaller` | Pre-compiles hot paths → 40% faster cold start |
| **[V4] Memory cache** | Kotlin `LinkedHashMap` (LRU, 100 entries) | Instant barcode re-lookup, zero DB round-trip |
| **[V4] StrictMode** | Debug builds only | Catches any accidental main-thread DB/IO access |

**Min SDK:** Android 7.0 (API 24). **Target SDK:** latest stable.
**Test profile:** low-RAM device (2 GB RAM, mid-range CPU) — must be smooth there.

---

## 3. ARCHITECTURE & DATA FLOW

```
First launch (background coroutine — never blocks UI):
  assets/ItemList.csv  ──┐
  assets/Outlets.csv   ──┴──► [V4] CSV Validation Screen ──► bulk INSERT into Room
                                    barcode column INDEXED  ← critical
                                    description column INDEXED
                                    productCode column INDEXED [V4]
                                    ──► [V4] CsvMetadata saved (version, count, timestamp)

Runtime — every operation:
  Scan barcode  ──► [V4] Check LRU memory cache first (100 items)
                         Hit: return instantly (0 ms)
                         Miss: SELECT * FROM items WHERE barcode = ? (< 50 ms, indexed)
                              ──► add to LRU cache

  Save entry    ──► INSERT into expiry_entries (immediate, Room, WAL mode)
  Share report  ──► SELECT from Room ──► build .xlsx (background, OOM-guarded) ──► ShareCompat
  Auto-backup   ──► [V4] triggered on export, archive, and re-import events

Master data update:
  User replaces ItemList.csv ──► [V4] CSV Validation Screen ──► background re-import
                                 ──► [V4] CsvMetadata updated

[V4] App startup sequence (background, parallel where safe):
  1. Pre-warm Room connection (open DB on background thread before login completes)
  2. Load merchandiser + salesman lists into memory
  3. Check startup health (item count, outlet count)
  4. Check for last-session recovery
  5. Pre-load LRU cache with last 20 scanned barcodes
```

**Key rule repeated:** No CSV or Excel file is ever touched during a barcode lookup, a save, or any live screen interaction. Only Room/SQLite (or LRU cache).

---

## 4. DATA MODEL (Room schema)

```kotlin
// WAL mode — set in Database builder [V4]
@Database(entities = [...], version = 1)
abstract class AppDatabase : RoomDatabase() {
  // In companion object / builder:
  // .setJournalMode(JournalMode.WRITE_AHEAD_LOGGING) // [V4] WAL mode
}

// Master: products — imported once from ItemList.csv
// CSV columns: BARCODE, DESCRIPTION, CODE
@Entity(
  tableName = "items",
  indices = [
    Index(value = ["barcode"], unique = true),   // CRITICAL
    Index(value = ["description"]),
    Index(value = ["productCode"])               // [V4] product code search
  ]
)
data class Item(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val barcode: String,
  val description: String,
  val productCode: String?
)

// Master: outlets — imported once from Outlets.csv
// CSV columns: CODE (outlet_code), CUSTOMER (outlet_name), SHORT_NAME (sheet tab name)
@Entity(
  tableName = "outlets",
  indices = [Index(value = ["outletName"])]
)
data class Outlet(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val outletCode: String,
  val outletName: String,
  val shortName: String    // [V4] used as Excel sheet tab name (max 31 chars)
)

// Live data: expiry entries
@Entity(
  tableName = "expiry_entries",
  indices = [
    Index(value = ["expiryDate"]),
    Index(value = ["archived"]),
    Index(value = ["outletCode"])
  ]
)
data class ExpiryEntry(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val barcode: String,
  val description: String,
  val productCode: String?,
  val expiryDate: String,        // stored ISO: yyyy-MM-dd
  val quantity: Int,
  val unit: String = "PC",       // [V4] PC / OUT / CTN — default always PC
  val outletName: String,
  val outletCode: String,
  val merchandiser: String,
  val salesman: String,
  val entryTimestamp: String,
  val archived: Boolean = false
)

// [V4] CSV version tracking — stored in Room
@Entity(tableName = "csv_metadata")
data class CsvMetadata(
  @PrimaryKey val fileType: String,   // "ITEMS" or "OUTLETS"
  val importedAt: String,             // ISO timestamp
  val recordCount: Int,
  val skippedRows: Int,
  val fileHash: String                // MD5 of CSV for change detection
)

// [V4] Stock entries — low stock and out of stock tracking
@Entity(
  tableName = "stock_entries",
  indices = [
    Index(value = ["outletCode"]),
    Index(value = ["archived"])
  ]
)
data class StockEntry(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val barcode: String,
  val description: String,
  val productCode: String?,
  val isOos: Boolean = false,     // true = Out of Stock (red LED)
  val quantity: Int = 0,          // 0 when OOS, > 0 when Low Stock
  val unit: String = "PC",        // PC / OUT / CTN
  val outletName: String,
  val outletCode: String,
  val merchandiser: String,
  val salesman: String,
  val entryTimestamp: String,
  val archived: Boolean = false
)

// Crash log stored as plain-text .log file — NOT in Room
```

**Status derived at query time — never stored:**

```kotlin
fun ExpiryEntry.status(): Status {
  val today = LocalDate.now()
  val expiry = LocalDate.parse(expiryDate)
  val days = ChronoUnit.DAYS.between(today, expiry)
  return when {
    days < 0    -> Status.EXPIRED
    days <= 30  -> Status.WITHIN_30   // [V4] matches dashboard category
    days <= 60  -> Status.WITHIN_60
    days <= 90  -> Status.WITHIN_90   // [V4] new category
    else        -> Status.SAFE
  }
}
```

---

## 5. DESIGN SYSTEM — EYE-COMFORT PROFESSIONAL PALETTE

The UI must feel calm, cool, and easy on the eyes for merchandisers working for extended periods in bright UAE retail environments.

### Color tokens

| Token | Hex | Used for |
|---|---|---|
| bg.app | #F0F4F8 | Main app background |
| bg.card | #FFFFFF | Cards, list items, input surfaces |
| bg.header | #1E3A5F | Top app bar, outlet banner — deep navy |
| brand.accent | #2E86C1 | Primary buttons, active nav, links |
| brand.title | #1A5276 | Screen titles, section headings |
| table.header | #2874A6 | Data table header row |
| nav.active | #2E86C1 | Active bottom nav tab |
| nav.inactive | #95A5A6 | Inactive bottom nav tab |
| text.primary | #1C2833 | Main body text |
| text.secondary | #5D6D7E | Subtitles, hints, placeholders |
| text.onDark | #FFFFFF | Text on dark backgrounds |
| border | #D5D8DC | Input field borders, dividers |
| shadow | #00000014 | Soft card shadows |
| field.active.bg | #E8F5E9 | Active/focused field background |
| field.active.border | #27AE60 | Active/focused field left border |
| field.active.label | #1E8449 | "Entering: BARCODE" label |
| field.filled.bg | #EAF4FB | Field filled with valid data |
| field.manual.bg | #FFF3F3 | Manual barcode entry mode |
| field.manual.border | #E74C3C | Manual barcode entry border |
| numpad.bg | #ECEFF1 | Numpad row background |
| numpad.key | #FFFFFF | Numpad key face |
| numpad.key.border | #CFD8DC | Numpad key border |
| numpad.key.text | #1C2833 | Numpad digit text |
| numpad.clear | #FFEBEE | C key background |
| numpad.clear.text | #C0392B | C key text |
| status.expired | #C0392B | Expired — deep red |
| status.d30 | #E67E22 | ≤30 days — amber-orange **[V4 updated]** |
| status.d60 | #D4AC0D | ≤60 days — muted gold **[V4 updated]** |
| status.d90 | #27AE60 | ≤90 days — calming green **[V4 new]** |
| status.safe | #1E8449 | Safe — deeper green |
| cal.expired | #FFCDD2 | Date picker — expired dates |
| cal.soon | #FFE0B2 | Date picker — expiring soon |
| cal.safe | #C8E6C9 | Date picker — safe dates |
| btn.login | #1E3A5F | Login button |
| btn.ocr | #1A5276 | OCR scan button |
| btn.next | #2E86C1 | Next buttons |
| btn.newentry.ready | #1E8449 | New Entry when all fields filled |
| btn.newentry.inactive | #BDC3C7 | New Entry when fields incomplete |
| disabled | #BDC3C7 | Disabled background |
| disabled.text | #7F8C8D | Disabled text |
| fab.bg | #2E86C1 | FAB background |
| fab.icon | #FFFFFF | FAB icon |
| snackbar.undo | #27AE60 | Undo action text |
| **[V4] stock.oos.active** | **#C0392B** | **OOS LED — active/red** |
| **[V4] stock.oos.inactive** | **#BDC3C7** | **OOS LED — inactive/grey** |
| **[V4] stock.qty.highlight** | **#FFF176** | **QTY field background when qty entered — bright yellow** |
| **[V4] stock.qty.border** | **#F9A825** | **QTY field border when qty entered — amber** |
| **[V4] bulk.mode.border** | **#F39C12** | **Bulk mode left border** |
| **[V4] health.ok** | **#27AE60** | **Startup health check — pass** |
| **[V4] health.warn** | **#E67E22** | **Startup health check — warning** |
| **[V4] health.fail** | **#C0392B** | **Startup health check — fail** |
| **[V4] csv.valid.bg** | **#E8F5E9** | **CSV validation screen — valid row** |
| **[V4] csv.warn.bg** | **#FFF3E0** | **CSV validation screen — warning row** |

### Typography & spacing

- Font: Roboto (system default) — no custom fonts
- Title: 24sp bold. Section header: 18sp medium. Body: 14sp regular. Table cell: 13sp. Hint: 12sp. Numpad digit: 20sp bold.
- Minimum tap target: 48dp × 48dp
- Numpad key: min 44dp tall, `fillMaxWidth()` divided by 11
- Card corner radius: 12dp. Button corner radius: 10dp. Card elevation: 2dp.
- Inner card padding: 16dp. Between-card spacing: 12dp.

### Long description word wrap (CRITICAL — applies everywhere)

Every item description in every list, table, card, or dropdown must word-wrap to as many lines as needed. Use `maxLines = Int.MAX_VALUE + softWrap = true` on all Text composables showing descriptions. **Never truncate with ellipsis.** The cell/container expands vertically.

Applies to: entry screen table, reports table, archive table, dashboard cards, item search dropdown, description field when filled, CSV validation screen.

### Keyboard / IME handling (CRITICAL)

- In Activity: `WindowCompat.setDecorFitsSystemWindows(window, false)`
- Wrap scrollable content with `Modifier.imePadding()`
- Custom numpad suppresses system keyboard on barcode, date, and QTY fields — set `keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Null)` and intercept focus to show numpad
- All input fields placed as high on screen as possible
- Test every screen with the numpad open — no field may be hidden

### Responsive layout

- `fillMaxWidth()`, `weight()` for all layouts
- `BoxWithConstraints` for adaptive tablet vs phone behaviour
- No hardcoded pixel or fixed dp widths
- Portrait-optimised (primary); landscape must not break

---

## 6. SCREEN-BY-SCREEN SPECIFICATION

### 6.1 Login / Access Screen

**Visual design:**
- Background: bg.app (#F0F4F8)
- Top announcement pill: "SUBMIT BEFORE 20TH EVERY MONTH" — brand.accent text, white pill, border
- Title: "EXPIRY TRACKING" — brand.title, 28sp, bold
- Subtitle: "Armada Distribution" — text.primary, 20sp
- Region badge: "ABU DHABI & AL AIN" — small outlined pill

**Login card** (white, 12dp radius, soft shadow):
- Password field (lock icon) — **NO Employee ID field**, **NO eye toggle**
- **[V4] Invisible password field — NO asterisks, NO dots, NO characters shown while typing**
  - The field appears and stays completely blank while the user types
  - Implemented using `TextStyle(color = Color.Transparent)` so typed characters are invisible
  - The cursor is visible so the user knows the field is active and accepting input
  - This means nobody watching cannot see how many characters were typed
  - No eye/show toggle button — field is always invisible, no exceptions
- "Access App →" button — full width, btn.login deep navy
- **NO "Remember device" checkbox** — not needed (see one-time password logic below)

**[V4] ONE-TIME PASSWORD — most important login change:**
```
FIRST LAUNCH AFTER INSTALLATION:
  → Show login screen with invisible password field
  → User enters password: Armd*@2026
  → On correct password:
       → Store flag: isAppAuthenticated = true in EncryptedSharedPreferences
       → Store flag: appInstalledAndVerified = true
       → Proceed directly to app — never ask password again

ALL SUBSEQUENT LAUNCHES (including after restart, after battery removal):
  → Check EncryptedSharedPreferences for isAppAuthenticated = true
  → If true: SKIP login screen entirely, go straight to app
  → User never sees the password screen again after first login
  → No session timeout, no inactivity lock, no re-authentication

PASSWORD IS ONLY SHOWN AGAIN IF:
  → App is uninstalled and reinstalled (EncryptedSharedPrefs cleared)
  → User manually resets the app from Settings → Reset App (optional menu item)
```

**Wrong password (first launch only):**
- Shake animation + "Incorrect password. Please try again."
- Field clears completely, stays invisible, ready for retry

**[V4] Startup Health Check** (runs in background on every launch):
```
On every app launch, before any screen becomes interactive:
  Check 1: Room database file exists and is not corrupted
  Check 2: items table count > 0
  Check 3: outlets table count > 0
  Check 4: CsvMetadata exists for both ITEMS and OUTLETS

If all checks pass AND isAppAuthenticated = true:
  → Skip login entirely
  → Show green pill on Dashboard: "✅ [count] items · [count] outlets · [import date]"
  → Go straight to Dashboard / New Entry screen

If all checks pass AND isAppAuthenticated = false (first launch):
  → Show login screen normally

If items or outlets count = 0:
  → Show health.fail red banner: "⚠️ Master data missing. Please re-import."
  → "Go to Settings → Re-import" button

If Room corrupted:
  → Show: "Database error detected. Tap to reset and re-import master data."
  → Offer Room.databaseBuilder with fallbackToDestructiveMigration() + re-import
```

---

### 6.2 Outlet Details — Step 1 + Dashboard (same screen, scrollable)

Header: back arrow + "OUTLET DETAILS" in brand.title

**Form fields (all * mandatory):**

**Merchandiser Name *** — searchable dropdown + free-text.
Seed list:
```
AKHIL KAKKARA ANILAN, AKHIL SUNNY THARAYIL, AMRIT PARIYAR,
BHIMKAJI TAMANG, BIMAL SUNWAR, DILIP KUMAR CHHETRI,
FIROZKHAN C SULAIMAN, KRISHNA SHAHI, NARAYAN GIRI, SANIL JAGALRAJ,
SHUHAIB P, SUSHIL THING TAMANG, VISHNU GOPAKUMAR,
RAMEES RAJILABEEVI, MUKESH SHRESHTHA, OMKARA BABU BANDI,
MOHAMMED HASSAN, ABID ALUNGAL, KUNJI MOIDEEN, SARATH MAKKAKOD,
SARATH RAJ PR, MOHAMMED SHAN, AKBAR SULTAN
```

**Salesman Name *** — searchable dropdown + free-text.
Seed list: `Muneer, Rajesh Shrestha, Noushir, Sreejith, Shiva, Ramraj, Vishnu Jayalal`

**Outlet Name *** — searchable from Room outlets table (outletCode, outletName). Manual entry allowed.

**Outlet Code** — auto-filled when outlet selected; editable if manually entered.

**"Next →" button:** Disabled until all 3 mandatory fields filled. If tapped incomplete: Snackbar "Please complete all required fields." + red border on empty fields.

---

**Dashboard section** (below form, same scrollable screen):

Heading: "Expiry Dashboard" in brand.title

**Quick SKU or Item lookup search bar** (searches by description OR product code [V4])

**[V4] Master Data Info strip** (below search bar, always visible):
```
📦 Items: [count]  |  🏪 Outlets: [count]  |  📅 Updated: [import date]
```
Tapping the strip navigates to Settings → Master Data Details.

**Stat cards** — show live counts from Room (active, non-archived, current outlet) ONLY when an outlet is loaded. If no outlet: show "—" with no colors.

**[V4] Updated dashboard categories (aligned to FMCG retail practice):**

| Card | Colour | Condition |
|---|---|---|
| 🔴 EXPIRED | status.expired | days < 0 |
| 🟠 ≤ 30 DAYS | status.d30 | 0 ≤ days ≤ 30 |
| 🟡 31–60 DAYS | status.d60 | 31 ≤ days ≤ 60 |
| 🟢 61–90 DAYS | status.d90 | 61 ≤ days ≤ 90 |

Items with > 90 days are considered safe and not highlighted.

**Latest Records section:**
- Header + "View Archive" link (right-aligned)
- Record cards: colored status dot · description (word-wrapped) · expiry date · quantity · ⋮ menu (edit / delete)
- Barcode NOT shown in cards
- Paging 3 — 25 at a time, lazy-load on scroll

---

### 6.3 New Entry — Step 2 ⭐ MOST CRITICAL SCREEN

**App shell:** bottom nav: Dashboard · New Entry (active) · Reports · History

**Top app bar:** hamburger · "Armada Distribution / EXPIRY TRACKING" · Armada logo

**Outlet banner** (full-width, bg.header navy, white text):
`Outlet: [Name] | Code: [Code]`

**[V4] Bulk / Repeat Mode banner** (shown only when Bulk Mode is active):
```
┌─────────────────────────────────────────────────────────────┐
│  🔁  BULK MODE ON — Last item pre-filled. Change if needed. │
│                                        [ Turn Off ]         │
└─────────────────────────────────────────────────────────────┘
```
Background: bulk.mode.bg (#FFF8E1). Left border: bulk.mode.border (#F39C12). "Turn Off" clears bulk mode and all fields.

---

**STEP FLOW INDICATOR**

```
① ITEM  ──▶  ② DATE  ──▶  ③ QTY  ──▶  ④ DONE
```

- Each step label is tappable — tapping jumps focus to that field
- Current active step: brand.accent highlight + underline
- Completed steps: ✅ + status.safe green
- Incomplete future steps: text.secondary grey

---

**ENTRY FIELDS** (placed as high as possible, above numpad and table)

All numpad-controlled fields suppress the system keyboard. When focused: light green background (field.active.bg), green left border 3dp, small label: "Entering: [FIELDNAME]".

---

**FIELD 1 — BARCODE / ITEM**

**Path A — Barcode (scan or manual):**
```
[ scan / Manual Barcode Entry .............. 📷 ]  [NEXT]
```
- 📷 → opens ML Kit barcode scanner
- On scan: vibrate + beep → [V4] check LRU cache first, then Room lookup → fills Description + Code → focus jumps to DATE
- Manual barcode: field turns field.manual.border red while typing → tap [NEXT] → lookup → fill

**Path B — Description search:**
```
[ SELECT / ENTER DESCRIPTION .............. ▼ ]
```
- System keyboard opens (text, not numpad — descriptions contain letters)
- Live Room query: `SELECT * FROM items WHERE description LIKE '%query%' OR productCode LIKE '%query%'` [V4] — searches both description AND product code
- 150 ms debounce before querying
- Virtualized lazy dropdown, word-wrapped results, product code shown in grey smaller text
- Tap result → fills all fields → focus jumps to DATE

**[V4] Path C — Product Code search:**
```
[ ENTER PRODUCT CODE .............. ]
```
A third search field, smaller, placed below Path A. User enters known product code (e.g. AL1004). Room lookup: `SELECT * FROM items WHERE productCode = ?`. If matched, fills all fields and jumps to DATE. Many FMCG merchandisers know product codes but not barcodes.

---

After a successful item match via any path:
- Description: full name, bold, brand.title, ✅ on right
- Barcode: matched barcode, grey, smaller text
- Code: product code, grey, smaller text
- Description field background: field.filled.bg light blue tint

---

**FIELD 2 — EXPIRY DATE**
```
[ dd / mm / yyyy  📅 ]
```
- Custom numpad only — system keyboard suppressed
- When focused: field.active.bg + "Entering: DATE"
- Auto-format with smart jump:
  - 2 DD digits → auto-inserts "/" → cursor jumps to MM
  - 2 MM digits → auto-inserts "/" → cursor jumps to YYYY
  - 4 YYYY digits → date complete → green ✅
  - 8 numpad taps = full date, zero extra steps
- C key: deletes one digit at a time, backwards through segments
- 📅 calendar icon → date picker (see Section 6.3 Color-coded calendar)
- "Scan Expiry (OCR)" button — see Section 9

**Date picker color coding:**
- Already past → cal.expired red background + confirmation warning if selected
- Within 30 days → cal.soon orange
- More than 30 days ahead → cal.safe green

---

**FIELD 3 — QTY**
```
[ QTY ........... ]
```
- Custom numpad only — system keyboard suppressed
- When focused: field.active.bg + "Entering: QTY"
- Accepts any positive integer (min 1)
- C key: single tap = delete last digit; long press = clear field
- No quick-tap shortcuts for quantity — numpad digits only

---

**CUSTOM NUMPAD (always visible on New Entry screen)**
```
┌──────────────────────────────────────────────────────────────┐
│ [ 0 ][ 1 ][ 2 ][ 3 ][ 4 ][ 5 ][ 6 ][ 7 ][ 8 ][ 9 ][ C ]  │
├──────────────────────────────────────────────────────────────┤
│ [          PC          ][       OUT       ][       CTN      ] │
└──────────────────────────────────────────────────────────────┘
```
- Always visible — never hidden, never slides up
- **Row 1:** Exactly 11 keys: digits 0–9 plus C
- **Row 2:** Unit selector — PC / OUT / CTN toggle buttons

**Unit toggle row behaviour:**
- **PC is always the default** — pre-selected on every new entry and after every save
- Selected unit: background brand.accent blue, white text — clearly active
- Unselected units: grey background, dark text
- Unit row is **full width**, divided into 3 equal buttons — large, easy to tap
- Unit row only **activates when QTY field is focused** — dimmed (opacity 0.4) otherwise
- One tap changes unit instantly — no dropdown, no extra screen
- Selected unit is saved to Room with the entry
- Unit resets to PC after every "NEW ENTRY" tap

**C key behaviour:**
- Single tap: deletes the last digit in the currently focused field
- Long press: clears the entire focused field
- Background: numpad.clear light red. Text: numpad.clear.text red.

**Context-aware:** digits go to whichever field currently has focus.
If no numpad-controlled field is focused → numpad keys dimmed (opacity 0.4).

**Key design:** white face, light border, 20sp bold, 12dp radius, 2dp elevation, ripple effect.
Key height: min 44dp. Full row width = `fillMaxWidth()`.
Each tap: immediate ripple + scale animation (0.95) + haptic micro-vibration.

---

**[V4] BULK QUANTITY MODE — "Repeat Last Item"**

After saving any entry, a small secondary button appears below the "NEW ENTRY" button:
```
[ 🔁 Repeat This Item ]
```
When tapped:
- Activates Bulk Mode (shows banner at top)
- Pre-fills barcode, description, code from the last saved entry
- Clears date and QTY only — focus jumps straight to DATE
- User changes only expiry date (or QTY if different)
- Saves as a new entry (does NOT merge with previous)
- Bulk Mode stays active until user taps "Turn Off" in the banner

This is a time-saver when the same product has items with multiple different expiry dates on the same shelf.

---

**"NEW ENTRY" BUTTON — DUAL PURPOSE (Save + Clear)**
```
[  ✅  NEW ENTRY  ]
```
- Green (btn.newentry.ready) when all three fields complete; tappable
- Grey (btn.newentry.inactive) when any field incomplete; shows Snackbar if tapped
- On tap (all complete):
  1. Validates all fields (Section 7)
  2. Writes to Room immediately (atomic, Dispatchers.IO)
  3. Entry appears in table at correct sorted position
  4. Clears all entry fields
  5. [V4] If Bulk Mode active: pre-fills item fields from last saved entry, clears date + QTY
  6. [V4] If Bulk Mode not active: all fields cleared
  7. Focus returns to barcode field (Step ①)
  8. Step flow indicator resets to ① highlighted
  9. Numpad remains visible and ready

---

**ENTRY DATA TABLE (below numpad, same screen)**

```
┌─────────────────────────┬──────────┬────────┬────────┐
│ DESCRIPTION             │ EXPIRY   │  QTY   │  CODE  │
├─────────────────────────┼──────────┼────────┼────────┤
│ AMICELLI HAZELNUT 12.5G │ 12/05/26 │ 12 PC  │ AL1004 │
│ EVIAN WATER 500ML       │ 25/06/26 │  6 OUT │ EV500  │
│ KITKAT CHUNKY 40G       │ 30/06/26 │ 24 CTN │ KK40   │
└─────────────────────────┴──────────┴────────┴────────┘
```

- Columns: DESCRIPTION · EXPIRY · QTY · CODE
- **QTY column shows quantity + unit together** (e.g. "12 PC", "6 OUT", "24 CTN")
- Description word-wraps — never truncated — full size always visible
- Sorted ascending by expiry date at all times
- Paging 3 (25 rows), smooth scrolling
- Table header: table.header blue, white text. Alternating row: white / #F8FAFC
- Swipe left → red "Delete" → 5-second Undo Snackbar
- Long-press → edit dialog (includes unit selector in edit dialog)

---

**FLOATING ACTION BUTTON (FAB)**
- Bottom-right, always visible
- Icon: camera/scan
- Background: fab.bg
- Tap: immediately focuses barcode field + activates camera scanner
- Stays above bottom nav bar

---

### 6.4 Reports Screen

- Screen title: "Reports"
- Outlet selector (searchable dropdown from Room)
- Outlet Code (auto-filled)
- When outlet selected → pre-share summary card:
```
✅ Outlet: [Name]
📦 Items entered: [count]
🔴 Expired: [count]
🟠 Expiring ≤ 30 days: [count]
👤 Merchandiser: [name]
📅 Report period: [current month name + year, e.g. June 2026]
```
- Report table (4 cols: CODE · DESCRIPTION · EXPIRY · QTY, word-wrap, ascending expiry, Paging 3)
- "Share Excel Report" button → background build (Apache POI, [V4] OOM-guarded) → ShareCompat

---

**EXCEL FILE — COMPLETE SPECIFICATION**

**File naming format:**
```
[Merchandiser Name] – [Salesman Name] – [Month Name] [Year] – Expiry Report.xlsx

Example:
Akhil Sunny – Rajesh Shrestha – June 2026 – Expiry Report.xlsx
```
- Merchandiser name and Salesman name come from the currently selected session values
- Month and Year come from the current device date at time of export
- File saved to: `getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)`

---

**Multi-sheet structure:**
```
One Excel file = One Merchandiser + One Salesman + One Month

Inside the file:
  Sheet 1  →  Outlet A  (sheet name = outlets.shortName)
  Sheet 2  →  Outlet B  (sheet name = outlets.shortName)
  Sheet 3  →  Outlet C  (sheet name = outlets.shortName)
  ... one sheet per outlet visited that month
```
- Sheet tab name = `shortName` column from Room outlets table (max 31 characters — Excel limit)
- Each sheet contains data for ONE outlet only
- Sorted ascending by expiry date always

---

**Each sheet content format:**
```
Row 1:  SALESMAN :       [salesman name]
Row 2:  MERCHANDISER :   [merchandiser name]
Row 3:  OUTLET NAME :    [full outlet name]
Row 4:  CODE :           [outlet code]
Row 5:  (blank row)
Row 6:  CODE  |  DESCRIPTION  |  EXPIRY DATE  |  QTY
Row 7+: data rows, sorted ascending by expiry date
```

**QTY column in Excel shows quantity + unit combined:**
```
CODE    | DESCRIPTION          | EXPIRY DATE | QTY
AL1004  | AMICELLI HAZELNUT    | 12/05/2026  | 12 PC
EV500   | EVIAN WATER 500ML    | 25/06/2026  | 6 OUT
KK40    | KITKAT CHUNKY 40G    | 30/06/2026  | 24 CTN
```
No separate unit column — unit is part of the QTY cell value.

- No logo, no totals row, no colored formatting
- Column widths auto-fit to content
- Data 100% sourced from Room — never from any cached or partial source

---

**WHATSAPP TEXT REPORT — COMPLETE SPECIFICATION**

A plain text report that can be shared directly via WhatsApp, email, Telegram,
or any Android share target. Generated from Room data. No formatting, no headers,
no company name, no merchandiser name, no salesman name.

**"Share Text Report" button** on the Reports screen (separate from "Share Excel Report"):

When tapped → show two options:

```
┌─────────────────────────────────────┐
│  Share Text Report                  │
│                                     │
│  [ 📋 This Outlet Only ]            │
│  [ 📋 All Outlets ]                 │
└─────────────────────────────────────┘
```

**Option 1 — This Outlet Only:**
```
Expiry Report
CARREFOUR KHALIDIYAH

AMICELLI HAZELNUT 12.5G - 12/05/26 - 12 PC
EVIAN WATER 500ML - 25/06/26 - 6 OUT
KITKAT CHUNKY 40G - 30/06/26 - 24 CTN
```

**Option 2 — All Outlets (under current merchandiser + salesman):**
```
Expiry Report
CARREFOUR KHALIDIYAH

AMICELLI HAZELNUT 12.5G - 12/05/26 - 12 PC
EVIAN WATER 500ML - 25/06/26 - 6 OUT

LULU MUSHRIF

KITKAT CHUNKY 40G - 30/06/26 - 24 CTN
NESCAFE GOLD 200G - 15/07/26 - 12 PC

SPINNEYS KHALIDIYAH

RED BULL 250ML - 20/07/26 - 6 OUT
```

**Text report rules:**
- First line always: `Expiry Report`
- Then outlet name (SHORT_NAME or full name) — no code
- One blank line between outlet name and items
- Each item line: `DESCRIPTION - DD/MM/YY - QTY UNIT`
- Sorted ascending by expiry date within each outlet
- One blank line between outlets (Option 2 only)
- No totals, no headers, no company name, no merchandiser, no salesman
- After text is generated → standard Android ShareCompat sheet opens
- User chooses WhatsApp, email, Telegram, or any installed share app
- Text is plain — copies and pastes perfectly in any app

---

**CUMULATIVE FILE LOGIC — CRITICAL:**

A merchandiser works across multiple days and sessions during the month.
The app must always update the SAME file, never create duplicates.

```
When "Share Excel Report" is tapped:

STEP 1 — Determine file name
  [Merchandiser] – [Salesman] – [Month] [Year] – Expiry Report.xlsx

STEP 2 — Check if file already exists this month
  YES → Open the existing file (Apache POI)
  NO  → Create a new file

STEP 3 — Find or create the sheet for the current outlet
  Sheet name = outlets.shortName for the selected outlet
  If sheet exists → CLEAR all rows in that sheet from Row 7 onwards
  If sheet does not exist → Create a new sheet with that name

STEP 4 — Rebuild sheet content from Room (fresh every time)
  Write header rows (Rows 1–5)
  Write column headers (Row 6)
  Query Room: SELECT all active non-archived entries for this
              outlet + merchandiser + salesman + current month
  Write all rows, sorted ascending by expiry date

STEP 5 — Save the file (overwrite existing file)

STEP 6 — Share via ShareCompat (Gmail first, else WhatsApp, else full sheet)
```

**Why rebuild from Room every time (not append):**
This guarantees 100% accurate data. If the merchandiser edited or deleted
an entry between sessions, the Excel always reflects the exact current
state of Room — never stale or duplicate rows.

**File persistence:**
- The file stays in `getExternalFilesDir(DIRECTORY_DOCUMENTS)` all month
- It is updated (not replaced) every time the merchandiser shares
- At end of month it contains ALL outlets visited by that merchandiser + salesman
- New month → new file (previous month's file stays untouched in Documents)

---

- Smart share: Gmail first if installed, else WhatsApp, then full ShareCompat sheet
- In-app past exports list (shows all monthly files)

**[V4] Auto-backup triggered on every export:** silently writes `expiry_backup_YYYYMMDD_HHmm.db` to `getExternalFilesDir("Backups")`. Background coroutine, never blocks UI.

---

### 6.5 History / Archive Screen

- Tab label: "History"
- Outlet selector at top
- Shows archived records for selected outlet only (archived = true)
- Table: CODE · DESCRIPTION · EXPIRY · QTY (4 cols, word-wrap, ascending, read-only)
- "Export History" button → same ShareCompat Excel export
- "Archive This Month" button → confirmation → sets `archived = true` on all active records for that outlet/month

---

**App shell:** bottom nav: Dashboard · New Entry · Reports · Stock · History

---

### 6.6 Stock Report Screen — Low Stock & Out of Stock

**Purpose:** Merchandiser scans or searches for products at the outlet and marks each one as Out of Stock (OOS) or enters a Low Stock quantity. Uses the same ItemList.csv source (Room items table) as a searchable list.

**Bottom nav tab label:** "Stock"

---

**SCREEN LAYOUT (top to bottom, maximum list space):**

```
┌──────────────────────────────────────────────────────────┐
│ [ 🔍 Search product name or barcode................... ] │  ← search bar
├──────────────────────────────────────────────────────────┤
│ FIXED PANEL:                                             │
│ [0]    [1]    [2]    [3]    [4]    [5]    [6]    [7]    [8]    [9] │
│ [      PC      ]   [     OUT      ]   [     CTN     ]  [SHARE]│
├──────────────────────────────────────────────────────────┤
│ OOS │ BARCODE    │ DESCRIPTION              │    QTY    │  ← column headers
├──────────────────────────────────────────────────────────┤
│  🔴 │ 6281003040 │ AMICELLI HAZELNUT        │           │
│     │            │ CREAM WAFER 12.5G        │           │
│  ⚫ │ 6281003041 │ EVIAN MINERAL WATER 500ML│ █ 5 PC █  │  ← yellow bg
│  ⚫ │ 6281003042 │ KITKAT CHUNKY MILK 40G   │           │
│           (full remaining screen height — LazyColumn)    │
└──────────────────────────────────────────────────────────┘
```

**No headers, no titles, no merchandiser name, no salesman name on screen.**
**Maximum possible screen space given to the item list.**

---

**SEARCH BAR:**
- Full width, system keyboard
- Filters items list in real time — debounce 150ms
- Searches both description AND barcode
- Placeholder: "Search product name or barcode…"
- Clear button (X) when text entered
- System keyboard — NOT the custom numpad

---

**FIXED PANEL (always visible above list):**

Row 1 — Numpad digits:
```
[ 0 ]  [ 1 ]  [ 2 ]  [ 3 ]  [ 4 ]  [ 5 ]  [ 6 ]  [ 7 ]  [ 8 ]  [ 9 ]
```
- 10 digit keys, maximum spacing across full width
- Digits go to whichever QTY field is currently focused
- No C key in this panel (QTY field tapped again clears it)

Row 2 — Unit + Share:
```
[          PC          ]  [         OUT         ]  [         CTN         ]  [ SHARE ]
```
- PC / OUT / CTN: unit toggle, PC always default, selected = brand.accent blue
- SHARE button: brand.accent blue, white text, right-aligned
- Unit applies to the currently focused row's QTY entry

---

**COLUMN HEADERS (fixed, does not scroll):**
```
OOS │ BARCODE │ DESCRIPTION │ QTY
```
- table.header blue background, white text
- OOS column: narrow, centered
- BARCODE column: medium width
- DESCRIPTION column: widest, flex remaining space
- QTY column: medium width, right-aligned

---

**ITEM LIST ROWS (LazyColumn, Paging 3, 25 at a time):**

Each row contains:

**OOS column:**
- Circle button — ⚫ grey (default) / 🔴 red (OOS active)
- Tap to toggle: grey → red → grey
- Minimum tap target: 48dp
- When red (OOS): QTY field in same row is hidden/disabled

**BARCODE column:**
- Shows item barcode in small grey text
- Non-interactive

**DESCRIPTION column:**
- Full description, word-wraps to as many lines as needed
- Never truncated — row height expands to fit
- softWrap = true, maxLines = Int.MAX_VALUE

**QTY column:**
- Tapping focuses this row's QTY field
- Focused: numpad digits go here, unit buttons apply here
- Empty (default): blank, white background, grey border
- Quantity entered: background turns **bright yellow (#FFF176)**, amber border (#F9A825)
- Shows value + unit: "5 PC", "2 OUT", "1 CTN"
- Tapping a filled QTY field: clears it (tap again to re-enter)
- When OOS = true for this row: QTY field hidden entirely

**Row background:**
- Alternating white / #F8FAFC
- Rows with OOS = true: faint red tint background (#FFEBEE)
- Rows with QTY entered: faint yellow tint background (#FFFDE7)
- Untouched rows: standard alternating background

---

**SHARE BUTTON behaviour:**

When SHARE is tapped:
1. Build the WhatsApp text report in background (Dispatchers.IO)
2. Only include rows where isOos = true OR quantity > 0
3. Format (plain text, no headers, no company name):

```
CARREFOUR KHALIDIYAH

LOW STOCK
AMICELLI HAZELNUT CREAM WAFER 12.5G - 5 PC
EVIAN MINERAL WATER 500ML - 2 OUT
KITKAT CHUNKY 40G - 1 CTN

OUT OF STOCK
RED BULL ENERGY DRINK 250ML
NESCAFE GOLD BLEND 200G
```

Rules:
- Outlet name first (from current session — outletName)
- Blank line after outlet name
- "LOW STOCK" section: all rows where quantity > 0, sorted by description
- Blank line between sections
- "OUT OF STOCK" section: all rows where isOos = true, sorted by description
- If only LOW STOCK items: omit OUT OF STOCK section (and vice versa)
- If no items marked at all: Snackbar "No items marked. Mark items before sharing."
- Standard Android ShareCompat sheet opens (WhatsApp, email, Telegram, etc.)

---

**DATA PERSISTENCE:**
- All marked items saved to Room (stock_entries table) immediately on change
- Persists across app close and device restart
- Linked to current session (merchandiser + salesman + outletCode)
- "Clear All" option in hamburger menu for this screen only — confirmation dialog

---

**NAVIGATION:**
- Accessible via "Stock" tab in bottom nav (5th tab)
- Uses session data from SessionHolder (merchandiser, salesman, outlet)
- If no outlet selected: show banner "Please select an outlet in Dashboard first"

### 6.7 [V4] Settings Screen (hamburger menu)

Accessible from hamburger icon in top app bar. Items:

1. **Master Data** — shows CsvMetadata:
```
ItemList.csv
  Imported: 2026-06-10 08:15
  Records: 947   Skipped: 0

Outlets.csv
  Imported: 2026-06-10 08:15
  Records: 415   Skipped: 0
```
Immediately identifies if a merchandiser is using outdated master files.

2. **Re-import Master Data** → CSV Validation Screen → background import

3. **Backups** — list of auto-backup files with dates; shareable via ShareCompat

4. **View Crash Log** — shows crash_log.txt; shareable via ShareCompat for developer support

5. **About** — app version, build date

---

## 7. CORE BEHAVIORS & VALIDATIONS

**Quantity > 20 → confirmation:**
"Quantity exceeds 20 pieces. Are you sure you want to continue?"

**Past expiry date → confirmation:**
"The selected expiry date has already passed. Are you sure you want to continue?"

**[V4] Duplicate guard (improved):**
Same barcode + outletCode + expiryDate + merchandiser exists →
"Same item and expiry already entered by this merchandiser. Do you want to add more quantity?"
- Yes: increment quantity on existing row (atomic Room update)
- No: cancel, return to entry

**Change-warning protection** — if user changes Merchandiser / Salesman / Outlet while entries exist:
"Are you sure you want to change this field? All current entries will be saved."
→ Confirmed: auto-save all records to Room, create silent backup, clear entry screen, load new selection.

**Auto-save** — every entry written to Room immediately. Survives app closure, background kill, battery removal, device restart.

**Expiry list sort** — ascending by expiry date everywhere, always.

**Last session recovery banner** — on every app launch (after login), check Room for unarchived entries from previous session:
```
"You have [N] entries from your last session at [Outlet Name]. Continue?"
[ Continue ]  [ Start Fresh ]
```
"Start fresh" keeps data in Room but clears the active session context.

**[V4] Startup health check** — see Section 6.1. Runs in background before login screen becomes interactive.

**[V4] Auto-backup triggers:**
- After "Share Excel Report" is tapped
- After "Archive This Month" is confirmed
- After "Re-import master data" completes
All backups are silent, background, and non-blocking.

---

## 8. BARCODE SCANNING

ML Kit Barcode Scanning (`com.google.mlkit:barcode-scanning`).

- Opens only when camera button tapped or FAB tapped — never auto-starts
- Formats: EAN-13, EAN-8, UPC-A, UPC-E, Code-128, Code-39, QR, DataMatrix
- On successful scan:
  1. Haptic: short vibration (`HapticFeedbackConstants.CONFIRM`)
  2. Audio: soft beep (`ToneGenerator`, TONE_PROP_BEEP, ~100ms, low volume)
  3. **[V4] Check LRU memory cache** (100 items, keyed by barcode string):
     - Cache hit → return item instantly (0 ms), skip DB query
     - Cache miss → `SELECT * FROM items WHERE barcode = ?` (< 50 ms, indexed) → add result to LRU cache
  4. Auto-fills Description + Code
  5. Focus immediately jumps to DATE field
  6. Scanner closes, camera released
- Barcode not found: Snackbar "Barcode not found. Enter description manually." Focus → description field.

**[V4] LRU Cache implementation:**
```kotlin
// In ItemRepository:
private val barcodeCache = object : LinkedHashMap<String, Item?>(100, 0.75f, true) {
  override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, Item?>) = size > 100
}

suspend fun findByBarcode(barcode: String): Item? {
  barcodeCache[barcode]?.let { return it }           // Cache hit
  val item = itemDao.findByBarcode(barcode)           // DB query
  barcodeCache[barcode] = item                        // Cache miss → store
  return item
}

fun preloadRecentBarcodes(recentEntries: List<ExpiryEntry>) {
  // Called on startup: pre-warms cache with last 20 scanned barcodes
  recentEntries.forEach { entry ->
    runCatching { barcodeCache[entry.barcode] = itemDao.findBarcodeSync(entry.barcode) }
  }
}
```

---

## 9. EXPIRY DATE OCR

ML Kit Text Recognition v2 (`com.google.mlkit:text-recognition`).

- Opens only from "Scan Expiry (OCR)" button — separate from barcode
- Scan zone overlay (clear rectangle in viewfinder centre)
- Continuous frame analysis until valid date found
- Camera released when OCR screen not active
- All OCR processing on background dispatcher

**Frame pre-processing:**
- Auto-contrast enhancement
- Grayscale conversion
- Edge sharpening

**[V4] OCR Retry Button:**
After any OCR result (confident or not), show three options:
```
[ ✅ Accept: 25/06/2026 ]   [ 🔄 Try Again ]   [ ✏️ Enter Manually ]
```
- "Try Again" → reopens camera without closing OCR screen (no navigation needed)
- "Enter Manually" → closes OCR, opens date numpad, focuses date field
- This replaces the old "failed → manual" binary flow

**Smart OCR confidence indicator:**
- 🟢 High (single clear date, well-formatted) → auto-accept, brief green checkmark animation
- 🟡 Medium (partially ambiguous) → show detected date with "Is this correct? ✓ Yes / ✗ No / 🔄 Try Again"
- 🔴 Low (no date found or multiple conflicting) → "Could not read date. Please enter manually." → numpad

**Multiple dates found:** show selection list → user picks correct one.

**Date parsing — accept ALL formats:**

| Format | Example |
|---|---|
| DD/MM/YYYY | 25/06/2026 |
| DD-MM-YYYY | 25-06-2026 |
| DD/MM/YY | 25/06/26 |
| DD-MM-YY | 25-06-26 |
| MM/YYYY | 06/2026 |
| MM-YYYY | 06-2026 |
| MM/YY | 06/26 |
| MMM YYYY | JUN 2026 |
| DD MMM YYYY | 25 JUN 2026 |
| DD-MMM-YYYY | 25-JUN-2026 |
| YYYY-MM-DD | 2026-06-25 |
| MMMM YYYY | JUNE 2026 |
| DD.MM.YYYY | 25.06.2026 |
| DD.MM.YY | 25.06.26 |

No-day formats (e.g. MM/YYYY): default to **last day** of that month.
Store: ISO `yyyy-MM-dd`. Display: `dd/MM/yyyy`.

---

## 10. IMPORT / EXPORT

### Import (CSV → Room)

**[V4] CSV Validation Screen** (shown before every import, including first launch):

```
┌────────────────────────────────────┐
│  📋 CSV Import Preview             │
│                                    │
│  ItemList.csv                      │
│  ✅ Items found:        947        │
│  ⚠️  Duplicate barcodes:   0       │
│  ⚠️  Blank descriptions:    0      │
│  ⚠️  Null product codes:    6      │
│  ❌  Blank barcodes:        0      │
│                                    │
│  Outlets.csv                       │
│  ✅ Outlets found:       415       │
│  ⚠️  Duplicate codes:      0       │
│  ❌  Blank names:           0      │
│                                    │
│  [ Import Data ]  [ Cancel ]       │
└────────────────────────────────────┘
```
- ✅ green = valid
- ⚠️ orange = warning (non-fatal, will skip/log)
- ❌ red = error (fatal rows will be skipped; import still proceeds for valid rows)
- User must tap "Import Data" to confirm. Cannot be bypassed.
- After import: CsvMetadata saved with timestamp, record count, and MD5 hash of CSV file.

**First launch:**
- Copy bundled `assets/ItemList.csv` + `assets/Outlets.csv` to `getExternalFilesDir()`
- Show CSV Validation Screen
- On confirm: bulk-INSERT into Room (single transaction, Dispatchers.IO)
- Non-dismissable progress dialog during import
- Skip rows where barcode or description is blank (log skipped rows to crash log)
- productCode null → insert null (6 known items — acceptable)

**CSV column mapping:**
- `ItemList.csv`: BARCODE→barcode, DESCRIPTION→description, CODE→productCode
- `Outlets.csv`: CODE→outletCode, CUSTOMER→outletName

**[V4] Re-import triggers LRU cache invalidation:** `barcodeCache.clear()` immediately after re-import completes.

---

### Export (.xlsx)

- Apache POI, background coroutine — **never** on main thread
- **[V4] OOM guard:**
```kotlin
var workbook: XSSFWorkbook? = null
try {
  workbook = XSSFWorkbook()
  // ... build report ...
  workbook.write(outputStream)
} catch (oom: OutOfMemoryError) {
  // Log to crash log
  // Show: "Not enough memory to export. Try closing other apps and retry."
} catch (e: Exception) {
  // Log to crash log
  // Show user-friendly error Snackbar
} finally {
  workbook?.close()  // Always release POI resources
}
```
- Loading indicator during build
- Columns: CODE · ITEM DESCRIPTION · EXPIRY DATE · QUANTITY (only)
- Sorted ascending by expiry date
- Smart share: Gmail first if installed, else WhatsApp, then full sheet
- File: `getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)`
- Filename: `Armada_Expiry_[OutletCode]_[YYYY-MM].xlsx`
- In-app past exports list + accessible from device file manager
- **[V4] Auto-backup** triggered after every export (see Section 7)

---

## 11. MONTHLY REMINDER NOTIFICATION

WorkManager `PeriodicWorkRequest`:
- Fires: 18th of every month at 9:00 AM local time (2 days before 20th deadline)
- Title: "⚠️ Expiry Report Reminder"
- Body: "Submit your expiry report before the 20th! Open the app to share."
- Tap → opens app directly to Reports screen
- 100% local, no internet, works even if app is not running
- `BOOT_COMPLETED` receiver to reschedule WorkManager on device restart
- User cannot disable the schedule from within the app

---

## 12. PERFORMANCE BUDGET

| Operation | Target |
|---|---|
| Barcode LRU cache hit | < 1 ms [V4] |
| Barcode Room lookup (cache miss) | < 50 ms |
| App cold start to login screen | < 1.5 s (< 1 s with Baseline Profiles [V4]) |
| Dashboard first paint (25 rows) | < 1 s |
| Scroll frame time | ≤ 16 ms (60 fps) |
| Description dropdown (3,000 items) | < 100 ms to first result |
| OCR date detection (good lighting) | < 3 s |
| Excel export (500 rows) | < 5 s in background |
| Numpad key tap to digit appearing | < 16 ms (instant feel) |
| CSV validation screen render | < 500 ms [V4] |
| Startup health check | < 300 ms [V4] |

### Implementation rules:

- `LazyColumn` / `LazyRow` — never `Column` with `forEach` for lists
- `key {}` in lazy lists for stable diffing
- `remember {}` and `derivedStateOf {}` to avoid unnecessary recompositions
- `const val` for all static strings
- Compressed Armada logo PNG (max 50 KB)
- All DB queries: `Dispatchers.IO`. All UI: `Dispatchers.Main`
- Description dropdown: 150 ms debounce before querying
- **[V4] `Dispatchers.IO` with a dedicated thread pool for POI operations** (not shared with DB pool)
- **[V4] `onTrimMemory(TRIM_MEMORY_RUNNING_CRITICAL)` callback:** clear LRU cache on severe memory pressure
- **[V4] Baseline Profiles:** generate and include a Baseline Profile for the barcode scan → Room lookup → field fill hot path. Reduces JIT compilation on first run by up to 40%.
- **[V4] StrictMode in debug builds:**
```kotlin
if (BuildConfig.DEBUG) {
  StrictMode.setThreadPolicy(
    StrictMode.ThreadPolicy.Builder()
      .detectAll()
      .penaltyLog()
      .penaltyDeath()   // Crash in debug if ANY main-thread IO found
      .build()
  )
}
```
- Profile with Android Studio Profiler before release — fix all jank

---

## 13. SECURITY

- Password `Armd*@2026` stored as BCrypt hash in EncryptedSharedPreferences — never plain text
- **[V4] One-time password:** asked only on first launch after installation. `isAppAuthenticated` flag stored in EncryptedSharedPreferences. All subsequent launches skip login entirely — including after device restart or battery removal.
- **[V4] No session timeout** — removed. App stays open permanently once authenticated.
- **[V4] No "Remember device" checkbox** — removed. Not needed with one-time password.
- **[V4] Invisible password field** — no asterisks, no dots, no characters shown. `TextStyle(color = Color.Transparent)`. No eye toggle.
- No Employee ID, no user accounts, no admin panel — fully standalone
- Local data in app-scoped external storage — not accessible by other apps on non-rooted devices
- **[V4] FileProvider `<paths>` explicitly configured** for the APK export path — missing FileProvider config is a common install-time crash cause. Verify in `AndroidManifest.xml` and `res/xml/file_paths.xml` before Phase 19.
- **[V4] `ActivityNotFoundException` guard for ShareCompat:** wrap all `startActivity(shareIntent)` calls in try-catch. If no app can handle the share intent, show: "No app found to share this file. You can find it in your Documents folder."

---

## 14. CRASH LOGGING & ERROR HANDLING

### Custom CrashHandler (implement exactly as shown):

```kotlin
class ArmadaCrashHandler(private val context: Context) : Thread.UncaughtExceptionHandler {
  private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

  override fun uncaughtException(thread: Thread, throwable: Throwable) {
    try {
      val logFile = File(context.getExternalFilesDir("logs"), "crash_log.txt")
      val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
      val deviceInfo = """
        Timestamp: $timestamp
        Device: ${Build.MANUFACTURER} ${Build.MODEL}
        Android: ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})
        App version: ${BuildConfig.VERSION_NAME}
        Thread: ${thread.name}
        ---STACKTRACE---
        ${throwable.stackTraceToString()}
        ================
      """.trimIndent()
      logFile.appendText(deviceInfo + "\n\n")
    } catch (e: Exception) {
      // silently fail — never cause a crash loop
    }
    defaultHandler?.uncaughtException(thread, throwable)
  }
}
```

Register in `Application.onCreate()`:
```kotlin
Thread.setDefaultUncaughtExceptionHandler(ArmadaCrashHandler(this))
```

Log file: `[ExternalFilesDir]/logs/crash_log.txt` — append, never overwrite.

---

### [V4] Coroutine Exception Handling — Required Pattern

Every `launch {}` block must use a named exception handler:

```kotlin
val dbExceptionHandler = CoroutineExceptionHandler { _, throwable ->
  crashLog(throwable)
  _uiState.update { it.copy(errorMessage = "Database error. Please restart the app.") }
}

viewModelScope.launch(Dispatchers.IO + dbExceptionHandler) {
  // Room operations
}
```

Use `SupervisorJob` at ViewModel scope level so one child coroutine failure does not cancel sibling coroutines:
```kotlin
class EntryViewModel : ViewModel() {
  private val supervisorScope = viewModelScope + SupervisorJob()
  // Use supervisorScope.launch(...) for all coroutines
}
```

---

### [V4] Memory Pressure Handling

```kotlin
override fun onTrimMemory(level: Int) {
  super.onTrimMemory(level)
  if (level >= ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL) {
    (application as ArmadaApp).barcodeCache.clear()
    // Do NOT clear Room — it manages its own connection pool
  }
}
```

---

### [V4] Room Migration Safety

```kotlin
Room.databaseBuilder(context, AppDatabase::class.java, "armada_expiry.db")
  .setJournalMode(JournalMode.WRITE_AHEAD_LOGGING)
  .fallbackToDestructiveMigration()  // For v1 — safe because Room is rebuilt from CSV on demand
  .build()
```

If Room is destructively migrated (rare, but possible after system update): the startup health check (Section 6.1) will detect item count = 0 and prompt the user to re-import master data. Expiry entries are lost in this case — inform the user clearly. This is acceptable for v1; a migration strategy can be added in v2.

---

### General error handling

Every DB, file, camera, OCR, and export operation wrapped in try-catch with user-friendly Snackbar/dialog — never a raw crash shown to user.

- On relaunch after crash: Room data intact → show last session recovery banner
- If Room corrupted: clear message + offer to re-import master data
- Validate every input before Room insert
- **[V4] Camera permission denied:** show "Camera permission is required to scan barcodes. Please enable it in Settings." with a direct link to app permission settings.
- **[V4] File write failure (storage full):** "Not enough storage space to save the export file. Please free up space and try again."

---

## 15. BUILD PHASES (one at a time — compile + run after each, then STOP)

**Phase 1 — Project skeleton**
Kotlin + Jetpack Compose, Hilt, design tokens (Section 5), folder structure, bottom-nav shell (Dashboard / New Entry / Reports / History), top app bar. App opens, correct colors, no crash. **[V4]** StrictMode configured for debug builds.

**Phase 2 — Room database layer**
All entities (including CsvMetadata [V4]), DAOs, database class with WAL mode [V4], Repository, all indexes. No UI yet.

**Phase 3 — CSV import + validation screen**
Bundle ItemList.csv + Outlets.csv + Armada logo in assets/. **[V4]** CSV Validation Screen before every import (Section 10). First launch: background import with progress dialog. Re-import button in Settings. CsvMetadata saved after import [V4]. LRU cache initialized [V4].

**Phase 4 — Login screen + startup health check**
Password screen per Section 6.1. BCrypt in EncryptedSharedPrefs. **[V4]** One-time password — asked only on first launch, never again after. **[V4]** Invisible password field (no asterisks, no dots, no eye toggle). **[V4]** No session timeout. Startup health check runs in background; if already authenticated, skip login entirely and go straight to app. CrashHandler registered.

**Phase 5 — Outlet Details screen (Step 1)**
Searchable dropdowns: Merchandiser, Salesman, Outlet. Auto-fill Outlet Code. Validation. "Next" disabled until complete.

**Phase 6 — Dashboard**
**[V4]** Updated 4 stat cards: EXPIRED / ≤30 DAYS / 31–60 DAYS / 61–90 DAYS. Live Room data, outlet-filtered. Latest 25 records (Paging 3). Status dots. Word-wrap. Master data info strip. Quick search (description + product code [V4]).

**Phase 7 — New Entry screen: fields + numpad + unit toggle**
Step flow indicator. All entry fields. Custom 0–9 + C numpad (always visible, context-aware, haptic per tap). **[V4]** Unit toggle row (PC / OUT / CTN) embedded inside numpad below digit row — full width, 3 equal buttons, PC always default, activates only when QTY focused. Active field green highlight + label. Date auto-format with smart jump. Focus management. "New Entry" button dual-action. Entry table shows QTY + unit combined (12 PC, 6 OUT, 24 CTN). Path C (product code search).

**Phase 8 — Barcode scanner + LRU cache + haptics**
ML Kit barcode scanning. Camera button + FAB. **[V4]** LRU memory cache (100 items) checked before Room. Indexed Room lookup. Auto-fill + auto-jump to date. Haptic + audio feedback. Camera release. Last session recovery banner. Pre-warm cache on startup [V4].

**Phase 9 — Description search dropdown**
Live Room query as user types (description + productCode [V4]). 150 ms debounce. Virtualized lazy dropdown (3,000+ items). Word-wrap in results. Tap to fill all fields + jump to date. Performance: first result < 100 ms.

**Phase 10 — Expiry date OCR + retry button**
ML Kit Text Recognition v2. Scan zone overlay. Continuous frames. Frame pre-processing (contrast, grayscale, edge). All 14 date formats. Confidence indicator (green/yellow/red). **[V4]** OCR Retry Button (Accept / Try Again / Enter Manually). Multi-result selection. Background processing. Camera release.

**Phase 11 — Date picker with color coding**
Calendar date picker. Color-code: red (expired), orange (≤30 days), green (safe). Integration with date field.

**Phase 12 — Validations, dialogs, swipe-delete, undo**
Qty > 20, past date, **[V4]** improved duplicate guard (barcode + outlet + date + merchandiser). Change-warning. Swipe-left delete with 5-second Undo Snackbar. **[V4]** Swipe-delete SupervisorJob crash guard. Long-press edit dialog.

**Phase 13 — Reports screen + cumulative multi-sheet Excel export + WhatsApp text report**
Outlet selector. Pre-share summary card. Report table (4 cols: CODE · DESCRIPTION · EXPIRY · QTY+UNIT, Paging 3, word-wrap, ascending sort). Background Excel build (Apache POI, OOM-guarded). Multi-sheet Excel: one file per merchandiser + salesman + month. One sheet per outlet (sheet name = outlets.shortName). Cumulative file logic: open existing file if it exists this month, rebuild outlet sheet from Room, save. QTY column in Excel shows quantity + unit combined (12 PC, 6 OUT, 24 CTN). File name: `[Merchandiser] – [Salesman] – [Month Year] – Expiry Report.xlsx`. **[V4]** WhatsApp text report: "Share Text Report" button with two options (This Outlet Only / All Outlets). Plain text format: outlet name + items (DESCRIPTION - DD/MM/YY - QTY UNIT), sorted ascending by expiry, shared via standard Android ShareCompat. Smart share for Excel. In-app past exports list. Auto-backup triggered on every export.

**Phase 14 — Stock Report Screen (Low Stock & Out of Stock)**
New StockEntry entity + DAO + Repository. 5th bottom nav tab "Stock". Screen: search bar (150ms debounce, description + barcode), fixed panel (numpad digits 0–9 max spacing + PC/OUT/CTN unit toggle + SHARE button), fixed column headers (OOS · BARCODE · DESCRIPTION · QTY), LazyColumn item list (Paging 3). OOS LED: grey/red toggle per row. QTY field: bright yellow background (#FFF176) when qty entered, hidden when OOS active. Unit applies to focused row. SHARE generates plain text report (LOW STOCK section + OUT OF STOCK section), shared via ShareCompat. Data persists in Room. "Clear All" in hamburger. "No outlet selected" banner.

**Phase 15 — History / Archive Screen**
Outlet selector. Archived records only. 4-column table. Archive action with auto-backup. Export history to Excel.

**Phase 16 — Monthly reminder notification**
WorkManager PeriodicWorkRequest. 18th of month, 9 AM. BOOT_COMPLETED receiver. Tap opens Reports screen.

**Phase 17 — Settings screen**
Master Data info (CsvMetadata display). Re-import button. Backups list. View/share crash log. About.

**Phase 18 — Bulk Quantity Mode + Baseline Profiles**
"Repeat This Item" button + Bulk Mode banner (Section 6.3). Bulk Mode state survives screen orientation change. Baseline Profiles generated for barcode scan hot path.

**Phase 19 — Automated Tests**
Write and run the following tests. All must pass with zero failures before Phase 19.

Room DAO tests: ItemDao (findByBarcode hit + miss, findByProductCode, searchByDescription), OutletDao (searchForDropdown, findByCode), ExpiryEntryDao (insert, getActiveEntriesPaged, all stat count flows), CsvMetadataDao (upsert + retrieve).

CSV import tests: valid files → correct counts in Room; blank barcodes → skipped; duplicate barcodes → first kept; null productCode → inserted as null; corrupted CSV → graceful error no crash; SHORT_NAME > 31 chars → truncated.

Duplicate guard tests: same barcode + outletCode + expiryDate + merchandiser → detected; different merchandiser same item → NOT flagged; different outlet same item → NOT flagged.

Excel export tests: single outlet → correct sheet name (shortName); multiple outlets → correct sheet count; QTY column shows "12 PC" / "6 OUT" / "24 CTN"; filename format correct; cumulative update clears and rebuilds sheet from Room; OOM simulation → graceful error no crash.

Date parsing tests: all 14 formats from Section 9 parse correctly; no-day formats default to last day of month.

Crash recovery tests: force close during Room insert → data intact on relaunch; force close during Excel export → Room intact, partial file discarded; Room version mismatch → fallbackToDestructiveMigration fires, health check prompts re-import.

Low resource tests: low storage → graceful error on export; onTrimMemory CRITICAL → LRU cache cleared no crash; 1,000+ entries → Paging 3 smooth at 60fps; 3,000+ products → description dropdown first result < 100ms.

**Phase 19 — Hardening Pass**
CrashHandler tested. All CoroutineExceptionHandler handlers verified. onTrimMemory LRU cache clearing tested. OOM guard for POI tested. ActivityNotFoundException for ShareCompat tested (uninstall Gmail + WhatsApp first). FileProvider config verified. All try/catch verified. Performance profiled with Android Studio Profiler. Numpad tested: all fields, auto-jump, C key, long-press clear. Description dropdown tested with 3,000 items — no jank. OCR retry tested. Word-wrap on all tables. Session recovery banner tested. StrictMode: zero violations in debug. All Phase 18 automated tests passing.

**Phase 20 — Release Checklist + Signed APK**
Run every item before generating release APK:

Device tests (physical Android phone, 2 GB RAM):
- App installs cleanly from APK
- First launch: CSV validation screen, import succeeds
- Login: password invisible, one-time only, skipped on relaunch
- Startup health check: green pill shows counts
- Outlet Details: all dropdowns work, Next navigates correctly
- Dashboard: 4 stat cards update live
- New Entry: barcode scan → auto-fill → date → qty → NEW ENTRY
- New Entry: description search with 1,197 items — no lag
- New Entry: product code search (Path C) works
- New Entry: PC/OUT/CTN toggle — PC default, resets after save
- New Entry: date numpad 8 taps, auto-format, auto-jump
- New Entry: C key backspace + long press clear
- New Entry: word-wrap on long descriptions
- New Entry: swipe delete + 5-second undo
- New Entry: session recovery banner after force close
- Reports: Excel export builds successfully
- Reports: multi-sheet file — one sheet per outlet
- Reports: QTY shows "12 PC" / "6 OUT" / "24 CTN"
- Reports: cumulative update adds new entries correctly
- Reports: WhatsApp text report — This Outlet Only
- Reports: WhatsApp text report — All Outlets
- History: archive action works
- Notification: monthly reminder fires (simulate 18th)
- Settings: master data info correct
- Settings: re-import works with updated CSV
- Settings: crash log viewable and shareable

Edge case tests:
- No internet — all functions work offline
- Camera permission denied — graceful message + Settings link
- Storage full — graceful error on export
- Gmail not installed — WhatsApp suggested, no crash
- WhatsApp not installed — full share sheet, no crash
- Force close during entry — data intact on relaunch
- Phone restart after entries — all data intact
- 1,000+ entries — smooth scrolling at 60fps
- 3,000+ products — dropdown first result < 100ms
- Corrupted CSV — validation screen shows errors, no crash
- Wrong password — shake animation, field clears, stays invisible
- Qty > 20 — confirmation dialog
- Past expiry date — confirmation dialog
- Duplicate entry — merge dialog
- OCR on faded date — retry button works
- Bulk mode — Repeat This Item pre-fills correctly

Once ALL checklist items pass: generate signed release APK, ProGuard/R8 rules for Apache POI + ML Kit verified, Baseline Profiles included, README updated.

---

## 16. DATA FILES — COLUMN MAPPING

**Source format: CSV (comma-separated values)**
The bundled master data files are CSV. They are imported into Room on first launch. The app never reads CSV at runtime — only Room.

**ItemList.csv:**
```
BARCODE       → items.barcode       (indexed, unique)
DESCRIPTION   → items.description   (indexed)
CODE          → items.productCode   (6 rows null — acceptable)
```

**Outlets.csv — [V4] THREE columns:**
```
CODE          → outlets.outletCode
CUSTOMER      → outlets.outletName
SHORT_NAME    → outlets.shortName   ← [V4] NEW — Excel sheet tab name, max 31 characters
```

Example Outlets.csv:
```
CODE,CUSTOMER,SHORT_NAME
AD001,CARREFOUR KHALIDIYAH MALL,CARREFOUR KHALIDIYAH
AD002,LULU HYPERMARKET MUSHRIF,LULU MUSHRIF
AD003,SPINNEYS KHALIDIYAH,SPINNEYS KHALIDIYAH
```

**SHORT_NAME rules:**
- Maximum 31 characters (Excel sheet tab limit — enforce in import, truncate if longer)
- Must be unique per outlet — no two outlets can share the same short name
- Used only as the Excel sheet tab name — never shown in the app UI
- If SHORT_NAME is blank for any row: use the first 31 characters of CUSTOMER as fallback

Current counts: **1,197 products** (clean, deduplicated), **282 outlets**.

---

## 17. PERFECT ENTRY FLOW SUMMARY (target: 3–5 taps per item)

**Standard flow:**

① Point camera at barcode → item fills automatically (0 taps) → vibrate + beep → focus jumps to DATE

OR: tap description field → type partial name → tap result from dropdown → all fields fill → focus jumps to DATE

OR: tap product code field → type code → Room lookup → fill → focus jumps to DATE [V4]

OR: tap barcode field → type digits on numpad → tap [NEXT] → fill → focus jumps to DATE

② Tap OCR → point at expiry date → auto-fill (0–2 taps incl. potential retry [V4])

OR: numpad digits for date (8 taps, auto-formatted)

OR: tap 📅 calendar picker

③ Tap numpad digits for quantity (1–2 taps)

④ Tap "NEW ENTRY" (green) = SAVE + CLEAR + READY FOR NEXT

**[V4] Bulk flow (same item, multiple expiry dates):**

① Scan barcode once → fills item

② Enter date + qty → tap "NEW ENTRY"

③ Tap "🔁 Repeat This Item" → item pre-filled, focus jumps to DATE

④ Enter new date + qty → tap "NEW ENTRY" → repeat from step ③

No re-scanning needed. Speed up of 50%+ for shelf audits with multiple dates.

---

## 18. DECISIONS LOG (V3 → V4)

| Item | V4 Change | Reason |
|---|---|---|
| One-time password | Asked only on first launch after install, never again | Merchandisers should not be slowed down by login every time |
| Invisible password field | No asterisks, no dots, no eye toggle — field stays blank while typing | Nobody watching can see character count or input |
| Session timeout | Removed entirely | Not needed with one-time password |
| Remember device checkbox | Removed | Not needed with one-time password |
| LRU cache | 100-item memory cache for barcodes | Re-scanned items return in < 1 ms |
| Baseline Profiles | Added | Up to 40% faster cold start |
| StrictMode | Debug builds | Catch any main-thread IO before release |
| Dashboard categories | Updated to EXPIRED / ≤30 / 31–60 / 61–90 | Aligns with FMCG retail practice |
| Startup health check | Added | Prevents mysterious failures from missing master data |
| CSV Validation Screen | Added before every import | Prevents corrupt data silently entering Room |
| CSV Version Tracking | CsvMetadata entity | Instantly identifies outdated master files |
| Product Code search | Path C on New Entry, also in dashboard search | Many merchandisers know codes not barcodes |
| Bulk Quantity Mode | "Repeat This Item" button | 50%+ speed boost for multi-date shelf audits |
| Improved duplicate guard | Now includes merchandiser field | Avoids conflicts when multiple people use same device |
| OCR Retry Button | Accept / Try Again / Enter Manually | Eliminates binary scan-or-fail flow |
| Auto-backup | Triggered on export, archive, and re-import | Protects against accidental data loss |
| OOM guard for POI | try/catch/finally with `OutOfMemoryError` | Prevents crash on low-RAM devices during export |
| SupervisorJob | All ViewModels use SupervisorJob | One coroutine failure doesn't kill sibling operations |
| CoroutineExceptionHandler | Required on every launch {} | No silent coroutine failures |
| onTrimMemory | Clears LRU cache on critical memory pressure | Prevents OOM on very low-RAM devices |
| FileProvider guard | Explicitly verified in Phase 18 | Missing config is a common install-time crash cause |
| ActivityNotFoundException | ShareCompat wrapped in try-catch | Graceful handling if no Gmail/WhatsApp installed |
| Room migration | `fallbackToDestructiveMigration()` + health check recovery | Safe for v1 — user can re-import master data |
| Settings screen | Dedicated screen via hamburger | Central hub for master data, backups, crash log |
| Numpad haptic per tap | Micro-vibration on every numpad key press | Tactile confirmation in noisy retail environments |
| Build phases | 17 → 19 phases | More granular; Bulk Mode and hardening are separate phases |

---

*End of Master Build Prompt V4 — Armada Expiry Tracking App*
*Prepared for: Armada Distribution, Abu Dhabi & Al Ain, UAE*
*Build tool: Cursor / Claude Code*
*Target: Production-grade Android APK*
