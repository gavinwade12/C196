package edu.wgu.gavin.c196.activities;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;

import edu.wgu.gavin.c196.R;
import edu.wgu.gavin.c196.adapters.CoursesCursorAdapter;
import edu.wgu.gavin.c196.data.WGUContract;

public class TermDetailActivity extends AppCompatActivity {

    public static int EDIT_TERM_REQUEST = 3;
    private static DateFormat mDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
    private long mTermId;
    private Cursor mCourseCursor;
    private ContentResolver mContentResolver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termdetail);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Log.d(getLocalClassName(), "Failed to get Bundle from intent.");
            finish();
            return;
        }
        mTermId = extras.getLong(WGUContract.Terms._ID);

        mContentResolver = getContentResolver();
        displayTermInformation();

        mCourseCursor = mContentResolver.query(
                WGUContract.Courses.CONTENT_URI,
                WGUContract.Courses.COLUMNS,
                "term_id = ?",
                new String[]{String.valueOf(mTermId)},
                WGUContract.Courses.START_DATE + " ASC");
        if (mCourseCursor == null) {
            Log.d(getLocalClassName(), "Courses query failed.");
            return;
        }

        CoursesCursorAdapter adapter = new CoursesCursorAdapter(this, mCourseCursor, 0);
        ListView coursesList = findViewById(R.id.term_courses);
        coursesList.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        if (mCourseCursor != null && !mCourseCursor.isClosed())
            mCourseCursor.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent intent = new Intent(this, EditTermActivity.class);
                intent.putExtra(WGUContract.Terms._ID, mTermId);
                startActivityForResult(intent, EDIT_TERM_REQUEST);
                return true;
            case R.id.action_delete:
                if (mCourseCursor.getCount() > 0) {
                    new AlertDialog.Builder(this)
                            .setMessage("Cannot delete term with courses.")
                            .setTitle("C196")
                            .setPositiveButton("OK", null)
                            .setCancelable(true)
                            .create()
                            .show();
                    return true;
                }
                getContentResolver().delete(
                        ContentUris.withAppendedId(WGUContract.Terms.CONTENT_URI, mTermId),
                        null,
                        null);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != EDIT_TERM_REQUEST) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        // If the request failed, then the data likely wasn't updated.
        if (resultCode != RESULT_OK)
            return;

        displayTermInformation();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    private void displayTermInformation() {
        String title = "";
        Date startDate = new Date(),
                endDate = new Date();
        try (Cursor termCursor = mContentResolver.query(
                ContentUris.withAppendedId(WGUContract.Terms.CONTENT_URI, mTermId),
                WGUContract.Terms.COLUMNS,
                null, null, null)) {
            if (termCursor == null || !termCursor.moveToFirst()) {
                Log.d(getLocalClassName(), "Failed to get term with Id: " + mTermId);
                finish();
            }

            title = termCursor.getString(termCursor.getColumnIndex(WGUContract.Terms.TITLE));
            startDate = new Date(termCursor.getLong(termCursor.getColumnIndex(WGUContract.Terms.START_DATE)) * 1000);
            endDate = new Date(termCursor.getLong(termCursor.getColumnIndex(WGUContract.Terms.END_DATE)) * 1000);
        } catch (Exception e) {
            Log.d(getLocalClassName(), "Failure displaying term information: " + e.toString());
            finish();
        }

        TextView titleView = findViewById(R.id.term_title);
        TextView startDateView = findViewById(R.id.term_startDate);
        TextView endDateView = findViewById(R.id.term_endDate);

        titleView.setText(title);
        startDateView.setText(mDateFormat.format(startDate));
        endDateView.setText(mDateFormat.format(endDate));
    }
}
