package edu.wgu.gavin.c196.models;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.wgu.gavin.c196.data.WGUContract;

public class Course {
    public long id;
    public String title;
    public Date startDate;
    public Date anticipatedEndDate;
    public Status status;
    public Long termId;
    public List<CourseMentor> mentors;
    public List<Assessment> assessments;
    public List<CourseNote> notes;

    public Course() {
        status = Status.PlanToTake;
        mentors = new ArrayList<>();
        assessments = new ArrayList<>();
        notes = new ArrayList<>();
    }

    public static Course fromCursor(Cursor cursor) {
        Course course = new Course();
        course.id = cursor.getLong(cursor.getColumnIndex(WGUContract.Courses._ID));
        if (!cursor.isNull(cursor.getColumnIndex(WGUContract.Courses.TERM_ID)))
            course.termId = cursor.getLong(cursor.getColumnIndex(WGUContract.Courses.TERM_ID));
        course.title = cursor.getString(cursor.getColumnIndex(WGUContract.Courses.TITLE));
        course.status = Course.Status.fromString(cursor.getString(cursor.getColumnIndex(WGUContract.Courses.STATUS)));
        course.startDate = new Date(cursor.getLong(cursor.getColumnIndex(WGUContract.Courses.START_DATE)) * 1000);
        course.anticipatedEndDate = new Date(cursor.getLong(cursor.getColumnIndex(WGUContract.Courses.END_DATE)) * 1000);
        return course;
    }

    public enum Status {
        InProgress,
        Completed,
        Dropped,
        PlanToTake;

        @Override
        public String toString() {
            switch (this) {
                case InProgress:
                    return "In Progress";
                case Completed:
                    return "Completed";
                case Dropped:
                    return "Dropped";
                case PlanToTake:
                    return "Plan to Take";
            }
            return null;
        }

        public static Status fromString(String status) {
            switch (status) {
                case "In Progress":
                    return InProgress;
                case "Completed":
                    return Completed;
                case "Dropped":
                    return Dropped;
                case "Plan to Take":
                    return PlanToTake;
            }
            return null;
        }
    }
}
