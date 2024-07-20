package com.woflydev.controller.hash;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class BCryptHash {
    public static String hashString(String s) {
        return BCrypt.withDefaults().hashToString(12, s.toCharArray());
    }

    public static boolean verifyHash(String pass, String hash) {
        BCrypt.Result result = BCrypt.verifyer().verify(pass.toCharArray(), hash);
        return result.verified;
    }
}
