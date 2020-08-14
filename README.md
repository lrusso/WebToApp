# Web To App

Examples of how to port a Web or HTML5 game to a native Android, iOS and Mac OS X App.

## How does it work

The App has a WebView that shows you the content of a embedded HTML file. Also, you can read files from the device and write files to it. For last, in the Android version the Web file is in **app/src/main/assets/SpiderGame.htm** and in the iOS/Mac OS X version is in **www/SpiderGame.htm**.

## Porting details

| PORTING TO  | WORKS UNDER | READ FILES | WRITE FILES
| :------------ |:--------------- |:---------------: |:---------------:|
| iOS | iOS 11.0 or later | Yes | Yes
| OS X | OS X 10.12 or later | Yes | Yes
| Android | Android 5.0 or later | Yes | Yes

## JavaScript example - How to download a file to the device

```javascript
function downloadBinaryFile(myArrayBuffer, filename)
    {
    new WebToApp(myArrayBuffer, filename, "application/octet-stream", true);
    }

function downloadTextFile(textContent, filename)
    {
    new WebToApp(textContent, filename, "application/octet-stream", false);
    }
```

## Android porting notes

Every downloaded Blob file is automatically written in the **Downloads** folder. If the file already exists, a new file will be created with a suffix, for example: **MyFileName(1).txt**.

## iOS porting notes

If your App is not going to save an image, delete the **NSPhotoLibraryUsageDescription** and **NSPhotoLibraryAddUsageDescription** keys from the **Info.plist** file.

## OS X porting notes

Every downloaded Blob file is automatically written in the **Downloads** folder. If the file already exists, a new file will be created with a suffix, for example: **MyFileName(1).txt**.

## OS X porting tutorial - How to get the .app file?

* Open the Mac project in Xcode.

* Click in **Product**.

* Click in **Archive**.

* Right click in the first item of the list and then click in **Show in Finder**.

* Right click in the **(ProductName).xcarchive** file.

* Click in **Show package contents**.

* Go to the **Products** folder.

* Go to the **Applications** folder.

* There is your .app file.

## Mac version

![alt screenshot](https://raw.githubusercontent.com/lrusso/WebToApp/master/Screenshot1.png)

## iOS version

![alt screenshot](https://raw.githubusercontent.com/lrusso/WebToApp/master/Screenshot2.png)

## Android version

![alt screenshot](https://raw.githubusercontent.com/lrusso/WebToApp/master/Screenshot3.png)

## Original Web version

https://lrusso.github.io/Spider/Spider.htm
