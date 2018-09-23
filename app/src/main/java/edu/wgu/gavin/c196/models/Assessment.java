package edu.wgu.gavin.c196.models;

import android.database.Cursor;

import java.util.Date;
import java.util.List;

import edu.wgu.gavin.c196.data.WGUContract;

public class Assessment {

    public long id;
    public long courseId;
    public String title;
    public Type type;
    public Date dueDate;
    public List<CourseNote> courseNotes;

    public static Assessment fromCursor(Cursor cursor) {
        Assessment assessment = new Assessment();
        assessment.id = cursor.getLong(cursor.getColumnIndex(WGUContract.Assessments._ID));
        assessment.courseId = cursor.getLong(cursor.getColumnIndex(WGUContract.Assessments.COURSE_ID));
        assessment.title = cursor.getString(cursor.getColumnIndex(WGUContract.Assessments.TITLE));
        assessment.type = Type.fromString(cursor.getString(cursor.getColumnIndex(WGUContract.Assessments.TYPE)));
        assessment.dueDate = new Date(cursor.getLong(cursor.getColumnIndex(WGUContract.Assessments.DUE_DATE)) * 1000);
        return assessment;
    }

    public enum Type {
        Objective,
        Performance;

        @Override
        public String toString() {
            if (this == Objective)
                return "Objective";
            else if (this == Performance)
                return "Performance";
            return "";
        }

        public static Type fromString(String type) {
            switch (type) {
                case "Objective":
                    return Objective;
                case "Performance":
                    return Performance;
                default:
                    return null;
            }
        }
    }
}
