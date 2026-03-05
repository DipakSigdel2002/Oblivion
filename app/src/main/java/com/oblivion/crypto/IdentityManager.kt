package com.oblivion.crypto

import java.security.SecureRandom
import org.whispersystems.libsignal.IdentityKeyPair
import org.whispersystems.libsignal.util.KeyHelper

/**
 * Oblivion Identity: No username, no password.
 * Just a generated Ed25519 keypair.
 */
object IdentityManager {
    
    fun generateNewIdentity(): IdentityKeyPair {
        // This generates a unique identity for this device
        return KeyHelper.generateIdentityKeyPair()
    }

    fun getOnionAddress(identityKeyPair: IdentityKeyPair): String {
        // In a real scenario, we derive a .onion address from the public key
        val publicKey = identityKeyPair.publicKey.serialize()
        return android.util.Base64.encodeToString(publicKey.take(10).toByteArray(), android.util.Base64.NO_WRAP) + ".onion"
    }
}