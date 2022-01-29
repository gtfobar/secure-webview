package bar.gtfo.webview.javareflectionattack;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class TestActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Uri uri = intent.getData();

        Log.d("evil", "Scheme: " + uri.getScheme());
        Log.d("evil", "UserInfo: " + uri.getUserInfo());
        Log.d("evil", "Host: " + uri.getHost());
        Log.d("evil", "toString(): " + uri.toString());
    }
}