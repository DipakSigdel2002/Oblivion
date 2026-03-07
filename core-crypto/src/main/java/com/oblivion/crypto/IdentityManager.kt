package com.oblivion.crypto

import org.bitcoinj.wallet.DeterministicSeed
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters
import java.security.SecureRandom

object IdentityManager {

    /**
     * Generates a new BIP39 Mnemonic and derives the Identity Key.
     * This is the "Root of Trust" for the user.
     */
    fun generateNewIdentity(): IdentityResult {
        // 1. Generate Mnemonic (128 bits = 12 words) directly via SecureRandom
        val seed = DeterministicSeed(SecureRandom(), 128, "", System.currentTimeMillis() / 1000L)
        val mnemonic = seed.mnemonicCode?.joinToString(" ") ?: ""

        // 2. Generate Ed25519 Keypair
        val generator = Ed25519KeyPairGenerator()
        generator.init(Ed25519KeyGenerationParameters(SecureRandom()))
        val keyPair = generator.generateKeyPair()

        return IdentityResult(mnemonic, keyPair)
    }

    data class IdentityResult(
        val mnemonic: String, 
        val keyPair: org.bouncycastle.crypto.AsymmetricCipherKeyPair
    )
}