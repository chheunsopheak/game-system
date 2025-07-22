package util

import constant.AESConstant.AES_IV
import constant.AESConstant.AES_KEY
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


object AESUtil {
    fun encrypt(text: String): String {

        val secretKey = SecretKeySpec(AES_KEY.toByteArray(Charsets.UTF_8).copyOf(32), "AES")
        val ivSpec = IvParameterSpec(AES_IV.toByteArray(Charsets.UTF_8))

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
        val encryptedBytes = cipher.doFinal(text.toByteArray(Charsets.UTF_8))
        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

    fun encrypt(text: String, secret: String?, vector: String?): String {
        val secretKey = SecretKeySpec(
            (secret ?: AES_KEY).toByteArray(Charsets.UTF_8).copyOf(32),
            "AES"
        )
        val ivSpec = IvParameterSpec((vector ?: AES_IV).toByteArray(Charsets.UTF_8).copyOf(16))
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)

        val encryptedBytes = cipher.doFinal(text.toByteArray(Charsets.UTF_8))
        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

    fun decrypt(text: String, aesKey: String, aesIV: String): String {
        val secretKey = SecretKeySpec(aesKey.toByteArray(Charsets.UTF_8).copyOf(32), "AES")
        val ivSpec = IvParameterSpec(aesIV.toByteArray(Charsets.UTF_8))
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)

        val decodedBytes = Base64.getDecoder().decode(text)
        val decryptedBytes = cipher.doFinal(decodedBytes)
        return String(decryptedBytes, Charsets.UTF_8)
    }

}