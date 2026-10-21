package cn.afeibaili.mchat.cipher

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * 密码工具类
 *
 *@author AfeiBaili
 *@version 2025/11/3 14:37
 */

class CipherProcessor(token: String) {
    private val keySpec = SecretKeySpec(
        MessageDigest.getInstance("sha-256").digest(token.toByteArray()), "AES"
    )

    private val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")

    fun encryptToString(content: String): String {
        return Base64.getEncoder().encodeToString(encrypt(content.toByteArray(Charsets.UTF_8)))
    }

    fun decryptToString(encrypted: String): String {
        return String(
            decrypt(Base64.getDecoder().decode(encrypted)),
            StandardCharsets.UTF_8
        )
    }

    fun encrypt(array: ByteArray): ByteArray {
        synchronized(this) {
            cipher.init(Cipher.ENCRYPT_MODE, keySpec)
            return cipher.doFinal(array)
        }
    }

    fun decrypt(array: ByteArray): ByteArray {
        synchronized(this) {
            cipher.init(Cipher.DECRYPT_MODE, keySpec)
            return cipher.doFinal(array)
        }
    }
}