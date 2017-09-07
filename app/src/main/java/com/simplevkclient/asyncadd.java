package com.simplevkclient;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Севастьян on 07.09.2017.
 */

public class asyncadd extends AsyncTask<Void, Integer, Void> {
    static ArrayList<Friend> Friends = new ArrayList<>();
    ArrayList<ContentProviderOperation> op = new ArrayList<ContentProviderOperation>();
    private Context mContext;

    public asyncadd(Context context){
        mContext = context;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Intent i = new Intent(mContext, AddToContacts.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);
    }

    @Override
    protected Void doInBackground(Void... params) {
        Friends = MainActivity.Friends;
        for(int i=0; i<Friends.size(); i++){
            Friend friend = Friends.get(i);
            String name = friend.name.toString()+" "+friend.fam.toString();
            String num = friend.num.toString();
            op.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .build());
            Log.i("ok", i+"");
/* Добавляем данные имени */
            op.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
                    .build());
/* Добавляем данные телефона */
            op.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, num)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());

            try {
                mContext.getContentResolver().applyBatch(ContactsContract.AUTHORITY, op);
            } catch (Exception e) {
                Log.e("Exception: ", e.getMessage());
            }
            op.clear();
        }

        //Toast.makeText(getApplicationContext(), "Контакты добалены!", Toast.LENGTH_SHORT).show();
        return null;
    }
}
