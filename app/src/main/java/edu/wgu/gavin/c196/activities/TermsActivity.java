package edu.wgu.gavin.c196.activities;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.wgu.gavin.c196.R;
import edu.wgu.gavin.c196.data.WGUContract;

public class TermsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        ContentValues values = new ContentValues();
        values.put(WGUContract.Terms.TITLE, "Test Title");
        Uri uri = getContentResolver().insert(WGUContract.Terms.CONTENT_URI, values);
        Cursor cursor = getContentResolver().query(uri, WGUContract.Terms.COLUMNS, "where _id = ?", new String[] { String.valueOf(ContentUris.parseId(uri)) }, null);
        if (cursor != null)
            cursor.close();
    }

}
