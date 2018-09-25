package edu.wgu.gavin.c196.activities;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import edu.wgu.gavin.c196.R;
import edu.wgu.gavin.c196.data.WGUContract;
import edu.wgu.gavin.c196.models.CourseMentor;

public class EditCourseMentorActivity extends AppCompatActivity {

    private ContentResolver mContentResolver;
    private CourseMentor mCourseMentor = new CourseMentor();

    private Uri mCourseMentorUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editmentor);

        mContentResolver = getContentResolver();

        Bundle extras = getIntent().getExtras();
        if (extras == null || !extras.containsKey(WGUContract.CourseMentors.COURSE_ID)) {
            Log.d(getLocalClassName(), "Could not get the course Id for editing course mentor.");
            finish();
            return;
        }
        mCourseMentor.courseId = extras.getLong(WGUContract.CourseMentors.COURSE_ID);

        EditText nameInput = findViewById(R.id.mentor_name);
        EditText phoneInput = findViewById(R.id.mentor_phone);
        EditText emailInput = findViewById(R.id.mentor_email);

        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mCourseMentor.name = editable.toString();
                updateMentor();
            }
        });
        phoneInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mCourseMentor.phoneNumber = editable.toString();
                updateMentor();
            }
        });
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mCourseMentor.emailAddress = editable.toString();
                updateMentor();
            }
        });

        if (extras.containsKey(WGUContract.CourseMentors._ID)) {
            long id = extras.getLong(WGUContract.CourseMentors._ID);
            mCourseMentorUri = ContentUris.withAppendedId(WGUContract.CourseMentors.CONTENT_URI, id);
            try (Cursor cursor = mContentResolver.query(
                    mCourseMentorUri,
                    WGUContract.CourseMentors.COLUMNS,
                    null, null, null)) {
                if (cursor == null)
                    throw new Exception("Course mentor query returned a null cursor.");
                if (!cursor.moveToFirst())
                    throw new Exception("Failed to find course mentor with Id: " + id);
                mCourseMentor = CourseMentor.fromCursor(cursor);
            } catch (Exception e) {
                Log.d(getLocalClassName(), e.toString());
                finish();
                return;
            }
        } else {
            mCourseMentor.name = "";
            mCourseMentor.phoneNumber = "";
            mCourseMentor.emailAddress = "";
        }

        nameInput.setText(mCourseMentor.name);
        phoneInput.setText(mCourseMentor.phoneNumber);
        emailInput.setText(mCourseMentor.emailAddress);
    }

    private void updateMentor() {
        ContentValues values = new ContentValues();
        values.put(WGUContract.CourseMentors.COURSE_ID, mCourseMentor.courseId);
        values.put(WGUContract.CourseMentors.NAME, mCourseMentor.name);
        values.put(WGUContract.CourseMentors.PHONE_NUMBER, mCourseMentor.phoneNumber);
        values.put(WGUContract.CourseMentors.EMAIL_ADDRESS, mCourseMentor.emailAddress);

        if (mCourseMentorUri != null) {
            mContentResolver.update(mCourseMentorUri, values, null, null);
            return;
        }

        mCourseMentorUri = mContentResolver.insert(WGUContract.CourseMentors.CONTENT_URI, values);
        if (mCourseMentorUri == null) {
            Log.d(getLocalClassName(), "Failed to insert course mentor.");
            finish();
        }
    }
}
