package phaser.spider;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends Activity
    {
    private static ValueCallback<Uri> mUploadMessage;
    private static ValueCallback<Uri[]> mUploadMessage5;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private final static int PERMISSION_REQUEST = 123;
    private WebView webView;

    @Override protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        hideNavigationBar();

        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setTextZoom(100);

        webView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
        webView.setDownloadListener(new DownloadListener(){@Override public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {webView.loadUrl(JavaScriptInterface.getBase64StringFromBlobUrl(url));}});

        webView.setWebViewClient(new myWebClient());
        webView.setWebChromeClient(new WebChromeClient()
            {
            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg)
                {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
                }

            // For Android 3.0+
            public void openFileChooser(ValueCallback uploadMsg, String acceptType)
                {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
                }

            //For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)
                {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
                }

            // For Android 5.0
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, FileChooserParams fileChooserParams)
                {
                mUploadMessage5 = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
                return true;
                }

            @Override public boolean onConsoleMessage(ConsoleMessage consoleMessage)
                {
                String stringMessage = consoleMessage.message();

                if (stringMessage.startsWith("WEBTOAPP_FILENAME="))
                    {
                    GlobalVars.filename = stringMessage.substring(18,stringMessage.length());
                    }

                return true;
                };
            });

        String htmlData = loadAssetTextAsString("SpiderGame.htm", this);
        webView.loadDataWithBaseURL(null, htmlData, "text/html", "UTF-8",null);

        if (Build.VERSION.SDK_INT>=23) //MARSHMALLOW
            {
            try
                {
                checkAppPermissions();
                }
            catch (Exception e)
                {
                }
            }
        }

    @Override public void onResume()
        {
        super.onResume();
        hideNavigationBar();
        }

    @Override public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
        {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try
            {
            if (requestCode==PERMISSION_REQUEST)
                {
                if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED))
                    {
                    System.exit(0);
                    }
                }
            }
            catch(Exception e)
            {
            }
        }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent intent)
        {
        if (resultCode == RESULT_OK)
            {
            if(requestCode==FILECHOOSER_RESULTCODE)
                {
                if (Build.VERSION.SDK_INT>=21) //LOLLIPOP
                    {
                    try
                        {
                        Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                        mUploadMessage5.onReceiveValue(new Uri[]{result});
                        mUploadMessage5 = null;
                        }
                        catch(Exception e)
                        {
                        }
                    }
                }
                else
                {
                try
                    {
                    if (null == mUploadMessage) return;
                    Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                    mUploadMessage.onReceiveValue(result);
                    mUploadMessage = null;
                    }
                    catch(Exception e)
                    {
                    }
                }
            }
            else
            {
            try
                {
                if (mUploadMessage5 != null)
                    {
                    mUploadMessage5.onReceiveValue(null);
                    mUploadMessage5 = null;
                    }

                if (mUploadMessage != null)
                    {
                    mUploadMessage.onReceiveValue(null);
                    mUploadMessage = null;
                    }
                }
                catch(Exception e)
                {
                }
            }
        }

    private String loadAssetTextAsString(String name, Context myContext)
        {
        BufferedReader in = null;
        try
            {
            StringBuilder buf = new StringBuilder();
            InputStream is = myContext.getAssets().open(name);
            in = new BufferedReader(new InputStreamReader(is));

            String str;
            boolean isFirst = true;
            while ((str=in.readLine())!=null)
                {
                if (isFirst)
                    {
                    isFirst = false;
                    }
                    else
                    {
                    buf.append("\n");
                    }
                buf.append(str);
                }
            return buf.toString();
            }
            catch (IOException e)
            {
            }
            finally
            {
            if (in!=null)
                {
                try
                    {
                    in.close();
                    }
                    catch (IOException e)
                    {
                    }
                }
            }
        return null;
        }

    private void hideNavigationBar()
        {
        try
            {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
            }
            catch(Exception e)
            {
            }
        }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkAppPermissions()
        {
        try
            {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(PERMISSIONS_STORAGE, PERMISSION_REQUEST);
                }
            }
            catch(Exception e)
            {
            }
        }
    }