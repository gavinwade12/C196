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
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edu.wgu.gavin.c196.R;
import edu.wgu.gavin.c196.adapters.cursor.CoursesCursorAdapter;
import edu.wgu.gavin.c196.data.WGUContract;
import edu.wgu.gavin.c196.models.Term;

public class EditTermActivity extends AppCompatActivity {

    private static DateFormat mDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

    private Term mTerm = new Term();

    private Button mBtnStartDate;
    private Button mBtnEndDate;

    private Uri mTermUri;
    private ContentResolver mContentResolver;
    private Cursor mCourseCursor;

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
                mTerm.title = editable.toString();
                updateFields();
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        });

        Bundle extras = getIntent().getExtras();
        if (extras == null || !extras.containsKey(WGUContract.Terms._ID)) {
            Date currentDate = Calendar.getInstance().getTime();
            mTerm.startDate = currentDate;
            mTerm.endDate = currentDate;
            mTerm.title = "";
            txtTitle.setText(mTerm.title);
            return;
        }

        long termId = extras.getLong(WGUContract.Terms._ID);
        mTermUri = ContentUris.withAppendedId(WGUContract.Terms.CONTENT_URI, termId);

        try (Cursor termCursor = mContentResolver.query(
                ContentUris.withAppendedId(WGUContract.Terms.CONTENT_URI, termId),
                WGUContract.Terms.COLUMNS,
                null, null, null)) {
            if (termCursor == null)
                throw new Exception("Term query returned a null cursor.");
            else if (!termCursor.moveToFirst())
                throw new Exception("Failed to get term with Id: " + termId);

            mTerm = Term.fromCursor(termCursor);
        } catch (Exception e) {
            Log.d(getLocalClassName(), "Failure displaying term information: " + e.toString());
            finish();
            return;
        }

        txtTitle.setText(mTerm.title);
        renderCourses();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (mCourseCursor != null && !mCourseCursor.isClosed())
            mCourseCursor.close();
        super.onDestroy();
    }

    public void onDateBtnClick(View view) {
        Calendar calendar = Calendar.getInstance();
        final int viewId = view.getId();
        if (viewId == R.id.term_startDate)
            calendar.setTime(mTerm.startDate);
        else if (viewId == R.id.term_endDate)
            calendar.setTime(mTerm.endDate);

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
                            mTerm.startDate = newDate;
                        else if (viewId == R.id.term_endDate)
                            mTerm.endDate = newDate;
                        updateFields();
                    }
                },
                year, month, day);
        Window window = dialog.getWindow();
        if (window != null)
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void updateFields() {
        mBtnStartDate.setText(mDateFormat.format(mTerm.startDate));
        mBtnEndDate.setText(mDateFormat.format(mTerm.endDate));

        ContentValues values = new ContentValues();
        values.put(WGUContract.Terms.TITLE, mTerm.title);
        values.put(WGUContract.Terms.START_DATE, mTerm.startDate.getTime()/1000);
        values.put(WGUContract.Terms.END_DATE, mTerm.endDate.getTime()/1000);

        if (mTermUri == null) {
            mTermUri = mContentResolver.insert(WGUContract.Terms.CONTENT_URI, values);
            if (mTermUri == null)
                Log.d(getLocalClassName(), "Failed inserting term.");
            else
                mTerm.id = ContentUris.parseId(mTermUri);
            return;
        }

        int updated = mContentResolver.update(mTermUri, values, null, null);
        if (updated == 0)
            Log.d(getLocalClassName(), "Failed to updated term with Id: " + ContentUris.parseId(mTermUri));
    }

    private void renderCourses() {
        if (mCourseCursor != null)
            mCourseCursor.close();

        mCourseCursor = mContentResolver.query(
                WGUContract.Courses.CONTENT_URI,
                WGUContract.Courses.COLUMNS,
                WGUContract.Courses.TERM_ID + " = ?",
                new String[]{String.valueOf(mTerm.id)},
                WGUContract.Courses.START_DATE + " ASC");

        if (mCourseCursor == null) {
            Log.d(getLocalClassName(), "Courses query failed.");
            finish();
            return;
        }

        ListView courseListView = findViewById(R.id.term_courses);
        CoursesCursorAdapter adapter = new CoursesCursorAdapter(this, mCourseCursor, 0, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View parentView = (View)view.getParent();
                TextView idView = parentView.findViewById(R.id.course_id);
                long courseId = Long.valueOf(idView.getText().toString(), 10);
                ContentValues values = new ContentValues();
                values.put(WGUContract.Courses.TERM_ID, (String)null);
                mContentResolver.update(
                        ContentUris.withAppendedId(WGUContract.Courses.CONTENT_URI, courseId),
                        values,
                        null,
                        null);
                renderCourses();
            }
        });
        courseListView.setAdapter(adapter);
    }

}
