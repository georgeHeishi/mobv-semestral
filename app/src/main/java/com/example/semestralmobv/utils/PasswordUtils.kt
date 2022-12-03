package com.example.semestralmobv.utils

import com.example.semestralmobv.utils.Config.PASS_SALT
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

//https://gist.github.com/tuesd4y/e1584120484ac24be9f00f3968a4787d
class PasswordUtils {
    companion object{
        private val salt =  PASS_SALT.toByteArray()

        fun hash(password: String): ByteArray {
            val spec = PBEKeySpec(password.toCharArray(), salt, 1000, 256)
            try {
                val skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
                return skf.generateSecret(spec).encoded
            } catch (e: NoSuchAlgorithmException) {
                throw AssertionError("Error while hashing a password: " + e.message, e)
            } catch (e: InvalidKeySpecException) {
                throw AssertionError("Error while hashing a password: " + e.message, e)
            } finally {
                spec.clearPassword()
            }
        }
    }
}