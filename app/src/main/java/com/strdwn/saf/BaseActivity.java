package com.strdwn.saf;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public abstract class BaseActivity extends AppCompatActivity {

   SharedPreferences sharedPreferences;

   private static String FOLDER_URI = "FOLDER_URI";
   private static String GIVEN_SCOPE = "GIVEN_SCOPE";
    private final int REQUEST_CODE_PATH_TO_DATA = 54;
    private final int STORAGE_PERMISSION = 32;
    EasyPermissions.PermissionCallbacks permissionCallbacks;
    String requestName;
    StorageCallbacks callbacks;

   public interface StorageCallbacks{
       void onFolderAccessGranted(DocumentFile folder);
   }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);

        permissionCallbacks = new EasyPermissions.PermissionCallbacks() {
            @Override
            public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
                selectIfHavePermission();
            }

            @Override
            public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
                if (EasyPermissions.somePermissionPermanentlyDenied(BaseActivity.this, perms)) {
                    AppSettingsDialog.Builder builder = new AppSettingsDialog.Builder(BaseActivity.this);
                    builder.build().show();
                } else {
                    Log.e("onPermissionsDenied", "hello else");
                    EasyPermissions.requestPermissions(BaseActivity.this, "Grant permission to use app!", STORAGE_PERMISSION, WRITE_EXTERNAL_STORAGE);
                }
            }

            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

            }
        };
        
    }

    public boolean checkStorage(){
        return sharedPreferences.contains("FOLDER_URI");
    }

    public void requestMainFolderAccess(String name,StorageCallbacks callbacks){
       if (checkStorage()){
//           DocumentFile main = DocumentFile.fromTreeUri(this,Uri.parse(sharedPreferences.getString(GIVEN_SCOPE,"")));
           DocumentFile folder = DocumentFile.fromTreeUri(this,Uri.parse(sharedPreferences.getString(FOLDER_URI,"")));
           if (folder!=null && folder.getName().equals(name)){
               callbacks.onFolderAccessGranted(folder);
           }
       }else {
           if (getContentResolver().getPersistedUriPermissions().isEmpty()) {
               requestName = name;
               this.callbacks = callbacks;
               if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                   selectIfHavePermission();
               } else {
                   Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                   startActivityForResult(intent, REQUEST_CODE_PATH_TO_DATA);
               }
           }else {
               Uri uri = getContentResolver().getPersistedUriPermissions().get(0).getUri();
               sharedPreferences.edit().putString(GIVEN_SCOPE, uri.toString()).apply();
               DocumentFile pickedDir = DocumentFile.fromTreeUri(this, uri);
               if (pickedDir.exists()){
                   DocumentFile folder = pickedDir.findFile(name);
                   if (folder!=null && folder.exists()){
                       sharedPreferences.edit().putString(FOLDER_URI, folder.getUri().toString()).apply();
                   }else {
                       folder = pickedDir.createDirectory(name);
                       sharedPreferences.edit().putString(FOLDER_URI, folder.getUri().toString()).apply();
                   }
                   callbacks.onFolderAccessGranted(folder);
               }else {
                   requestName = name;
                   this.callbacks = callbacks;
                   if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                       selectIfHavePermission();
                   } else {
                       Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                       startActivityForResult(intent, REQUEST_CODE_PATH_TO_DATA);
                   }
               }
           }
       }
    }


    @AfterPermissionGranted(STORAGE_PERMISSION)
    private void selectIfHavePermission() {
        if (EasyPermissions.hasPermissions(this, WRITE_EXTERNAL_STORAGE)) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            startActivityForResult(intent, REQUEST_CODE_PATH_TO_DATA);
        } else {
            EasyPermissions.requestPermissions(this, "Grant permission to save meme audio.", STORAGE_PERMISSION, WRITE_EXTERNAL_STORAGE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, permissionCallbacks);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PATH_TO_DATA) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            if (data == null) {
                return;
            }
            DocumentFile pickedDir = DocumentFile.fromTreeUri(this, data.getData());
            grantUriPermission(getPackageName(), data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            getContentResolver().takePersistableUriPermission(data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            sharedPreferences.edit().putString(GIVEN_SCOPE, data.getData().toString()).apply();
            DocumentFile folder = pickedDir.findFile(requestName);
            if (folder==null || !folder.exists()){
                folder = pickedDir.createDirectory(requestName);
            }
            sharedPreferences.edit().putString(FOLDER_URI,folder.getUri().toString()).apply();
            callbacks.onFolderAccessGranted(folder);

        } else if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            selectIfHavePermission();
        }
    }

}
