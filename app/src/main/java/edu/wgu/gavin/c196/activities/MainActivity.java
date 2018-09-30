package edu.wgu.gavin.c196.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import edu.wgu.gavin.c196.R;
import edu.wgu.gavin.c196.data.WGUContract;
import edu.wgu.gavin.c196.models.Course;

public class MainActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private TextView mCompletedCourses;
    private TextView mTotalCourses;
    private ContentResolver mContentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = findViewById(R.id.progress_bar);
        mCompletedCourses = findViewById(R.id.completed_courses);
        mTotalCourses = findViewById(R.id.total_courses);

        mContentResolver = getContentResolver();

        updateProgress();
    }

    @Override
    protected void onResume() {
        updateProgress();
        super.onResume();
    }

    public void onBtnClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btnViewTerms:
                intent = new Intent(this, TermsActivity.class);
                break;
            case R.id.btnViewCourses:
                intent = new Intent(this, CoursesActivity.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    public void updateProgress() {
        int totalCourses;
        int completedCourses;
        try {
            try (Cursor cursor = mContentResolver.query(
                    WGUContract.Courses.CONTENT_URI,
                    WGUContract.Courses.COLUMNS,
                    null, null, null)) {
                if (cursor == null)
                    throw new Exception("Courses query returned a null cursor.");

                totalCourses = cursor.getCount();
            }

            if (totalCourses == 0) {
                mProgressBar.setProgress(0);
                mCompletedCourses.setText("0");
                mTotalCourses.setText("0");
                return;
            }

            try (Cursor cursor = mContentResolver.query(
                    WGUContract.Courses.CONTENT_URI,
                    WGUContract.Courses.COLUMNS,
                    WGUContract.Courses.STATUS + " = ?",
                    new String[]{Course.Status.Completed.toString()},
                    null)) {
                if (cursor == null)
                    throw new Exception("Completed courses query returned a null cursor.");

                completedCourses = cursor.getCount();
            }
        } catch (Exception e) {
            Log.d(getLocalClassName(), "Failed to get course information for progress.\n" + e.toString());
            return;
        }

        mProgressBar.setProgress(100 * completedCourses / totalCourses);
        mCompletedCourses.setText(String.valueOf(completedCourses));
        mTotalCourses.setText(String.valueOf(totalCourses));
    }
}
