package com.oblivion.crypto

import org.bitcoinj.wallet.DeterministicSeed
import java.security.SecureRandom

object IdentityManager {

    /**
     * Generates a new 12-word Mnemonic.
     * This is the "Security Code" user must save to restore their address.
     */
    fun createMnemonic(): String {
        val seedBytes = ByteArray(16)
        SecureRandom().nextBytes(seedBytes)
        val seed = DeterministicSeed(seedBytes, null, "", System.currentTimeMillis())
        return seed.mnemonicCode?.joinToString(" ") ?: ""
    }

    // TODO: Phase 2 will implement Ed25519 + Tor v3 address derivation from this Mnemonic
}