package com.simplevkclient;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.AccessibleObject;
import java.util.ArrayList;

/**
 * Created by Севастьян on 06.09.2017.
 */

public class AddPhoto extends Activity{
    private Bitmap contactPhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<ContentProviderOperation> op = new ArrayList<ContentProviderOperation>();
        String id = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        Log.i("ok", "id: "+id);
/* Добавляем пустой контакт */
        op.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

/* Добавляем данные имени */
        op.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, "lobert Smith")
                .build());
/* Добавляем данные телефона */
        op.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, "11-22-33")
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());

        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, op);

        } catch (Exception e) {
            Log.e("Exception: ", e.getMessage());
        }
        String fname=new File(Environment.getExternalStorageDirectory(), "test.png").getAbsolutePath();
        //contactPhoto = BitmapFactory.decodeFile(ContactPhotoHelper.getTempFile().getAbsolutePath());
        contactPhoto = BitmapFactory.decodeFile(fname);
        Log.i("ok", fname);
        if (contactPhoto != null) {
            Log.i("ok", "ok");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            contactPhoto.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();
            ContactPhotoHelper.setContactPhoto(getContentResolver(), bitmapdata, 0);
        }
    }

}
