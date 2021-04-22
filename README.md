# SAF
This utility makes using SAF easy.


[![](https://jitpack.io/v/aashitshah26/SAF.svg)](https://jitpack.io/#aashitshah26/SAF)
![](https://img.shields.io/apm/l/vim-mode)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)


To add *SAF* in your project follow the following steps :-

## Installing

Add [JitPack](https://jitpack.io) repository to your root `build.gradle`

```gradle
allprojects {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```
Add dependency to your module `build.gradle`:

```gradle
implementation 'com.github.aashitshah26:SAF:<latest-version>'
```

## Usage

Add *SAF* to your Code:

Extend your activity with Base Activity like this:

```java
public class MainActivity extends BaseActivity {
   ...
}
```

Change the storage permission in manifest to this:

```xml
<uses-permission
        android:name="android.permission.WRITE_EXTERNAL_STORAGE"
        android:maxSdkVersion="28" />
```


To create/access your app folder call *requestMainFolderAccess()*:

```java
requestMainFolderAccess("app-folder-name", new StorageCallbacks() {
            @Override
            public void onFolderAccessGranted(DocumentFile folder) {
                //Save or access any files from this folder only.
                
                //To create a sub directory:-
                 DocumentFile subDirectory = folder.createDirectory("sub-directory-name");
      
                //To create a file:-
                 DocumentFile file = folder.createFile(MimeTypeMap.getSingleton().getMimeTypeFromExtension(".pdf"),"trial.pdf") 
            }
        });
```
