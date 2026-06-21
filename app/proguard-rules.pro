# Armada Expiry App ProGuard rules
# Full rules for Apache POI, ML Kit, etc. will be added in Phase 19

# Keep Hilt generated components
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

# Keep data classes used by Room (added in Phase 2)
-keep class com.armada.expiryapp.data.model.** { *; }

# Keep crash handler
-keep class com.armada.expiryapp.util.ArmadaCrashHandler { *; }
