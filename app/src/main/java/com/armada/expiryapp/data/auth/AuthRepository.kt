package com.armada.expiryapp.data.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import at.favre.lib.crypto.bcrypt.BCrypt
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    companion object {
        private const val PREFS_NAME          = "armada_auth"
        private const val KEY_IS_AUTHENTICATED = "is_authenticated"
        private const val KEY_PASSWORD_HASH   = "password_hash"
        private const val CORRECT_PASSWORD    = "Armd*@2026"
    }

    // Lazy: EncryptedSharedPreferences performs KeyStore I/O on first access.
    // Always accessed from Dispatchers.IO, so the synchronised lazy default is safe.
    private val prefs: SharedPreferences by lazy {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        EncryptedSharedPreferences.create(
            PREFS_NAME,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    suspend fun isAuthenticated(): Boolean = withContext(Dispatchers.IO) {
        runCatching { prefs.getBoolean(KEY_IS_AUTHENTICATED, false) }.getOrDefault(false)
    }

    suspend fun setAuthenticated() = withContext(Dispatchers.IO) {
        runCatching { prefs.edit().putBoolean(KEY_IS_AUTHENTICATED, true).apply() }
    }

    suspend fun clearAuthentication() = withContext(Dispatchers.IO) {
        runCatching { prefs.edit().remove(KEY_IS_AUTHENTICATED).apply() }
    }

    suspend fun verifyPassword(input: String): Boolean = withContext(Dispatchers.IO) {
        runCatching {
            val hash = getOrCreateHash()
            BCrypt.verifyer().verify(input.toCharArray(), hash.toCharArray()).verified
        }.getOrDefault(false)
    }

    // Computes and stores the BCrypt hash of the correct password on first call.
    // Subsequent calls return the stored hash. BCrypt cost 10 (~100ms on device).
    private fun getOrCreateHash(): String {
        val stored = prefs.getString(KEY_PASSWORD_HASH, null)
        if (stored != null) return stored
        val hash = BCrypt.withDefaults().hashToString(10, CORRECT_PASSWORD.toCharArray())
        prefs.edit().putString(KEY_PASSWORD_HASH, hash).apply()
        return hash
    }
}
