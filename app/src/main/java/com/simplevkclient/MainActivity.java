package com.simplevkclient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity {
    private String scope[] = new String[]{VKScope.MESSAGES, VKScope.FRIENDS, VKScope.WALL};
    ListView mListView;
    Button addbut;
    int count = 0;
    TextView numfriends;
    static public ArrayList<Friend> Friends = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        numfriends = (TextView) findViewById(R.id.count);
        addbut = (Button) findViewById(R.id.add);
        //Friends.clear();
        Friends.clear();
        count = 0;
        VKSdk.login(this, scope);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
                @Override
                public void onResult(VKAccessToken res) {
                    //mListView = (ListView) findViewById(R.id.list_view);
                    Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
                    VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "contacts,photo_max_orig"));
                    request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        //Log.i("ok", response.json.toString());
                        VKList list = (VKList) response.parsedModel;
                        JSONObject friend = null;
                        long lenght=0;
                        try {
                            lenght = response.json.getJSONObject("response").getJSONArray("items").length();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Log.i("ok", lenght+"");
                        for(int i=0; i<lenght; i++){
                            try {
                                friend = (JSONObject) response.json.getJSONObject("response").getJSONArray("items").get(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                String number = friend.get("mobile_phone").toString();
                                Pattern pattern = Pattern.compile("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$");
                                Matcher matcher = pattern.matcher(number);
                                if(matcher.matches()){
                                    //Log.i("ok", "valid"+number);
                                    String num = friend.get("mobile_phone").toString();
                                    String name = friend.get("first_name").toString();
                                    String fam = friend.get("last_name").toString();
                                    Friend newfr = new Friend(name, fam, num);
                                    Log.i("ok", name+" "+fam+" "+num);
                                    Friends.add(count,newfr);
                                    count++;
                                }
                                else {
                                    //Log.i("ok", "invalid"+number);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        String numfr = String.valueOf(count);
                        numfriends.setText(numfr);
                        addbut.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(MainActivity.this, AddToContacts.class);
                                Toast.makeText(getApplicationContext(), "Начало синхронизации...", Toast.LENGTH_SHORT).show();
                                //startActivity(i);
                                //finish();
                                addbut.setEnabled(false);
                                new asyncadd(getApplication()).execute();
                            }
                        });

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
