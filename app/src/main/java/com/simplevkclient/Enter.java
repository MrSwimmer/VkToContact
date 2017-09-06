package com.simplevkclient;

import android.app.Application;
import android.content.Intent;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

/**
 * Created by Севастьян on 05.09.2017.
 */

public class Enter extends Application {

        VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
            @Override
            public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
                if (newToken == null) {
                    Intent intent = new Intent(Enter.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        };
        @Override
        public void onCreate() {
            super.onCreate();
            vkAccessTokenTracker.startTracking();
            VKSdk.initialize(this);
        }
}
