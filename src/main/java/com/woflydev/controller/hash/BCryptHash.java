package com.woflydev.controller.hash;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * Simple wrapper for the PatrickFav's BCrypt hashing library.
 * <p>
 * Uses 12 rounds of hashing.
 */
public class BCryptHash {
    public static String hashString(String s) {
        return BCrypt.withDefaults().hashToString(12, s.toCharArray());
    }

    public static boolean verifyHash(String pass, String hash) {
        BCrypt.Result result = BCrypt.verifyer().verify(pass.toCharArray(), hash);
        return result.verified;
    }
}
