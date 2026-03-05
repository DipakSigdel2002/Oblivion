package com.oblivion.data

import android.content.Context
import org.bouncycastle.crypto.generators.Argon2BytesGenerator
import org.bouncycastle.crypto.params.Argon2Parameters
import java.security.SecureRandom

class AuthVault(context: Context) {
    private val prefs = context.getSharedPreferences("oblivion_vault", Context.MODE_PRIVATE)

    fun setupVault(pin: String) {
        val salt = ByteArray(16).also { SecureRandom().nextBytes(it) }
        val hash = hashPin(pin, salt)
        
        prefs.edit()
            .putString("pin_hash", hash.encodeBase64())
            .putString("pin_salt", salt.encodeBase64())
            .apply()
    }

    private fun hashPin(pin: String, salt: ByteArray): ByteArray {
        val params = Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
            .withIterations(3)
            .withMemoryAsKB(65536)
            .withParallelism(1)
            .withSalt(salt)
            .build()
        val generator = Argon2BytesGenerator()
        generator.init(params)
        val result = ByteArray(32)
        generator.generateBytes(pin.toByteArray(), result)
        return result
    }

    private fun ByteArray.encodeBase64() = android.util.Base64.encodeToString(this, android.util.Base64.DEFAULT)
}