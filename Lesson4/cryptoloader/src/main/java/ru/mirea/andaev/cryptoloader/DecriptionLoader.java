package ru.mirea.andaev.cryptoloader;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DecriptionLoader extends AsyncTaskLoader<String> {
    public static final String ARG_WORD = "word";
    public static final String ARG_KEY = "banana";
    byte[] ciphered_text;
    SecretKey key;

    public DecriptionLoader(@NonNull Context context, Bundle args) {
        super(context);
        if (args != null){
            byte[] deciphered_key = args.getByteArray(ARG_KEY);
            key = new SecretKeySpec(deciphered_key,0,deciphered_key.length,"AES");
            ciphered_text = args.getByteArray(ARG_WORD);
        }
    }
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
    @Nullable
    @Override
    public String loadInBackground() {
        SystemClock.sleep(5000);
        return decryptMsg(ciphered_text,key);
    }

    public static String decryptMsg(byte[] cipherText, SecretKey secret){
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secret);
            return new String(cipher.doFinal(cipherText));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
                 | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
