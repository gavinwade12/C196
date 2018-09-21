package edu.wgu.gavin.c196.activities;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edu.wgu.gavin.c196.R;
import edu.wgu.gavin.c196.data.WGUContract;

public class EditTermActivity extends AppCompatActivity {

    private static DateFormat mDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

    private String mTitle;
    private Date mStartDate;
    private Date mEndDate;

    private Button mBtnStartDate;
    private Button mBtnEndDate;

    private boolean mIsNewTerm;
    private Uri mTermUri;
    private ContentResolver mContentResolver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editterm);

        mContentResolver = getContentResolver();

        EditText txtTitle = findViewById(R.id.term_title);
        mBtnStartDate = findViewById(R.id.term_startDate);
        mBtnEndDate = findViewById(R.id.term_endDate);

        txtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                mTitle = editable.toString();
                updateFields(true);
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        });

        Bundle extras = getIntent().getExtras();
        if (extras == null || !extras.containsKey(WGUContract.Terms._ID)) {
            mIsNewTerm = true;
            Date currentDate = Calendar.getInstance().getTime();
            mStartDate = currentDate;
            mEndDate = currentDate;
            mTitle = "";
            txtTitle.setText(mTitle);
            return;
        }

        long termId = extras.getLong(WGUContract.Terms._ID);
        mTermUri = ContentUris.withAppendedId(WGUContract.Terms.CONTENT_URI, termId);

        try (Cursor termCursor = mContentResolver.query(
                ContentUris.withAppendedId(WGUContract.Terms.CONTENT_URI, termId),
                WGUContract.Terms.COLUMNS,
                null, null, null)) {
            if (termCursor == null || !termCursor.moveToFirst()) {
                Log.d(getLocalClassName(), "Failed to get term with Id: " + termId);
                finish();
            }

            mTitle = termCursor.getString(termCursor.getColumnIndex(WGUContract.Terms.TITLE));
            mStartDate = new Date(termCursor.getLong(termCursor.getColumnIndex(WGUContract.Terms.START_DATE)) * 1000);
            mEndDate = new Date(termCursor.getLong(termCursor.getColumnIndex(WGUContract.Terms.END_DATE)) * 1000);
        } catch (Exception e) {
            Log.d(getLocalClassName(), "Failure displaying term information: " + e.toString());
            finish();
        }

        txtTitle.setText(mTitle);
        updateFields(false);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    public void onDateBtnClick(View view) {
        Calendar calendar = Calendar.getInstance();
        final int viewId = view.getId();
        if (viewId == R.id.term_startDate)
            calendar.setTime(mStartDate);
        else if (viewId == R.id.term_endDate)
            calendar.setTime(mEndDate);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        Calendar newCalendar = Calendar.getInstance();
                        newCalendar.set(year, month, day);
                        Date newDate = newCalendar.getTime();
                        if (viewId == R.id.term_startDate)
                            mStartDate = newDate;
                        else if (viewId == R.id.term_endDate)
                            mEndDate = newDate;
                        updateFields(true);
                    }
                },
                year, month, day);
        Window window = dialog.getWindow();
        if (window != null)
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void updateFields(boolean persistent) {
        mBtnStartDate.setText(mDateFormat.format(mStartDate));
        mBtnEndDate.setText(mDateFormat.format(mEndDate));

        if (!persistent)
            return;

        ContentValues values = new ContentValues();
        values.put(WGUContract.Terms.TITLE, mTitle);
        values.put(WGUContract.Terms.START_DATE, mStartDate.getTime()/1000);
        values.put(WGUContract.Terms.END_DATE, mEndDate.getTime()/1000);

        if (mTermUri == null) {
            mTermUri = mContentResolver.insert(WGUContract.Terms.CONTENT_URI, values);
            if (mTermUri == null)
                Log.d(getLocalClassName(), "Failed inserting term.");
            return;
        }

        int updated = mContentResolver.update(mTermUri, values, null, null);
        if (updated == 0)
            Log.d(getLocalClassName(), "Failed to updated term with Id: " + ContentUris.parseId(mTermUri));
    }
}
