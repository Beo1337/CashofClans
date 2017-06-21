package com.cashify.password;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.content.ContentValues.TAG;

/**
 * Created by mhackl on 02.06.2017.
 */

public class PasswordFiler {

    private static final String filename = "passwd";
    private Context context;
    private final MessageDigest sha256;


    public PasswordFiler(Context context) throws NoSuchAlgorithmException {
        this.context = context;
        this.sha256 = MessageDigest.getInstance("SHA-256");
    }

    public boolean setPassword(String p) {
        Log.i(TAG, "setPassword: " + p);
        byte[] hash = sha256.digest(p.getBytes());
        return writeFile(hash);
    }

    public boolean clearPassword() {
        Log.i(TAG, "clearPassword");
        return context.deleteFile(filename);
    }

    public boolean hasSetPassword() {
        File f = context.getFileStreamPath(filename);
        Log.i(TAG, "hasSetPassword: " + (f != null && f.exists()));
        return f != null && f.exists();
    }

    public boolean checkPassword(String p)
    {
        byte[] hashFile = readFile();
        byte[] hashInput = sha256.digest(p.getBytes());

        Log.i(TAG, "checkPassword file: " + arrayToHex(hashFile));
        Log.i(TAG, "checkPassword pass: " + arrayToHex(hashInput));

        if (hashFile.length != hashInput.length) return false;

        for(int i = 0; i < hashFile.length; i++) {
            if (hashFile[i] != hashInput[i]) return false;
        }

        return true;
    }


    private boolean writeFile(byte[] c) {
        Log.i(TAG, "writeFile");
        try {
            context.deleteFile(filename);
            FileOutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(c);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private byte[] readFile() {
        Log.i(TAG, "readFile");
        byte[] c = new byte[]{
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        };

        try {
            FileInputStream inputStream = context.openFileInput(filename);
            int readCount = inputStream.read(c);
            Log.i(TAG, "readFile: " + readCount + " bytes read");
            inputStream.close();
            return c;
        } catch (Exception e) {
            return new byte[]{};
        }
    }

    private String arrayToHex(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
