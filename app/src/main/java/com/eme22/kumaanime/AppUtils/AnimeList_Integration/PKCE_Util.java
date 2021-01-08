package com.eme22.kumaanime.AppUtils.AnimeList_Integration;

import org.apache.commons.lang3.RandomStringUtils;

public class PKCE_Util {

    private final String CODE_VERIFIER_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-._~";
    public String generateVerifier(int length){
        return RandomStringUtils.random(length, CODE_VERIFIER_STRING);
    }
}
