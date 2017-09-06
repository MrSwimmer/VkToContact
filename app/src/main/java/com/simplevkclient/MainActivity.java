package com.simplevkclient;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.provider.ContactsContract;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.util.VKUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    private String scope[] = new String[]{VKScope.MESSAGES, VKScope.FRIENDS, VKScope.WALL};
    ListView mListView;

    private Bitmap contactPhoto = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VKSdk.login(this, scope);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
                @Override
                public void onResult(VKAccessToken res) {
                    mListView = (ListView) findViewById(R.id.list_view);
                    Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
                    VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "contacts"));
                    //Log.i("ok", request.toString());
                    request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        VKList list = (VKList) response.parsedModel;
                        JSONObject friend = null;
                        long lenght=0;
                        try {
                            lenght = response.json.getJSONObject("response").getJSONArray("items").length();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for(int i=0; i<lenght; i++){
                            try {
                                friend = (JSONObject) response.json.getJSONObject("response").getJSONArray("items").get(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                if(friend.get("mobile_phone").toString()!=null) {
                                    String name = friend.get("mobile_phone").toString();
                                    name+=" "+friend.get("first_name").toString()+" "+friend.get("second_name");
                                    Log.i("ok", name);
                                }
                            } catch (JSONException e) {
                                continue;
                            }
                        }

                        //Log.i("ok", response.json.toString());
//                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, list);
//                        mListView.setAdapter(arrayAdapter);
                    }
                });

            }
            @Override
            public void onError(VKError error) {
// Произошла ошибка авторизации (например, пользователь запретил авторизацию)
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
