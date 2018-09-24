package edu.wgu.gavin.c196.activities;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.wgu.gavin.c196.R;
import edu.wgu.gavin.c196.adapters.view.AssessmentViewAdapter;
import edu.wgu.gavin.c196.adapters.view.CourseMentorViewAdapter;
import edu.wgu.gavin.c196.adapters.view.CourseNoteViewAdapter;
import edu.wgu.gavin.c196.data.WGUContract;
import edu.wgu.gavin.c196.models.Assessment;
import edu.wgu.gavin.c196.models.Course;
import edu.wgu.gavin.c196.models.CourseMentor;
import edu.wgu.gavin.c196.models.CourseNote;
import edu.wgu.gavin.c196.models.Term;

public class EditCourseActivity extends AppCompatActivity {

    public final static int ADD_COURSE_MENTOR_REQUEST = 10;
    public final static int EDIT_COURSE_MENTOR_REQUEST = 11;
    public final static int ADD_ASSESSMENT_REQUEST = 12;
    public final static int EDIT_ASSESSMENT_REQUEST = 13;
    public final static int ADD_COURSE_NOTE_REQUEST = 14;
    public final static int EDIT_COURSE_NOTE_REQUEST = 15;

    private static DateFormat mDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

    private Course mCourse = new Course();
    private List<Term> mAvailableTerms = new ArrayList<>();

    private ContentResolver mContentResolver;
    private Uri mCourseUri;

