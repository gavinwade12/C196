package edu.wgu.gavin.c196.data;

import android.net.Uri;

public final class WGUContract {

    public static final String AUTHORITY = "edu.wgu.gavin.c196.provider";

    public static class Assessments {

        public static final String TABLE_NAME = "assessments";
        public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);

        public static final String _ID = "_id";
        public static final String NOTES = "notes";
        public static final String TYPE = "type";

        public static final String[] COLUMNS = {_ID, NOTES, TYPE};

    }

    public static class Courses {

        public static final String TABLE_NAME = "courses";
        public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);

        public static final String _ID = "_id";
        public static final String TITLE = "title";
        public static final String START_DATE = "start_date";
        public static final String END_DATE = "end_date";
        public static final String STATUS = "status";
        public static final String NOTES = "notes";

        public static final String[] COLUMNS = {_ID, TITLE, START_DATE, END_DATE, STATUS, NOTES};

    }

    public static class CourseMentors {

        public static final String TABLE_NAME = "course_mentors";
        public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);

        public static final String _ID = "_id";
        public static final String NAME = "name";
        public static final String PHONE_NUMBER = "phone_number";
        public static final String EMAIL_ADDRESS = "email_address";

        public static final String[] COLUMNS = {_ID, NAME, PHONE_NUMBER, EMAIL_ADDRESS};

    }

    public static class ScheduledAlerts {

        public static final String TABLE_NAME = "scheduled_alerts";
        public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);

        public static final String _ID = "_id";
        public static final String DATE = "date";
        public static final String NAME = "name";

        public static final String[] COLUMNS = {_ID, DATE, NAME};

    }

    public static class Terms {

        public static final String TABLE_NAME = "terms";
        public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);

        public static final String _ID = "_id";
        public static final String TITLE = "title";
        public static final String START_DATE = "start_date";
        public static final String END_DATE = "end_date";

        public static final String[] COLUMNS = {_ID, TITLE, START_DATE, END_DATE};

    }

    public static class CoursesCourseMentorsMapping {

        public static final String TABLE_NAME = "courses_course_mentors";
        public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);

        public static final String _ID = "_id";
        public static final String COURSE_MENTOR_ID = "course_mentor_id";
        public static final String COURSE_ID = "course_id";

        public static final String[] COLUMNS = {_ID, COURSE_MENTOR_ID, COURSE_ID};

    }

    public static class CoursesAssessmentsMapping {

        public static final String TABLE_NAME = "courses_assessments";
        public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);

        public static final String _ID = "_id";
        public static final String COURSE_ID = "course_id";
        public static final String ASSESSMENT_ID = "assessment_id";

        public static final String[] COLUMNS = {_ID, COURSE_ID, ASSESSMENT_ID};

    }

    public static class TermsCoursesMapping {

        public static final String TABLE_NAME = "terms_courses";
        public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);

        public static final String _ID = "_id";
        public static final String TERM_ID = "term_id";
        public static final String COURSE_ID = "course_id";

        public static final String[] COLUMNS = {_ID, TERM_ID, COURSE_ID};

    }

}
