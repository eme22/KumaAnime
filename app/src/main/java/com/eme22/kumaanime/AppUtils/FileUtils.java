package com.eme22.kumaanime.AppUtils;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.eme22.kumaanime.App;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileUtils {

    public static boolean moveFile(File src, File targetFile) throws IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!src.renameTo(targetFile)) {
                // If rename fails we must do a true deep copy instead.
                Path sourcePath = src.toPath();
                Path targetDirPath = targetFile.toPath();
                try {
                    Files.move(sourcePath, targetDirPath, StandardCopyOption.REPLACE_EXISTING);
                    return true;
                } catch (IOException ex) {
                    Log.e("DATA","Failed to move " + src + " to " + targetFile + " - " + ex.getMessage(),ex);
                    return false;
                }
            }
            else return true;
        } else {
            if (src.exists()) {
                boolean renamed = src.renameTo(targetFile);
                Log.d("TAG", "renamed: " + renamed);
                return renamed;
            }
            return false;
        }
    }

    public static boolean deleteRecursive(File fileOrDirectory) {
        Log.d("DELETING", fileOrDirectory.getPath());
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        return fileOrDirectory.delete();
    }

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
