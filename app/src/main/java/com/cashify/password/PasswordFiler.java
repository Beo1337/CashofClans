package com.cashify.password;

import android.content.Context;
import android.util.Log;

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
        byte[] hash = sha256.digest(p.getBytes());
        return writeFile(hash);
    }

    public boolean clearPassword(String p) {
        return writeFile(new byte[]{});
    }

    public boolean hasSetPassword() {
        return readFile().length != 0;
    } // TODO: sha256 has fixed length

    public boolean checkPassword(String p) {
        byte[] hashFile = readFile();
        byte[] hashInput = p.getBytes();

        if (hashFile.length != hashInput.length) return false;
        for (int i = 0; i < hashFile.length; i++) if (hashFile[i] != hashInput[i]) return false;
        return true;
    }


    private boolean writeFile(byte[] c) {
        try {
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
        byte[] c = new byte[]{
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
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
}
