package edu.wgu.gavin.c196.models;

import android.database.Cursor;

import edu.wgu.gavin.c196.data.WGUContract;

public class CourseNote {

    public long id;
    public long courseId;
    public String note;

    public static CourseNote fromCursor(Cursor cursor) {
        CourseNote note = new CourseNote();
        note.id = cursor.getLong(cursor.getColumnIndex(WGUContract.CourseNotes._ID));
        note.courseId = cursor.getLong(cursor.getColumnIndex(WGUContract.CourseNotes.COURSE_ID));
        note.note = cursor.getString(cursor.getColumnIndex(WGUContract.CourseNotes.NOTE));
        return note;
    }

}
