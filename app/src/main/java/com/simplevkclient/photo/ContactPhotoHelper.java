package com.simplevkclient.photo;


        import java.io.File;
        import java.io.IOException;
        import java.io.InputStream;
        import android.content.ContentResolver;
        import android.content.ContentUris;
        import android.content.ContentValues;
        import android.content.Context;
        import android.content.res.Resources;
        import android.database.Cursor;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.net.Uri;
        import android.os.Environment;
        import android.provider.ContactsContract;
        import android.provider.ContactsContract.Data;
        import android.provider.ContactsContract.CommonDataKinds.Photo;
        import android.util.Log;

        import com.simplevkclient.R;

public class ContactPhotoHelper {

    private static final String TEMP_PHOTO_FILE = "tempPhoto.png";

    public static Uri getTempUri() {
        return Uri.fromFile(getTempFile());
    }

    public static File getTempFile() {
        if (isSDCARDMounted()) {

            File f = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE);
            try {
                f.createNewFile();
            } catch (IOException e) {
                Log.e("er", e.toString());
            }
            return f;
        } else {
            return null;
        }
    }

    public static boolean isSDCARDMounted() {
        String status = Environment.getExternalStorageState();

        if (status.equals(Environment.MEDIA_MOUNTED))
            return true;
        return false;
    }

    public static void setContactPhoto(ContentResolver c, byte[] bytes, long personId) {
        int photoRow = -1;
        String where = Data.RAW_CONTACT_ID + " = " + personId + " AND "
                + Data.MIMETYPE + "=='" + Photo.CONTENT_ITEM_TYPE
                + "'";
        Cursor cursor = c.query(Data.CONTENT_URI, null, where, null, null);
        int idIdx = cursor.getColumnIndexOrThrow(Data._ID);
        if (cursor.moveToFirst()) {
            photoRow = cursor.getInt(idIdx);
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(Data.RAW_CONTACT_ID, personId);
        values.put(Data.IS_SUPER_PRIMARY, 1);
        values.put(Photo.PHOTO, bytes);
        values.put(Data.MIMETYPE, Photo.CONTENT_ITEM_TYPE);

        if (photoRow >= 0) {
            c.update(Data.CONTENT_URI, values, Data._ID + " = " + photoRow, null);
        } else {
            c.insert(Data.CONTENT_URI, values);
        }
    }

    public static Bitmap loadContactPhoto(ContentResolver cr, long id, Context context) {
        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
        if (input == null) {
            Resources res = context.getResources();

        }
        return BitmapFactory.decodeStream(input);
    }

}