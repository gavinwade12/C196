package edu.wgu.gavin.c196.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import static edu.wgu.gavin.c196.data.WGUContract.*;

public class WGUProvider extends ContentProvider {

    private static final int ASSESSMENTS = 1;
    private static final int ASSESSMENTS_ID = 2;
    private static final int COURSES = 3;
    private static final int COURSES_ID = 4;
    private static final int COURSE_MENTORS = 5;
    private static final int COURSE_MENTORS_ID = 6;
    private static final int COURSE_NOTES = 7;
    private static final int COURSE_NOTES_ID = 8;
    private static final int TERMS = 9;
    private static final int TERMS_ID = 10;

    private static final String ID_PATH = "/#";
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, Assessments.TABLE_NAME, ASSESSMENTS);
        uriMatcher.addURI(AUTHORITY, Assessments.TABLE_NAME+ID_PATH, ASSESSMENTS_ID);
        uriMatcher.addURI(AUTHORITY, Courses.TABLE_NAME, COURSES);
        uriMatcher.addURI(AUTHORITY, Courses.TABLE_NAME+ID_PATH, COURSES_ID);
        uriMatcher.addURI(AUTHORITY, CourseMentors.TABLE_NAME, COURSE_MENTORS);
        uriMatcher.addURI(AUTHORITY, CourseMentors.TABLE_NAME+ID_PATH, COURSE_MENTORS_ID);
        uriMatcher.addURI(AUTHORITY, CourseNotes.TABLE_NAME, COURSE_NOTES);
        uriMatcher.addURI(AUTHORITY, CourseNotes.TABLE_NAME+ID_PATH, COURSE_NOTES_ID);
        uriMatcher.addURI(AUTHORITY, Terms.TABLE_NAME, TERMS);
        uriMatcher.addURI(AUTHORITY, Terms.TABLE_NAME+ID_PATH, TERMS_ID);
    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        DBHelper helper = new DBHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        ModelInfo info = ModelInfo.fromUri(uri);
        if (info == null)
            return null;

        if (info.singular) {
            selection = "_id = ?";
            selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
        }
        return database.query(info.tableName, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public String getType(@NonNull Uri uri) {
        ModelInfo info = ModelInfo.fromUri(uri);
        if (info == null)
            return null;
        return info.mimeType;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        ModelInfo info = ModelInfo.fromUri(uri);
        if (info == null)
            return null;

        long id;
        try {
            id = database.insert(info.tableName, null, values);
        } catch (Exception e) {
            Log.d(getClass().getName(), e.toString());
            return null;
        }

        return Uri.withAppendedPath(uri, String.valueOf(id));
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        ModelInfo info = ModelInfo.fromUri(uri);
        if (info == null)
            return -1;

        if (info.singular) {
            selection = "_id = ?";
            selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
        }
        return database.delete(info.tableName, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        ModelInfo info = ModelInfo.fromUri(uri);
        if (info == null)
            return -1;

        if (info.singular) {
            selection = "_id = ?";
            selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
        }
        return database.update(info.tableName, values, selection, selectionArgs);
    }

    private static class ModelInfo {

        String tableName;
        String[] columns;
        String mimeType;
        boolean singular;

        ModelInfo(String tableName, String[] columns) {
            this(tableName, columns, false);
        }

        ModelInfo(String tableName, String[] columns, boolean singular) {
            this.tableName = tableName;
            this.columns = columns;
            this.singular = singular;
            if (singular)
                this.mimeType = "vnd.android.cursor.item/vnd.edu.wgu.gavin.c196.provider." + tableName;
            else
                this.mimeType = "vnd.android.cursor.dir/vnd.edu.wgu.gavin.c196.provider." + tableName;
        }

        static ModelInfo fromUri(Uri uri) {
            switch (uriMatcher.match(uri)) {
                case ASSESSMENTS:
                    return new ModelInfo(Assessments.TABLE_NAME, Assessments.COLUMNS);
                case ASSESSMENTS_ID:
                    return new ModelInfo(Assessments.TABLE_NAME, Assessments.COLUMNS, true);
                case COURSES:
                    return new ModelInfo(Courses.TABLE_NAME, Courses.COLUMNS);
                case COURSES_ID:
                    return new ModelInfo(Courses.TABLE_NAME, Courses.COLUMNS, true);
                case COURSE_MENTORS:
                    return new ModelInfo(CourseMentors.TABLE_NAME, CourseMentors.COLUMNS);
                case COURSE_MENTORS_ID:
                    return new ModelInfo(CourseMentors.TABLE_NAME, CourseMentors.COLUMNS, true);
                case COURSE_NOTES:
                        return new ModelInfo(CourseNotes.TABLE_NAME, CourseNotes.COLUMNS);
                case COURSE_NOTES_ID:
                    return new ModelInfo(CourseNotes.TABLE_NAME, CourseNotes.COLUMNS, true);
                case TERMS:
                    return new ModelInfo(Terms.TABLE_NAME, Terms.COLUMNS);
                case TERMS_ID:
                    return new ModelInfo(Terms.TABLE_NAME, Terms.COLUMNS, true);
            }
            return null;
        }

    }

}