    private Button mBtnStartDate;
    private Button mBtnEndDate;
    private Button mBtnStatus;
    private Button mBtnTerm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editcourse);

        mContentResolver = getContentResolver();

        mBtnStartDate = findViewById(R.id.course_startDate);
        mBtnEndDate = findViewById(R.id.course_endDate);
        mBtnStatus = findViewById(R.id.course_status);
        mBtnTerm = findViewById(R.id.course_term);
        EditText titleView = findViewById(R.id.course_title);
        titleView.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                mCourse.title = editable.toString();
                updateFields();
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
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

        try {
            try (Cursor courseCursor = mContentResolver.query(
                    ContentUris.withAppendedId(WGUContract.Courses.CONTENT_URI, courseId),
                    WGUContract.Courses.COLUMNS,
                    null, null, null)) {
                if (courseCursor == null)
                    throw new Exception("Course query returned a null cursor.");
                else if (!courseCursor.moveToFirst())
                    throw new Exception("Failed to get course with Id: " + courseCursor);

                mCourse = Course.fromCursor(courseCursor);
            }

            try (Cursor termsCursor = mContentResolver.query(
                    WGUContract.Terms.CONTENT_URI,
                    WGUContract.Terms.COLUMNS,
                    null, null, WGUContract.Terms.TITLE + " ASC")) {
                if (termsCursor == null)
                    throw new Exception("Available terms query returned a null cursor.");
                while (termsCursor.moveToNext())
                    mAvailableTerms.add(Term.fromCursor(termsCursor));
            }
        } catch (Exception e) {
            Log.d(getLocalClassName(), "Failure displaying course information: " + e.toString());
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        titleView.setText(mCourse.title);
        renderCourseMentors();
        renderAssessments();
        renderNotes();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case EDIT_COURSE_MENTOR_REQUEST:
                renderCourseMentors();
                break;
            case EDIT_ASSESSMENT_REQUEST:
                renderAssessments();
                break;
            case EDIT_COURSE_NOTE_REQUEST:
                renderNotes();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
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

    public void onTermBtnClick(View view) {
        final String[] termList = new String[mAvailableTerms.size()];
        for (int i = 0; i < mAvailableTerms.size(); i++)
            termList[i] = mAvailableTerms.get(i).title;

        new AlertDialog.Builder(this)
                .setTitle(R.string.term)
                .setItems(termList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Term term = mAvailableTerms.get(i);
                        mCourse.termId = term.id;
                        updateFields();
                    }
                })
                .show();
    }

    private void updateFields() {
        mBtnStartDate.setText(mDateFormat.format(mCourse.startDate));
        mBtnEndDate.setText(mDateFormat.format(mCourse.anticipatedEndDate));
        mBtnStatus.setText(mCourse.status.toString());
        if (mCourse.termId != null) {
            for (Term term : mAvailableTerms) {
                if (term.id != mCourse.termId)
                    continue;

                mBtnTerm.setText(term.title);
                break;
            }
        } else
            mBtnTerm.setText(R.string.unassigned);

        ContentValues values = new ContentValues();
        values.put(WGUContract.Courses.TITLE, mCourse.title);
        values.put(WGUContract.Courses.START_DATE, mCourse.startDate.getTime()/1000);
        values.put(WGUContract.Courses.END_DATE, mCourse.anticipatedEndDate.getTime()/1000);
        values.put(WGUContract.Courses.STATUS, mCourse.status.toString());
        values.put(WGUContract.Courses.TERM_ID, mCourse.termId);

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

    public void renderCourseMentors() {
        List<CourseMentor> mentors = new ArrayList<>();
        try (Cursor cursor = mContentResolver.query(
                WGUContract.CourseMentors.CONTENT_URI,
                WGUContract.CourseMentors.COLUMNS,
                WGUContract.CourseMentors.COURSE_ID + " = ?",
                new String[]{String.valueOf(mCourse.id)},
                WGUContract.CourseMentors.NAME + " ASC")) {
            if (cursor == null)
                throw new Exception("Course mentors query returned a null cursor.");
            while (cursor.moveToNext())
                mentors.add(CourseMentor.fromCursor(cursor));
        } catch (Exception e) {
            Log.d(getLocalClassName(), "Failed getting course mentors.\n" + e.toString());
            finish();
            return;
        }

        ViewGroup mentorListView = findViewById(R.id.course_mentors);
        mentorListView.removeAllViews();
        for (final CourseMentor mentor : mentors) {
            View mentorView = LayoutInflater.from(this).inflate(R.layout.listitem_coursementor, null);
            CourseMentorViewAdapter.from(mentor, mentorView,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(EditCourseActivity.this, EditCourseMentorActivity.class);
                            intent.putExtra(WGUContract.CourseMentors._ID, mentor.id);
                            startActivityForResult(intent, EDIT_COURSE_MENTOR_REQUEST);
                        }
                    },
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mContentResolver.delete(
                                    ContentUris.withAppendedId(WGUContract.CourseMentors.CONTENT_URI, mentor.id),
                                    null,
                                    null);
                            renderCourseMentors();
                        }
                    }).render();
            mentorListView.addView(mentorView);
        }
    }

    public void renderAssessments() {
        List<Assessment> assessments = new ArrayList<>();
        try (Cursor cursor = mContentResolver.query(
                WGUContract.Assessments.CONTENT_URI,
                WGUContract.Assessments.COLUMNS,
                WGUContract.Assessments.COURSE_ID + " = ?",
                new String[]{String.valueOf(mCourse.id)},
                WGUContract.Assessments.DUE_DATE + " ASC")) {
            if (cursor == null)
                throw new Exception("Course mentors query returned a null cursor.");
            while (cursor.moveToNext())
                assessments.add(Assessment.fromCursor(cursor));
        } catch (Exception e) {
            Log.d(getLocalClassName(), "Failed getting assessments.\n" + e.toString());
            finish();
            return;
        }

        ViewGroup assessmentListView = findViewById(R.id.course_assessments);
        assessmentListView.removeAllViews();
        for (final Assessment assessment : assessments) {
            View assessmentView = LayoutInflater.from(this).inflate(R.layout.listitem_assessment, null);
            AssessmentViewAdapter.from(assessment, assessmentView,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(EditCourseActivity.this, EditAssessmentActivity.class);
                            intent.putExtra(WGUContract.Assessments._ID, assessment.id);
                            startActivityForResult(intent, EDIT_ASSESSMENT_REQUEST);
                        }
                    },
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mContentResolver.delete(
                                    ContentUris.withAppendedId(WGUContract.Assessments.CONTENT_URI, assessment.id),
                                    null,
                                    null);
                            renderAssessments();
                        }
                    }).render();
            assessmentListView.addView(assessmentView);
        }
    }

    public void renderNotes() {
        List<CourseNote> notes = new ArrayList<>();
        try (Cursor cursor = mContentResolver.query(
                WGUContract.CourseNotes.CONTENT_URI,
                WGUContract.CourseNotes.COLUMNS,
                WGUContract.CourseNotes.COURSE_ID + " = ?",
                new String[]{String.valueOf(mCourse.id)},
                WGUContract.CourseNotes.NOTE + " ASC")) {
            if (cursor == null)
                throw new Exception("Course mentors query returned a null cursor.");
            while (cursor.moveToNext())
                notes.add(CourseNote.fromCursor(cursor));
        } catch (Exception e) {
            Log.d(getLocalClassName(), "Failed getting assessments.\n" + e.toString());
            finish();
            return;
        }

        ViewGroup notesListView = findViewById(R.id.course_notes);
        notesListView.removeAllViews();
        for (final CourseNote note : notes) {
            View notesView = LayoutInflater.from(this).inflate(R.layout.listitem_coursenote, null);
            CourseNoteViewAdapter.from(note, notesView,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(EditCourseActivity.this, EditNoteActivity.class);
                            intent.putExtra(WGUContract.CourseNotes._ID, note.id);
                            startActivityForResult(intent, EDIT_COURSE_NOTE_REQUEST);
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mContentResolver.delete(
                                    ContentUris.withAppendedId(WGUContract.CourseNotes.CONTENT_URI, note.id),
                                    null,
                                    null);
                            renderNotes();
                        }
                    }).render();
            notesListView.addView(notesView);
        }
    }

}
