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
import edu.wgu.gavin.c196.models.CourseNote;

public class EditNoteActivity extends AppCompatActivity {

    private ContentResolver mContentResolver;
    private CourseNote mCourseNote = new CourseNote();
    private Uri mCourseNoteUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnote);

        mContentResolver = getContentResolver();

        Bundle extras = getIntent().getExtras();
        if (extras == null || !extras.containsKey(WGUContract.CourseNotes.COURSE_ID)) {
            Log.d(getLocalClassName(), "Could not get course Id for editing note.");
            finish();
            return;
        }
        mCourseNote.courseId = extras.getLong(WGUContract.CourseNotes.COURSE_ID);

        if (extras.containsKey(WGUContract.CourseNotes._ID)) {
            long id = extras.getLong(WGUContract.CourseNotes._ID);
            mCourseNoteUri = ContentUris.withAppendedId(WGUContract.CourseNotes.CONTENT_URI, id);
            try (Cursor cursor = mContentResolver.query(
                    mCourseNoteUri,
                    WGUContract.CourseNotes.COLUMNS,
                    null, null, null)) {
                if (cursor == null)
                    throw new Exception("Course note query returned a null cursor.");
                if (!cursor.moveToFirst())
                    throw new Exception("Failed to find note with Id: " + id);
                mCourseNote = CourseNote.fromCursor(cursor);
            } catch (Exception e) {
                Log.d(getLocalClassName(), e.toString());
                finish();
                return;
            }
        } else {
            mCourseNote.note = "";
        }

        EditText noteInput = findViewById(R.id.note);
        noteInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mCourseNote.note = editable.toString();
                updateNote();
            }
        });

        noteInput.setText(mCourseNote.note);
    }

    private void updateNote() {
        ContentValues values = new ContentValues();
        values.put(WGUContract.CourseNotes.COURSE_ID, mCourseNote.courseId);
        values.put(WGUContract.CourseNotes.NOTE, mCourseNote.note);

        if (mCourseNoteUri != null) {
            mContentResolver.update(mCourseNoteUri, values, null, null);
            return;
        }

        mCourseNoteUri = mContentResolver.insert(WGUContract.CourseNotes.CONTENT_URI, values);
        if (mCourseNoteUri == null) {
            Log.d(getLocalClassName(), "Failed to insert course note.");
            finish();
            return;
        }
        mCourseNote.id = ContentUris.parseId(mCourseNoteUri);
    }
}
