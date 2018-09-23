package edu.wgu.gavin.c196.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import edu.wgu.gavin.c196.models.Assessment;
import edu.wgu.gavin.c196.models.Course;

import static edu.wgu.gavin.c196.data.WGUContract.*;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "wgu.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TERMS =
            "CREATE TABLE " + Terms.TABLE_NAME + " ( " +
                    Terms._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Terms.TITLE + " TEXT, " +
                    Terms.START_DATE + " INTEGER, " +
                    Terms.END_DATE + " INTEGER);";

    private static final String CREATE_COURSES =
            "CREATE TABLE " + Courses.TABLE_NAME + " ( " +
                    Courses._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Courses.TITLE + " TEXT, " +
                    Courses.STATUS + " TEXT, " +
                    Courses.START_DATE + " INTEGER, " +
                    Courses.END_DATE + " INTEGER, " +
                    Courses.TERM_ID + " INTEGER, " +
                    "FOREIGN KEY (" + Courses.TERM_ID + ") REFERENCES " +
                        Terms.TABLE_NAME + "(" + Terms._ID + "));";

    private static final String CREATE_COURSE_NOTES =
            "CREATE TABLE " + CourseNotes.TABLE_NAME + " ( " +
                    CourseNotes._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CourseNotes.NOTE + " TEXT, " +
                    CourseNotes.COURSE_ID + " INTEGER, " +
                    "FOREIGN KEY (" + CourseNotes.COURSE_ID + ") REFERENCES " +
                        Courses.TABLE_NAME + "(" + Courses._ID + "));";

    private static final String CREATE_ASSESSMENTS =
            "CREATE TABLE " + Assessments.TABLE_NAME + " ( " +
                    Assessments._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Assessments.TITLE + " TEXT, " +
                    Assessments.TYPE + " TEXT, " +
                    Assessments.DUE_DATE + " INTEGER, " +
                    Assessments.COURSE_ID + " INTEGER, " +
                    "FOREIGN KEY (" + Assessments.COURSE_ID + ") REFERENCES " +
                    Courses.TABLE_NAME + "(" + Courses._ID + "));";


    private static final String CREATE_COURSE_MENTORS =
            "CREATE TABLE " + CourseMentors.TABLE_NAME + " ( " +
                    CourseMentors._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CourseMentors.NAME + " TEXT, " +
                    CourseMentors.EMAIL_ADDRESS + " TEXT, " +
                    CourseMentors.PHONE_NUMBER + " TEXT, " +
                    CourseMentors.COURSE_ID + " INTEGER, " +
                    "FOREIGN KEY (" + CourseMentors.COURSE_ID + ") REFERENCES " +
                        Courses.TABLE_NAME + "(" + Courses._ID + "));";

    private static final String CREATE_SCHEDULED_ALERTS =
            "CREATE TABLE " + ScheduledAlerts.TABLE_NAME + " ( " +
                    ScheduledAlerts._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ScheduledAlerts.NAME + " TEXT, " +
                    ScheduledAlerts.DATE + " INTEGER);";

    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        context.deleteDatabase(DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TERMS);
        db.execSQL(CREATE_COURSES);
        db.execSQL(CREATE_COURSE_NOTES);
        db.execSQL(CREATE_ASSESSMENTS);
        db.execSQL(CREATE_COURSE_MENTORS);
        db.execSQL(CREATE_SCHEDULED_ALERTS);

        seed(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private void seed(SQLiteDatabase db) {
        Course.Status[] courseStatuses = {Course.Status.InProgress, Course.Status.Completed,
                Course.Status.Dropped, Course.Status.PlanToTake};
        Assessment.Type[] assessmentTypes = {Assessment.Type.Objective, Assessment.Type.Performance};
        for (int i = 1; i < 11; i++) {
            db.execSQL("INSERT INTO terms(title, start_date, end_date) VALUES ('term " + i +
                    "', strftime('%s', 'now'), strftime('%s', 'now'));");
            for (int j = 1; j < 10; j++) {
                db.execSQL("INSERT INTO courses(title, status, start_date, end_date, term_id) VALUES " +
                    "('course_"+i+"_"+j+"', '"+courseStatuses[j%4].toString()+"', strftime('%s', 'now'), strftime('%s', 'now'), "+i+");");
                long courseId = ((i-1) * 9) + j;
                for (int k = 1; k < 4; k++) {
                    db.execSQL("INSERT INTO course_mentors(course_id, name, email_address, phone_number) VALUES " +
                        "(" + courseId + ", 'mentor_" + j + "_" + k + "', 'test" + k + "@email.com', '419-555-123" + k + "');");

                    db.execSQL("INSERT INTO assessments(title, type, due_date, course_id) VALUES " +
                        "('assessment_"+j+"_"+k+"', '"+assessmentTypes[k%2].toString()+"', strftime('%s', 'now'), "+courseId+");");

                    db.execSQL("INSERT INTO course_notes(note, course_id) VALUES ('note_"+j+"_"+k+"', "+courseId+");");
                }
            }
        }
    }

}
