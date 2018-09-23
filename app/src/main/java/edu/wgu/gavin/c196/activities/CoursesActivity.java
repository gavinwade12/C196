package edu.wgu.gavin.c196.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import edu.wgu.gavin.c196.R;
import edu.wgu.gavin.c196.adapters.list.CourseGroupExpandableListAdapter;
import edu.wgu.gavin.c196.data.WGUContract;
import edu.wgu.gavin.c196.models.Course;
import edu.wgu.gavin.c196.models.Term;

public class CoursesActivity extends AppCompatActivity {

    public static int VIEW_COURSE_REQUEST = 4;
    public static int CREATE_COURSE_REQUEST = 5;

    private ContentResolver mContentResolver;
    private ExpandableListView mCourseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        mContentResolver = getContentResolver();
        mCourseList = findViewById(R.id.courseList);
        displayCourses();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.action_new)
            return super.onOptionsItemSelected(item);

        Intent intent = new Intent(this, EditCourseActivity.class);
        startActivityForResult(intent, CREATE_COURSE_REQUEST);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != VIEW_COURSE_REQUEST && requestCode != CREATE_COURSE_REQUEST) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        if (resultCode != RESULT_OK)
            return;

        displayCourses();
    }

    private void displayCourses() {
        List<Term> termList = new ArrayList<>();

        // Get courses not assigned to a term
        try (Cursor courseCursor = mContentResolver.query(
                WGUContract.Courses.CONTENT_URI,
                WGUContract.Courses.COLUMNS,
                WGUContract.Courses.TERM_ID + " IS NULL",
                null,
                WGUContract.Courses.START_DATE + " ASC")) {
            if (courseCursor == null) {
                Log.d(getLocalClassName(), "Unassigned course query returned a null cursor.");
                finish();
                return;
            }

            Term defaultTerm = new Term();
            defaultTerm.title = "Unassigned Courses";
            while (courseCursor.moveToNext())
                defaultTerm.courses.add(Course.fromCursor(courseCursor));

            termList.add(defaultTerm);
        }

        // Get all terms and assigned courses
        try (Cursor termCursor = mContentResolver.query(WGUContract.Terms.CONTENT_URI,
                WGUContract.Terms.COLUMNS,
                null,
                null,
                WGUContract.Terms.START_DATE + " ASC")) {
            if (termCursor == null)
                throw new Exception("Term query returned a null cursor.");

            while (termCursor.moveToNext()) {
                Term term = Term.fromCursor(termCursor);

                try (Cursor courseCursor = mContentResolver.query(
                        WGUContract.Courses.CONTENT_URI,
                        WGUContract.Courses.COLUMNS,
                        WGUContract.Courses.TERM_ID + " = ?",
                        new String[]{String.valueOf(term.id)},
                        WGUContract.Courses.START_DATE + " ASC")) {
                    if (courseCursor == null)
                        throw new Exception("Course query returned a null cursor.");

                    while (courseCursor.moveToNext())
                        term.courses.add(Course.fromCursor(courseCursor));
                }

                termList.add(term);
            }
        } catch(Exception e) {
            Log.d(getLocalClassName(), "Failed getting terms.\n" + e.toString());
            finish();
            return;
        }

        CourseGroupExpandableListAdapter adapter = new CourseGroupExpandableListAdapter(this, termList);
        mCourseList.setAdapter(adapter);
    }

}
