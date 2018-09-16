package edu.wgu.gavin.c196.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import edu.wgu.gavin.c196.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            case R.id.btnViewAssessments:
                intent = new Intent(this, AssessmentsActivity.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
