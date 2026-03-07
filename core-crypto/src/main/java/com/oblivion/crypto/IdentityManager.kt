package com.oblivion.crypto

import org.bitcoinj.crypto.MnemonicCode
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters
import java.security.SecureRandom

object IdentityManager {

    /**
     * Generates a new BIP39 Mnemonic and derives the Identity Key.
     * This is the "Root of Trust" for the user.
     */
    fun generateNewIdentity(): IdentityResult {
        // 1. Generate 16 bytes of pure entropy (128 bits = 12 words)
        val entropy = ByteArray(16)
        SecureRandom().nextBytes(entropy)
        
        // 2. Convert entropy directly to BIP39 words
        val mnemonicWords = MnemonicCode.INSTANCE.toMnemonic(entropy)
        val mnemonic = mnemonicWords.joinToString(" ")

        // 3. Generate Ed25519 Keypair (For Tor v3 routing and Identity)
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