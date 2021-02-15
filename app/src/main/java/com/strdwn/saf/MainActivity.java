package com.strdwn.saf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import android.os.Bundle;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestMainFolderAccess("Aashit", new StorageCallbacks() {
            @Override
            public void onFolderAccessGranted(DocumentFile folder) {
                folder.createDirectory("Aashit"+System.currentTimeMillis());
                //This folder will give u access to your main folder. Do everything in this folder only.
            }
        });
    }
}