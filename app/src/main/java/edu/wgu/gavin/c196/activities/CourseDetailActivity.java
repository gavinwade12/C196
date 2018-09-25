package edu.wgu.gavin.c196.activities;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import edu.wgu.gavin.c196.R;
import edu.wgu.gavin.c196.adapters.view.CourseDetailViewAdapter;
import edu.wgu.gavin.c196.data.WGUContract;
import edu.wgu.gavin.c196.models.Assessment;
import edu.wgu.gavin.c196.models.Course;
import edu.wgu.gavin.c196.models.CourseMentor;
import edu.wgu.gavin.c196.models.CourseNote;
import edu.wgu.gavin.c196.models.Term;

public class CourseDetailActivity extends AppCompatActivity {

    public static int EDIT_COURSE_REQUEST = 6;

    private long mCourseId;
    private ContentResolver mContentResolver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coursedetail);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Log.d(getLocalClassName(), "Failed to get Bundle from intent.");
            setResult(RESULT_CANCELED);
            finish();
            return;
        }
        mCourseId = extras.getLong(WGUContract.Courses._ID);

        mContentResolver = getContentResolver();
        displayCourseInfo();
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
                Intent intent = new Intent(this, EditCourseActivity.class);
                intent.putExtra(WGUContract.Courses._ID, mCourseId);
                startActivityForResult(intent, EDIT_COURSE_REQUEST);
                return true;
            case R.id.action_delete:
                deleteCourse();
                setResult(RESULT_OK);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != EDIT_COURSE_REQUEST) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        if (resultCode != RESULT_OK)
            return;

        displayCourseInfo();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    public void displayCourseInfo() {
        Course course;
        String termTitle = "";
        try {
            try (Cursor courseCursor = mContentResolver.query(
                    ContentUris.withAppendedId(WGUContract.Courses.CONTENT_URI, mCourseId),
                    WGUContract.Courses.COLUMNS,
                    null,
                    null,
                    null)) {
                if (courseCursor == null)
                    throw new Exception("Course query returned a null cursor.");
                else if (!courseCursor.moveToFirst())
                    throw new Exception("Failed to find course with Id: " + mCourseId);

                course = Course.fromCursor(courseCursor);
            }

            if (course.termId != null) {
                try (Cursor termCursor = mContentResolver.query(
                        ContentUris.withAppendedId(WGUContract.Terms.CONTENT_URI, course.termId),
                        WGUContract.Terms.COLUMNS, null, null, null)) {
                    if (termCursor == null)
                        throw new Exception("Course term query returned a null cursor.");
                    else if (!termCursor.moveToFirst())
                        throw new Exception("Failed to find term with Id: " + course.termId);

                    termTitle = Term.fromCursor(termCursor).title;
                }
            }

            try (Cursor mentorCursor = mContentResolver.query(
                    WGUContract.CourseMentors.CONTENT_URI,
                    WGUContract.CourseMentors.COLUMNS,
                    WGUContract.CourseMentors.COURSE_ID + " = ?",
                    new String[]{String.valueOf(mCourseId)},
                    WGUContract.CourseMentors.NAME + " ASC")) {
                if (mentorCursor == null)
                    throw new Exception("Course mentor query returned a null cursor.");
                while (mentorCursor.moveToNext())
                    course.mentors.add(CourseMentor.fromCursor(mentorCursor));
            }

            try (Cursor noteCursor = mContentResolver.query(
                    WGUContract.CourseNotes.CONTENT_URI,
                    WGUContract.CourseNotes.COLUMNS,
                    WGUContract.CourseNotes.COURSE_ID + " = ?",
                    new String[]{String.valueOf(mCourseId)},
                    WGUContract.CourseNotes.NOTE + " ASC")) {
                if (noteCursor == null)
                    throw new Exception("Course note query returned a null cursor.");
                while (noteCursor.moveToNext())
                    course.notes.add(CourseNote.fromCursor(noteCursor));
            }

            try (Cursor assessmentCursor = mContentResolver.query(
                    WGUContract.Assessments.CONTENT_URI,
                    WGUContract.Assessments.COLUMNS,
                    WGUContract.Assessments.COURSE_ID + " = ?",
                    new String[]{String.valueOf(mCourseId)},
                    WGUContract.Assessments.TYPE)) {
                if (assessmentCursor == null)
                    throw new Exception("Course assessment query returned a null cursor.");
                while (assessmentCursor.moveToNext())
                    course.assessments.add(Assessment.fromCursor(assessmentCursor));
            }
        } catch(Exception e) {
            Log.d(getLocalClassName(), "Failed getting course information.\n" + e.toString());
            finish();
            return;
        }

        CourseDetailViewAdapter.from(this, course, termTitle, findViewById(android.R.id.content)).render();
    }

    private void deleteCourse() {
        mContentResolver.delete(
                WGUContract.Assessments.CONTENT_URI,
                WGUContract.Assessments.COURSE_ID + " = ?",
                new String[]{String.valueOf(mCourseId)});

        mContentResolver.delete(
                WGUContract.CourseMentors.CONTENT_URI,
                WGUContract.CourseMentors.COURSE_ID + " = ?",
                new String[]{String.valueOf(mCourseId)});

        mContentResolver.delete(
                WGUContract.CourseNotes.CONTENT_URI,
                WGUContract.CourseNotes.COURSE_ID + " = ?",
                new String[]{String.valueOf(mCourseId)});

        mContentResolver.delete(
                ContentUris.withAppendedId(WGUContract.Courses.CONTENT_URI, mCourseId),
                null,
                null);
    }
}
