package com.oblivion.data

import android.content.Context
import java.security.MessageDigest

class AuthRepository(context: Context) {
    private val prefs = context.getSharedPreferences("oblivion_auth", Context.MODE_PRIVATE)

    fun setMasterPin(pin: String) {
        val hashed = sha256(pin)
        prefs.edit().putString("master_pin", hashed).apply()
    }

    fun verifyPin(input: String): Boolean {
        val stored = prefs.getString("master_pin", null) ?: return false
        return sha256(input) == stored
    }

    // NEW: Checks if this is the first launch
    fun isPinSet(): Boolean {
        return prefs.contains("master_pin")
    }

    private fun sha256(input: String): String {
        return MessageDigest.getInstance("SHA-256")
            .digest(input.toByteArray())
            .fold("") { str, it -> str + "%02x".format(it) }
    }
}