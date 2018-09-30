package edu.wgu.gavin.c196.activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import edu.wgu.gavin.c196.data.AlertReceiver;
import edu.wgu.gavin.c196.data.WGUContract;
import edu.wgu.gavin.c196.models.Assessment;

public class EditAssessmentActivity extends AppCompatActivity {

    private static DateFormat mDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

    private Assessment mAssessment = new Assessment();
    private Uri mAssessmentUri;
    private ContentResolver mContentResolver;

    private Button mTypeBtn;
    private Button mStartDateBtn;
    private Button mDueDateBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editassessment);

        mContentResolver = getContentResolver();

        EditText titleInput = findViewById(R.id.assessment_title);
        mTypeBtn = findViewById(R.id.assessment_type);
        mStartDateBtn = findViewById(R.id.assessment_startDate);
        mDueDateBtn = findViewById(R.id.assessment_dueDate);

        titleInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mAssessment.title = editable.toString();
                updateFields();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras == null || !extras.containsKey(WGUContract.Assessments.COURSE_ID)) {
            Log.d(getLocalClassName(), "Could not get course Id for editing assessment.");
            finish();
            return;
        }
        mAssessment.courseId = extras.getLong(WGUContract.Assessments.COURSE_ID);

        if (extras.containsKey(WGUContract.Assessments._ID)) {
            long id = extras.getLong(WGUContract.Assessments._ID);
            mAssessmentUri = ContentUris.withAppendedId(WGUContract.Assessments.CONTENT_URI, id);
            try (Cursor cursor = mContentResolver.query(
                    mAssessmentUri,
                    WGUContract.Assessments.COLUMNS,
                    null, null, null)) {
                if (cursor == null)
                    throw new Exception("Assessment query returned a null cursor.");
                if (!cursor.moveToFirst())
                    throw new Exception("Failed to find assessment with Id: " + id);
                mAssessment = Assessment.fromCursor(cursor);
            } catch (Exception e) {
                Log.d(getLocalClassName(), e.toString());
                finish();
                return;
            }
        } else {
            mAssessment.title = "";
            mAssessment.type = Assessment.Type.Performance;
            Date now = Calendar.getInstance().getTime();
            mAssessment.startDate = now;
            mAssessment.dueDate = now;
        }

        titleInput.setText(mAssessment.title);
    }

    public void onTypeBtnClick(View view) {
        final String[] items = new String[]{Assessment.Type.Performance.toString(),
                Assessment.Type.Objective.toString()};
        new AlertDialog.Builder(this)
                .setTitle(R.string.type)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAssessment.type = Assessment.Type.fromString(items[i]);
                        updateFields();
                    }
                })
                .show();
    }

    public void onDateBtnClick(View view) {
        Calendar calendar = Calendar.getInstance();
        final boolean isStartDate = view == mStartDateBtn;
        if (isStartDate)
            calendar.setTime(mAssessment.startDate);
        else
            calendar.setTime(mAssessment.dueDate);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, day);
                        if (isStartDate)
                            mAssessment.startDate = calendar.getTime();
                        else
                            mAssessment.dueDate = calendar.getTime();
                        updateFields();
                    }
                },
                year, month, day);
        Window window = dialog.getWindow();
        if (window != null)
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void onScheduleAlertBtnClick(View view) {
        final String[] items = new String[]{"Start Date", "Due Date"};
        new AlertDialog.Builder(this)
                .setTitle(R.string.schedule_alert)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Date alertDate = new Date();
                        String time = "";
                        switch (i) {
                            case 0:
                                alertDate = mAssessment.startDate;
                                time = "start";
                                break;
                            case 1:
                                alertDate = mAssessment.dueDate;
                                time = "due date";
                                break;
                        }
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(alertDate);

                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        if (alarmManager == null) {
                            Log.d(getLocalClassName(), "Failed to get alarm manager system service.");
                            return;
                        }

                        Intent intent = new Intent(EditAssessmentActivity.this, AlertReceiver.class);
                        intent.putExtra("event", mAssessment.title);
                        intent.putExtra("time", time);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                EditAssessmentActivity.this, 0, intent, 0);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    }
                })
                .show();
    }

    private void updateFields() {
        mTypeBtn.setText(mAssessment.type.toString());
        mStartDateBtn.setText(mDateFormat.format(mAssessment.startDate));
        mDueDateBtn.setText(mDateFormat.format(mAssessment.dueDate));

        ContentValues values = new ContentValues();
        values.put(WGUContract.Assessments.COURSE_ID, mAssessment.courseId);
        values.put(WGUContract.Assessments.TITLE, mAssessment.title);
        values.put(WGUContract.Assessments.TYPE, mAssessment.type.toString());
        values.put(WGUContract.Assessments.START_DATE, mAssessment.startDate.getTime()/1000);
        values.put(WGUContract.Assessments.DUE_DATE, mAssessment.dueDate.getTime()/1000);

        if (mAssessmentUri != null) {
            mContentResolver.update(mAssessmentUri, values, null, null);
            return;
        }

        mAssessmentUri = mContentResolver.insert(WGUContract.Assessments.CONTENT_URI, values);
        if (mAssessmentUri == null) {
            Log.d(getLocalClassName(), "Failed to insert assessment.");
            finish();
            return;
        }
        mAssessment.id = ContentUris.parseId(mAssessmentUri);
    }
}
