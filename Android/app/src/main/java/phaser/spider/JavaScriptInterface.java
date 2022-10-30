package phaser.spider;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.webkit.JavascriptInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class JavaScriptInterface {

    private Context context;

    public JavaScriptInterface(Context context)
        {
        this.context = context;
        }

    public static String getBase64StringFromBlobUrl(String blobUrl)
        {
        if(blobUrl.startsWith("blob"))
            {
            return "javascript: var xhr = new XMLHttpRequest();" +
                    "xhr.open('GET', '" + blobUrl + "', true);" +
                    "xhr.setRequestHeader('Content-type','application/octet-stream');" +
                    "xhr.responseType = 'blob';" +
                    "xhr.onload = function(e)" +
                        "{" +
                        "if (this.status == 200)" +
                            "{" +
                            "var blobStateFile = this.response;" +
                            "var filereader = new FileReader();" +
                            "filereader.onload = function()" +
                                "{" +
                                "Android.getBase64FromBlobData(filereader.result);" +
                                "};" +
                            "filereader.readAsDataURL(blobStateFile);" +
                            "}" +
                        "};" +
                    "xhr.send();";
            }
        return "javascript: console.log('It is not a Blob URL');";
        }

    @JavascriptInterface
    public void getBase64FromBlobData(String base64Data)
        {
        // different logics for writing a file - thank you android
        if (Build.VERSION.SDK_INT>=29)
            {
            writeFileNew(base64Data);
            }
        else
            {
            writeFileLegacy(base64Data);
            }
        }

    public void writeFileLegacy(String base64Data)
        {
        try
            {
            String path = Environment.getExternalStorageDirectory() + File.separator  + "Download";

            // Creates the folder
            File folder = new File(path);
            folder.mkdirs();

            // Getting the format and content that the file will have
            String fileNameRAW = GlobalVars.filename;

            String fileName = "";
            if (fileNameRAW.indexOf(".")>0)
                {
                fileName = fileNameRAW.substring(0,fileNameRAW.lastIndexOf("."));
                }
                else
                {
                fileName = fileNameRAW;
                }

            String fileFormat = "";
            if (fileNameRAW.indexOf(".")>0)
                {
                fileFormat = fileNameRAW.substring(fileNameRAW.lastIndexOf("."),fileNameRAW.length());
                }

            boolean fileCanBeCreated = false;
            int counter = 0;
            while(fileCanBeCreated==false)
                {
                if (counter==0)
                    {
                    File fileChecker = new File(folder, fileName + fileFormat);
                    if (fileChecker.exists()==false)
                        {
                        fileName = fileName + fileFormat;
                        fileCanBeCreated = true;
                        }
                        else
                        {
                        counter = counter + 1;
                        }
                    }
                    else
                    {
                    File fileChecker = new File(folder, fileName + "(" + counter + ")" + fileFormat);
                    if (fileChecker.exists()==false)
                        {
                        fileName = fileName + "(" + counter + ")" + fileFormat;
                        fileCanBeCreated = true;
                        }
                        else
                        {
                        counter = counter + 1;
                        }
                    }
                }

            // Patch for Android 8.0 and above
            String header = "data:application/octet-stream;base64,";
            int located = base64Data.indexOf(header);
            base64Data = base64Data.substring(located + header.length(),base64Data.length());

            // Writing the file
            byte[] fileAsBytes = Base64.decode(base64Data, 0);
            FileOutputStream fOut;
            fOut = new FileOutputStream(path + "/" + fileName, false);
            fOut.write(fileAsBytes);
            fOut.flush();
            fOut.close();
            }
            catch(Exception e)
            {
            }
        }

    public void writeFileNew(String base64Data)
        {
        // Getting the format and content that the file will have
        String fileName = GlobalVars.filename;

        // Creating a variable that will contain the mime type
        String mimeType = "";

        // Getting the mime type
        if (fileName.toLowerCase().endsWith(".mp3"))
            {
            mimeType = "audio/mpeg";
            }
        else if (fileName.toLowerCase().endsWith(".mpga"))
            {
            mimeType = "audio/mpeg";
            }
        else if (fileName.toLowerCase().endsWith(".m4a"))
            {
            mimeType = "audio/mp4";
            }
        else if (fileName.toLowerCase().endsWith(".wav"))
            {
            mimeType = "audio/x-wav";
            }
        else if (fileName.toLowerCase().endsWith(".amr"))
            {
            mimeType = "audio/amr";
            }
        else if (fileName.toLowerCase().endsWith(".awb"))
            {
            mimeType = "audio/amr-wb";
            }
        else if (fileName.toLowerCase().endsWith(".wma"))
            {
            mimeType = "audio/x-ms-wma";
            }
        else if (fileName.toLowerCase().endsWith(".ogg"))
            {
            mimeType = "audio/ogg";
            }
        else if (fileName.toLowerCase().endsWith(".oga"))
            {
            mimeType = "audio/oga";
            }
        else if (fileName.toLowerCase().endsWith(".aac"))
            {
            mimeType = "audio/aac";
            }
        else if (fileName.toLowerCase().endsWith(".mka"))
            {
            mimeType = "audio/x-matroska";
            }
        else if (fileName.toLowerCase().endsWith(".mid"))
            {
            mimeType = "audio/midi";
            }
        else if (fileName.toLowerCase().endsWith(".midi"))
            {
            mimeType = "audio/midi";
            }
        else if (fileName.toLowerCase().endsWith(".xmf"))
            {
            mimeType = "audio/midi";
            }
        else if (fileName.toLowerCase().endsWith(".xmf"))
            {
            mimeType = "audio/midi";
            }
        else if (fileName.toLowerCase().endsWith(".rtttl"))
            {
            mimeType = "audio/midi";
            }
        else if (fileName.toLowerCase().endsWith(".smf"))
            {
            mimeType = "audio/sp-midi";
            }
        else if (fileName.toLowerCase().endsWith(".imy"))
            {
            mimeType = "audio/imelody";
            }
        else if (fileName.toLowerCase().endsWith(".imy"))
            {
            mimeType = "audio/imelody";
            }
        else if (fileName.toLowerCase().endsWith(".rtx"))
            {
            mimeType = "audio/midi";
            }
        else if (fileName.toLowerCase().endsWith(".ota"))
            {
            mimeType = "audio/midi";
            }
        else if (fileName.toLowerCase().endsWith(".mxmf"))
            {
            mimeType = "audio/midi";
            }
        else if (fileName.toLowerCase().endsWith(".flac"))
            {
            mimeType = "audio/flac";
            }
        else if (fileName.toLowerCase().endsWith(".m3u"))
            {
            mimeType = "audio/x-mpegurl";
            }
        else if (fileName.toLowerCase().endsWith(".pls"))
            {
            mimeType = "audio/x-scpls";
            }
        else if (fileName.toLowerCase().endsWith(".m3u8"))
            {
            mimeType = "audio/mpegurl";
            }
        else if (fileName.toLowerCase().endsWith(".m3u"))
            {
            mimeType = "application/x-mpegurl";
            }
        else if (fileName.toLowerCase().endsWith(".wpl"))
            {
            mimeType = "application/vnd.ms-wpl";
            }
        else if (fileName.toLowerCase().endsWith(".m3u8"))
            {
            mimeType = "application/vnd.apple.mpegurl";
            }
        else if (fileName.toLowerCase().endsWith(".fl"))
            {
            mimeType = "application/x-android-drm-fl";
            }
        else if (fileName.toLowerCase().endsWith(".mpeg"))
            {
            mimeType = "video/mpeg";
            }
        else if (fileName.toLowerCase().endsWith(".mpg"))
            {
            mimeType = "video/mpeg";
            }
        else if (fileName.toLowerCase().endsWith(".mp4"))
            {
            mimeType = "video/mp4";
            }
        else if (fileName.toLowerCase().endsWith(".m4v"))
            {
            mimeType = "video/mp4";
            }
        else if (fileName.toLowerCase().endsWith(".mov"))
            {
            mimeType = "video/quicktime";
            }
        else if (fileName.toLowerCase().endsWith(".3gp"))
            {
            mimeType = "video/3gpp";
            }
        else if (fileName.toLowerCase().endsWith(".3gpp"))
            {
            mimeType = "video/3gpp";
            }
        else if (fileName.toLowerCase().endsWith(".3g2"))
            {
            mimeType = "video/3gpp2";
            }
        else if (fileName.toLowerCase().endsWith(".3gpp2"))
            {
            mimeType = "video/3gpp2";
            }
        else if (fileName.toLowerCase().endsWith(".mkv"))
            {
            mimeType = "video/x-matroska";
            }
        else if (fileName.toLowerCase().endsWith(".webm"))
            {
            mimeType = "video/webm";
            }
        else if (fileName.toLowerCase().endsWith(".ts"))
            {
            mimeType = "video/mp2ts";
            }
        else if (fileName.toLowerCase().endsWith(".avi"))
            {
            mimeType = "video/avi";
            }
        else if (fileName.toLowerCase().endsWith(".wmv"))
            {
            mimeType = "video/x-ms-wmv";
            }
        else if (fileName.toLowerCase().endsWith(".asf"))
            {
            mimeType = "video/x-ms-asf";
            }
        else if (fileName.toLowerCase().endsWith(".mpg"))
            {
            mimeType = "video/mp2p";
            }
        else if (fileName.toLowerCase().endsWith(".mpeg"))
            {
            mimeType = "video/mp2p";
            }
        else if (fileName.toLowerCase().endsWith(".jpg"))
            {
            mimeType = "image/jpeg";
            }
        else if (fileName.toLowerCase().endsWith(".jpeg"))
            {
            mimeType = "image/jpeg";
            }
        else if (fileName.toLowerCase().endsWith(".gif"))
            {
            mimeType = "image/gif";
            }
        else if (fileName.toLowerCase().endsWith(".png"))
            {
            mimeType = "image/png";
            }
        else if (fileName.toLowerCase().endsWith(".bmp"))
            {
            mimeType = "image/x-ms-bmp";
            }
        else if (fileName.toLowerCase().endsWith(".wbmp"))
            {
            mimeType = "image/vnd.wap.wbmp";
            }
        else if (fileName.toLowerCase().endsWith(".webp"))
            {
            mimeType = "image/webp";
            }
        else if (fileName.toLowerCase().endsWith(".dng"))
            {
            mimeType = "image/x-adobe-dng";
            }
        else if (fileName.toLowerCase().endsWith(".cr2"))
            {
            mimeType = "image/x-canon-cr2";
            }
        else if (fileName.toLowerCase().endsWith(".nef"))
            {
            mimeType = "image/x-nikon-nef";
            }
        else if (fileName.toLowerCase().endsWith(".nrw"))
            {
            mimeType = "image/x-nikon-nrw";
            }
        else if (fileName.toLowerCase().endsWith(".arw"))
            {
            mimeType = "image/x-sony-arw";
            }
        else if (fileName.toLowerCase().endsWith(".rw2"))
            {
            mimeType = "image/x-panasonic-rw2";
            }
        else if (fileName.toLowerCase().endsWith(".orf"))
            {
            mimeType = "image/x-olympus-orf";
            }
        else if (fileName.toLowerCase().endsWith(".raf"))
            {
            mimeType = "image/x-fuji-raf";
            }
        else if (fileName.toLowerCase().endsWith(".pef"))
            {
            mimeType = "image/x-pentax-pef";
            }
        else if (fileName.toLowerCase().endsWith(".srw"))
            {
            mimeType = "image/x-samsung-srw";
            }
        else if (fileName.toLowerCase().endsWith(".txt"))
            {
            mimeType = "text/plain";
            }
        else if (fileName.toLowerCase().endsWith(".htm"))
            {
            mimeType = "text/html";
            }
        else if (fileName.toLowerCase().endsWith(".html"))
            {
            mimeType = "text/html";
            }
        else if (fileName.toLowerCase().endsWith(".pdf"))
            {
            mimeType = "application/pdf";
            }
        else if (fileName.toLowerCase().endsWith(".doc"))
            {
            mimeType = "application/msword";
            }
        else if (fileName.toLowerCase().endsWith(".docx"))
            {
            mimeType = "application/msword";
            }
        else if (fileName.toLowerCase().endsWith(".rtf"))
            {
            mimeType = "application/rtf";
            }
        else if (fileName.toLowerCase().endsWith(".xls"))
            {
            mimeType = "application/vnd.ms-excel";
            }
        else if (fileName.toLowerCase().endsWith(".xlsx"))
            {
            mimeType = "application/vnd.ms-excel";
            }
        else if (fileName.toLowerCase().endsWith(".ppt"))
            {
            mimeType = "application/mspowerpoint";
            }
        else if (fileName.toLowerCase().endsWith(".pptx"))
            {
            mimeType = "application/mspowerpoint";
            }
        else if (fileName.toLowerCase().endsWith(".zip"))
            {
            mimeType = "application/zip";
            }
        else
            {
            mimeType = "application/octet-stream";
            }

        // Patch for Android 8.0 and above
        String header = "data:application/octet-stream;base64,";
        int located = base64Data.indexOf(header);
        base64Data = base64Data.substring(located + header.length(),base64Data.length());

        // Getting file content
        byte[] fileAsBytes = Base64.decode(base64Data, 0);

        try
            {
            ContentValues values = new ContentValues();
            OutputStream outputStream;

            values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

            Uri extVolumeUri = MediaStore.Files.getContentUri("external");
            Uri fileUri = context.getContentResolver().insert(extVolumeUri, values);

            outputStream = context.getContentResolver().openOutputStream(fileUri);
            outputStream.write(fileAsBytes);
            outputStream.close();
            } catch(Exception e) {
            }
        }
    }