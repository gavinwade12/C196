package edu.wgu.gavin.c196.models;

import android.database.Cursor;

import edu.wgu.gavin.c196.data.WGUContract;

public class CourseMentor {

    public long id;
    public long courseId;
    public String name;
    public String phoneNumber;
    public String emailAddress;

    public static CourseMentor fromCursor(Cursor cursor) {
        CourseMentor mentor = new CourseMentor();
        mentor.id = cursor.getLong(cursor.getColumnIndex(WGUContract.CourseMentors._ID));
        mentor.courseId = cursor.getLong(cursor.getColumnIndex(WGUContract.CourseMentors.COURSE_ID));
        mentor.name = cursor.getString(cursor.getColumnIndex(WGUContract.CourseMentors.NAME));
        mentor.phoneNumber = cursor.getString(cursor.getColumnIndex(WGUContract.CourseMentors.PHONE_NUMBER));
        mentor.emailAddress = cursor.getString(cursor.getColumnIndex(WGUContract.CourseMentors.EMAIL_ADDRESS));
        return mentor;
    }

}
