package edu.wgu.gavin.c196.activities;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import edu.wgu.gavin.c196.models.Course;

public class EditCourseActivity extends AppCompatActivity {

    private static DateFormat mDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

    private Course mCourse = new Course();

    private ContentResolver mContentResolver;
    private Uri mCourseUri;

    private Button mBtnStartDate;
    private Button mBtnEndDate;
    private Button mBtnStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editcourse);

        mContentResolver = getContentResolver();

        mBtnStartDate = findViewById(R.id.course_startDate);
        mBtnEndDate = findViewById(R.id.course_endDate);
        mBtnStatus = findViewById(R.id.course_status);
        EditText titleView = findViewById(R.id.course_title);
        titleView.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                mCourse.title = editable.toString();
                updateFields();
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras == null || !extras.containsKey(WGUContract.Courses._ID)) {
            Date currentDate = Calendar.getInstance().getTime();
            mCourse.startDate = currentDate;
            mCourse.anticipatedEndDate = currentDate;
            mCourse.title = "";
            titleView.setText(mCourse.title);
            return;
        }

        long courseId = extras.getLong(WGUContract.Courses._ID);
        mCourseUri = ContentUris.withAppendedId(WGUContract.Courses.CONTENT_URI, courseId);

        try (Cursor courseCursor = mContentResolver.query(
                ContentUris.withAppendedId(WGUContract.Courses.CONTENT_URI, courseId),
                WGUContract.Courses.COLUMNS,
                null, null, null)) {
            if (courseCursor == null)
                throw new Exception("Course query returned a null cursor.");
            else if (!courseCursor.moveToFirst())
                throw new Exception("Failed to get course with Id: " + courseCursor);

            mCourse = Course.fromCursor(courseCursor);
        } catch (Exception e) {
            Log.d(getLocalClassName(), "Failure displaying course information: " + e.toString());
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        titleView.setText(mCourse.title);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    public void onDateBtnClick(View view) {
        Calendar calendar = Calendar.getInstance();
        final int viewId = view.getId();
        if (viewId == R.id.course_startDate)
            calendar.setTime(mCourse.startDate);
        else if (viewId == R.id.course_endDate)
            calendar.setTime(mCourse.anticipatedEndDate);

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
                        if (viewId == R.id.course_startDate)
                            mCourse.startDate = newDate;
                        else if (viewId == R.id.course_endDate)
                            mCourse.anticipatedEndDate = newDate;
                        updateFields();
                    }
                },
                year, month, day);
        Window window = dialog.getWindow();
        if (window != null)
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void onStatusBtnClick(View view) {
        final String[] statusList = new String[]{
                Course.Status.PlanToTake.toString(),
                Course.Status.InProgress.toString(),
                Course.Status.Completed.toString(),
                Course.Status.Dropped.toString()
        };

        new AlertDialog.Builder(this)
                .setTitle(R.string.status)
                .setItems(statusList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mCourse.status = Course.Status.fromString(statusList[i]);
                        updateFields();
                    }
                })
                .show();
    }

    private void updateFields() {
        mBtnStartDate.setText(mDateFormat.format(mCourse.startDate));
        mBtnEndDate.setText(mDateFormat.format(mCourse.anticipatedEndDate));
        mBtnStatus.setText(mCourse.status.toString());

        ContentValues values = new ContentValues();
        values.put(WGUContract.Courses.TITLE, mCourse.title);
        values.put(WGUContract.Courses.START_DATE, mCourse.startDate.getTime()/1000);
        values.put(WGUContract.Courses.END_DATE, mCourse.anticipatedEndDate.getTime()/1000);
        values.put(WGUContract.Courses.STATUS, mCourse.status.toString());

        if (mCourseUri == null) {
            mCourseUri = mContentResolver.insert(WGUContract.Courses.CONTENT_URI, values);
            if (mCourseUri == null)
                Log.d(getLocalClassName(), "Failed to insert course.");
            return;
        }

        int a = mContentResolver.update(mCourseUri, values, null, null);
        if (a == 0)
            Log.d(getLocalClassName(), "Failed to update course with Id: " + ContentUris.parseId(mCourseUri));
    }

}
