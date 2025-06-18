package com.deeraj.rssfeedapp.db

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import java.util.UUID

object SecureDatabaseManager {

    private const val PREF_FILE_NAME = "secure_prefs"
    private const val PASSPHRASE_KEY = "db_passphrase"

    fun getSecureDatabaseFactory(context: Context): SupportFactory {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        val sharedPreferences = EncryptedSharedPreferences.create(
            PREF_FILE_NAME,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        var passphraseString = sharedPreferences.getString(PASSPHRASE_KEY, null)

        if (passphraseString == null) {
            passphraseString = UUID.randomUUID().toString()
            sharedPreferences.edit()
                .putString(PASSPHRASE_KEY, passphraseString)
                .apply()

        }

        val passphraseBytes = SQLiteDatabase.getBytes(passphraseString.toCharArray())
        return SupportFactory(passphraseBytes)
    }
}

