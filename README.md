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

Extend your activity with `BaseActivity` like this:

```java
public class MainActivity extends BaseActivity {
   ...
}
```

Change the storage permission in `Manifest` to this:

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


## License 

    MIT License

    Copyright (c) 2019 Aashit Shah

    Permission is hereby granted, free of charge, to any person obtaining a copy 
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
    of the Software, and to permit persons to whom the Software is furnished to do so,
    subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all copies
    or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
    INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
    FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
    ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
