package com.eme22.kumaanime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.SparseArray;

public class PermissionActivity extends AppCompatActivity {

    public interface OnPermissionCallback{
        void requestResult(String[] permissions, int[] grantResults);
    }

    private SparseArray<OnPermissionCallback> permissionCallback = new SparseArray<>();

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        permissionCallback.get(requestCode).requestResult(permissions, grantResults);
    }

    public void addPermissionCallback(int requestCode, OnPermissionCallback  callback){
        permissionCallback.put(requestCode, callback);
    }
}