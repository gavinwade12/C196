package edu.wgu.gavin.c196.data;

import android.net.Uri;

public final class WGUContract {

    public static final String AUTHORITY = "edu.wgu.gavin.c196.provider";

    public static class Terms {

        public static final String TABLE_NAME = "terms";
        public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);

        public static final String _ID = "_id";
        public static final String TITLE = "title";
        public static final String START_DATE = "start_date";
        public static final String END_DATE = "end_date";

        public static final String[] COLUMNS = {_ID, TITLE, START_DATE, END_DATE};

    }

    public static class Courses {

        public static final String TABLE_NAME = "courses";
        public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);

        public static final String _ID = "_id";
        public static final String TITLE = "title";
        public static final String START_DATE = "start_date";
        public static final String END_DATE = "end_date";
        public static final String STATUS = "status";
        public static final String TERM_ID = "term_id";

        public static final String[] COLUMNS = {_ID, TITLE, START_DATE, END_DATE, STATUS, TERM_ID};

    }

    public static class CourseNotes {

        public static final String TABLE_NAME = "course_notes";
        public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);

        public static final String _ID = "_id";
        public static final String NOTE = "note";
        public static final String COURSE_ID = "course_id";

        public static final String[] COLUMNS = {_ID, NOTE, COURSE_ID};
    }

    public static class Assessments {

        public static final String TABLE_NAME = "assessments";
        public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);

        public static final String _ID = "_id";
        public static final String TITLE = "title";
        public static final String TYPE = "type";
        public static final String START_DATE = "start_date";
        public static final String DUE_DATE = "due_date";
        public static final String COURSE_ID = "course_id";

        public static final String[] COLUMNS = {_ID, TITLE, TYPE, START_DATE, DUE_DATE, COURSE_ID};

    }

    public static class CourseMentors {

        public static final String TABLE_NAME = "course_mentors";
        public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);

        public static final String _ID = "_id";
        public static final String NAME = "name";
        public static final String PHONE_NUMBER = "phone_number";
        public static final String EMAIL_ADDRESS = "email_address";
        public static final String COURSE_ID = "course_id";

        public static final String[] COLUMNS = {_ID, NAME, PHONE_NUMBER, EMAIL_ADDRESS, COURSE_ID};

    }

}
