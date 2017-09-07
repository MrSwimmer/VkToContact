package com.simplevkclient;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Севастьян on 07.09.2017.
 */

public class AddToContacts extends Activity{


    Button Mark, Exit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end);

        Mark = (Button) findViewById(R.id.mark);
        Exit = (Button) findViewById(R.id.exit);
        Mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.simplevkclient"));
                startActivity(intent);
            }
        });
        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //finish();
    }

}
