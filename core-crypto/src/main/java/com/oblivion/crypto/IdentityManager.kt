package com.oblivion.crypto

import org.bitcoinj.wallet.DeterministicSeed
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters
import java.security.SecureRandom

object IdentityManager {

    fun generateNewIdentity(): IdentityResult {
        // Generate Mnemonic: Using the simple constructor
        val seedBytes = ByteArray(16)
        SecureRandom().nextBytes(seedBytes)
        // Simplified constructor
        val seed = DeterministicSeed(seedBytes, "", System.currentTimeMillis())
        val mnemonic = seed.mnemonicCode?.joinToString(" ") ?: ""

        val generator = Ed25519KeyPairGenerator()
        generator.init(Ed25519KeyGenerationParameters(SecureRandom()))
        val keyPair = generator.generateKeyPair()

        return IdentityResult(mnemonic, keyPair)
    }

    data class IdentityResult(val mnemonic: String, val keyPair: org.bouncycastle.crypto.AsymmetricCipherKeyPair)
}