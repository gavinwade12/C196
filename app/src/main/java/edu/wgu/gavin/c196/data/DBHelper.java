package edu.wgu.gavin.c196.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import edu.wgu.gavin.c196.models.ScheduledAlert;

import static edu.wgu.gavin.c196.data.WGUContract.*;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "wgu.db";
    private static final int DATABASE_VERSION = 2;

    private static final String CREATE_ASSESSMENTS =
            "CREATE TABLE " + Assessments.TABLE_NAME + " ( " +
                    Assessments._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Assessments.NOTES + " TEXT, " +
                    Assessments.TYPE + " TEXT);";
    private static final String CREATE_COURSES =
            "CREATE TABLE " + Courses.TABLE_NAME + " ( " +
                    Courses._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Courses.TITLE + " TEXT, " +
                    Courses.NOTES + " TEXT, " +
                    Courses.STATUS + " TEXT, " +
                    Courses.START_DATE + " INTEGER, " +
                    Courses.END_DATE + " INTEGER);";
    private static final String CREATE_COURSE_MENTORS =
            "CREATE TABLE " + CourseMentors.TABLE_NAME + " ( " +
                    CourseMentors._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CourseMentors.NAME + " TEXT, " +
                    CourseMentors.EMAIL_ADDRESS + " TEXT, " +
                    CourseMentors.PHONE_NUMBER + " TEXT);";
    private static final String CREATE_SCHEDULED_ALERTS =
            "CREATE TABLE " + ScheduledAlerts.TABLE_NAME + " ( " +
                    ScheduledAlerts._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ScheduledAlerts.NAME + " TEXT, " +
                    ScheduledAlerts.DATE + " INTEGER);";
    private static final String CREATE_TERMS =
            "CREATE TABLE " + Terms.TABLE_NAME + " ( " +
                    Terms._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Terms.TITLE + " TEXT, " +
                    Terms.START_DATE + " INTEGER, " +
                    Terms.END_DATE + " INTEGER);";
    private static final String CREATE_COURSES_COURSE_MENTORS_MAPPING =
            "CREATE TABLE " + CoursesCourseMentorsMapping.TABLE_NAME + " ( " +
                    CoursesCourseMentorsMapping._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CoursesCourseMentorsMapping.COURSE_ID + " INTEGER, " +
                    CoursesCourseMentorsMapping.COURSE_MENTOR_ID + " INTEGER, " +
                    "FOREIGN KEY (" + CoursesCourseMentorsMapping.COURSE_ID + ") " +
                        "REFERENCES " + Courses.TABLE_NAME + "(" + Courses._ID + "), " +
                    "FOREIGN KEY (" + CoursesCourseMentorsMapping.COURSE_MENTOR_ID + " ) " +
                        "REFERENCES " + CourseMentors.TABLE_NAME + "(" + CourseMentors._ID + "));";
    private static final String CREATE_COURSES_ASSESSMENTS_MAPPING =
            "CREATE TABLE " + CoursesAssessmentsMapping.TABLE_NAME + " ( " +
                    CoursesAssessmentsMapping._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CoursesAssessmentsMapping.COURSE_ID + " INTEGER, " +
                    CoursesAssessmentsMapping.ASSESSMENT_ID + " INTEGER, " +
                    "FOREIGN KEY (" + CoursesAssessmentsMapping.COURSE_ID + ") " +
                        "REFERENCES " + Courses.TABLE_NAME + "(" + Courses._ID + "), " +
                    "FOREIGN KEY (" + CoursesAssessmentsMapping.ASSESSMENT_ID + ") " +
                        "REFERENCES " + Assessments.TABLE_NAME + "(" + Assessments._ID + "));";
    private static final String CREATE_TERMS_COURSES_MAPPING =
            "CREATE TABLE " + TermsCoursesMapping.TABLE_NAME + " ( " +
                TermsCoursesMapping._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TermsCoursesMapping.TERM_ID + " INTEGER, " +
                TermsCoursesMapping.COURSE_ID + " INTEGER, " +
                "FOREIGN KEY (" + TermsCoursesMapping.TERM_ID + " ) " +
                    "REFERENCES " + Terms.TABLE_NAME + "(" + Terms._ID + "), " +
                "FOREIGN KEY (" + TermsCoursesMapping.COURSE_ID + ") " +
                    "REFERENCES " + Courses.TABLE_NAME + "(" + Courses._ID + "));";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ASSESSMENTS);
        db.execSQL(CREATE_COURSES);
        db.execSQL(CREATE_COURSE_MENTORS);
        db.execSQL(CREATE_SCHEDULED_ALERTS);
        db.execSQL(CREATE_TERMS);
        db.execSQL(CREATE_COURSES_COURSE_MENTORS_MAPPING);
        db.execSQL(CREATE_COURSES_ASSESSMENTS_MAPPING);
        db.execSQL(CREATE_TERMS_COURSES_MAPPING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String[] tables = {Assessments.TABLE_NAME, Courses.TABLE_NAME, CourseMentors.TABLE_NAME,
                ScheduledAlerts.TABLE_NAME, Terms.TABLE_NAME, CoursesCourseMentorsMapping.TABLE_NAME,
                CoursesAssessmentsMapping.TABLE_NAME, TermsCoursesMapping.TABLE_NAME};
        for (int i = 0; i < tables.length; i++)
            db.execSQL("DROP TABLE " + tables[i]);
        onCreate(db);
    }
}
