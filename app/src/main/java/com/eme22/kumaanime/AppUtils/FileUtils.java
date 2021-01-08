package com.eme22.kumaanime.AppUtils;

import android.content.Context;

import com.eme22.kumaanime.App;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtils {

    public static void writeFileOnInternalStorage(String sFileName, String sBody) throws IOException {
        try {
            FileOutputStream outputStream = App.getInstance().openFileOutput(sFileName, Context.MODE_PRIVATE);
            outputStream.write(sBody.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FileInputStream inputStream = App.getInstance().openFileInput(sFileName);
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            r.close();
            inputStream.close();
            //Log.d("File", "File contents: " + total);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
