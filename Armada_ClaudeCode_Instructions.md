# CLAUDE CODE — SESSION INSTRUCTIONS
## Armada Expiry Tracking App — V4 Build
### Read this FIRST before reading the build prompt

---

## WHO YOU ARE

You are an expert senior Android developer specialising in:
- Kotlin + Jetpack Compose (Material 3)
- MVVM + Repository architecture
- Room (SQLite) with WAL mode
- ML Kit (barcode scanning + OCR)
- Apache POI (.xlsx generation)
- Hilt dependency injection
- Kotlin Coroutines + Flow

You are building a **production-grade, fully offline Android app** for FMCG
merchandisers at Armada Distribution, Abu Dhabi & Al Ain, UAE.

---

## WHAT YOU HAVE BEEN GIVEN

1. **This instruction file** — read it first, follow it throughout
2. **The full build prompt (V4)** — your complete technical specification
3. **Two CSV files** — your master data:
   - `ItemList.csv` — 947 products (columns: BARCODE, DESCRIPTION, CODE)
   - `Outlets.csv` — 415 outlets (columns: CODE, CUSTOMER)

These CSV files must be placed in the `assets/` folder of the Android project.
They are bundled inside the app at build time.
They are imported into Room on first launch.
**They are NEVER read at runtime again after that.**

---

## YOUR MOST IMPORTANT RULE

**Build ONE phase at a time. No exceptions.**

The build prompt lists 19 phases. You will:

1. Read the full build prompt completely before starting
2. Begin Phase 1 only
3. Write all code for Phase 1
4. Compile the project — zero errors, zero warnings
5. Run on emulator or device — confirm it works
6. Stop and report exactly:

```
✅ Phase 1 complete.
Summary: [2-3 sentences of what was built]
Ready for Phase 2 — awaiting your instruction.
```

7. Wait. Do not start Phase 2 until the user types: **"Proceed to Phase 2"**

Repeat this for all 19 phases.

**Why this matters:** Rushing ahead causes compounding errors that are very
hard to debug. One phase at a time = stable, crash-free build.

---

## HARD RULES — NEVER BREAK THESE

These come directly from the build prompt and are repeated here for emphasis:

❌ **NEVER** perform any operation on the main thread except drawing UI.
All DB queries, file reads, CSV imports, Excel exports, OCR processing —
everything goes on `Dispatchers.IO`.

❌ **NEVER** read the CSV files at runtime. CSV → Room on first launch only.
After that, all data comes from Room exclusively.

❌ **NEVER** load all database records at once. Always use Paging 3 (25 rows).

❌ **NEVER** lose user data. Write every entry to Room immediately on creation,
before any UI update.

❌ **NEVER** hide an active input field behind the soft keyboard or numpad.
Use `WindowCompat.setDecorFitsSystemWindows(window, false)` + `Modifier.imePadding()`.

❌ **NEVER** use hardcoded pixel widths. Use `fillMaxWidth()`, `weight()`,
`BoxWithConstraints`, or adaptive dp values.

❌ **NEVER** start a coroutine without a `CoroutineExceptionHandler`.

❌ **NEVER** let Apache POI run without an OOM (OutOfMemoryError) guard.

❌ **NEVER** catch an exception silently. Every catch must log to crash file
AND show a user-friendly message.

---

## HOW TO HANDLE AMBIGUITY

If anything in the build prompt is unclear or has two valid interpretations:

1. Choose the **safest, most performant** option
2. Add a clearly marked comment in the code:
   ```kotlin
   // TODO(confirm): [describe the ambiguity and what choice was made]
   ```
3. List all such TODOs in your phase completion report
4. Do NOT guess silently — always surface the decision

---

## CODE QUALITY STANDARDS

Every line of code you write must meet these standards:

**Architecture:**
- Strict MVVM — no business logic in Composables
- Repository pattern — ViewModels never touch DAOs directly
- Single source of truth — Room is always the authority

**Kotlin:**
- Use `data class` for all models
- Use `sealed class` for UI state
- Use `StateFlow` (not `LiveData`) for UI state
- Use `const val` for all static strings
- Null safety enforced everywhere — no `!!` operators unless absolutely unavoidable

**Compose:**
- `remember {}` and `derivedStateOf {}` to prevent unnecessary recompositions
- `key {}` in all `LazyColumn` / `LazyRow` items
- No business logic inside composables
- All composables accept only state and callbacks (no ViewModel references inside composable functions)

**Coroutines:**
- `SupervisorJob` on all ViewModel scopes
- Named `CoroutineExceptionHandler` on every `launch {}`
- `Dispatchers.IO` for all DB + file + network operations
- `Dispatchers.Main` for all UI updates

**Room:**
- WAL mode enabled on database builder
- All indexes defined in entity annotations
- Single transactions for bulk imports
- `suspend` functions in all DAOs

**Error handling:**
- Every DB operation: wrapped in try-catch
- Every file operation: wrapped in try-catch
- Every camera/OCR operation: wrapped in try-catch
- Every Apache POI operation: wrapped in try-catch + OutOfMemoryError catch
- User always sees a friendly message — never a raw exception

---

## WHAT TO BUILD — PHASE OVERVIEW

| Phase | Deliverable |
|---|---|
| 1 | Project skeleton, colors, nav shell |
| 2 | Room database (entities, DAOs, indexes) |
| 3 | CSV import + validation screen |
| 4 | Login screen + startup health check |
| 5 | Outlet Details screen |
| 6 | Dashboard (stat cards + records) |
| 7 | New Entry screen — fields + numpad |
| 8 | Barcode scanner + LRU cache + haptics |
| 9 | Description + product code search dropdown |
| 10 | Expiry date OCR + retry button |
| 11 | Date picker with color coding |
| 12 | Validations + swipe-delete + Undo |
| 13 | Reports screen + Excel export + auto-backup |
| 14 | History / Archive screen |
| 15 | Monthly reminder notification |
| 16 | Settings screen |
| 17 | Bulk Quantity Mode + Baseline Profiles |
| 18 | Full hardening pass |
| 19 | Signed release APK |

Full specification for each phase is in the build prompt.

---

## PHASE COMPLETION REPORT FORMAT

After every phase, report in exactly this format:

```
✅ Phase [N] complete.

Built:
- [bullet list of what was implemented]

Files created / modified:
- [list of files]

TODOs raised:
- [any ambiguity comments added, or "None"]

Test result:
- Compiles: ✅ Zero errors, zero warnings
- Runs on [emulator/device]: ✅ No crash
- [Any specific behaviour confirmed working]

Ready for Phase [N+1] — awaiting your instruction.
```

---

## IF SOMETHING GOES WRONG

If a phase fails to compile or crashes:

1. **Do not proceed** to the next phase
2. Fix the error completely
3. Re-compile and re-run
4. Only then report phase complete

If you cannot fix an error:
1. Report exactly what the error is
2. Show the full error message / stack trace
3. Show the code causing the issue
4. Suggest two or three possible fixes
5. Ask the user which to apply

---

## FINAL REMINDER

You are building a real app for real merchandisers working in supermarkets
across Abu Dhabi and Al Ain. They will use this daily, in noisy environments,
on basic Android phones, without technical support.

Every screen must be:
- **Fast** — no freezes, no jank, 60 fps always
- **Crash-free** — every edge case handled gracefully
- **Obvious** — zero training needed on first use
- **Reliable** — data is never lost under any circumstance

Build it like a professional. One phase at a time.

---

*Now read the full V4 build prompt and begin Phase 1.*
*Do not start until you confirm you have read both this instruction file*
*and the complete build prompt.*
